import AdmZip from 'adm-zip'
import fs from 'fs/promises'
import os from 'os'
import { green, yellow, cyan, magenta, Color, blue, white } from 'colorette'
import { WasmRegistry, Image } from 'wasm-oci'

type Packager = (packer: AdmZip, log: Logger) => Promise<void>

interface Webjar {
  group: string
  artifact: string
  version: string
  color: Color,
  build: Packager
}

const webjars: Webjar[] = [{
  group: 'com.redhat.openshift.knative.showcase',
  artifact: 'frontend',
  version: 'main',
  color: cyan,
  build: async p => {
    p.addLocalFolder('./build', 'META-INF/resources', (path) => {
      return !path.includes('index.html')
    })
    p.addLocalFile('./build/index.html', 'META-INF/resources', 'home.html')
  }
}, {
  group: 'com.redhat.openshift.knative.showcase',
  artifact: 'cloudevents-pp-wasm',
  version: 'main',
  color: magenta,
  build: async (p, log) => {
    const tmpDir = os.tmpdir()
    const tmp = await fs.mkdtemp(`${tmpDir}/wasm-oci-`)
    const reg = new WasmRegistry(tmp)
    const image = Image.parse('quay.io/cardil/cloudevents-pretty-print@sha256:134269c7090bcb923d8b8764920bb25923e38411413eac7e4240524f1a40dc74')
    log(`Pulling image: ${green(image.toString())}`)
    const wasm = await reg.pull(image)
    p.addFile('META-INF/cloudevents-pretty-print.wasm', await fs.readFile(wasm.file), 'Wasm')
    await fs.rm(tmp, { recursive: true })
  }
}]

type Logger = (message?: any, ...optionalParams: any[]) => void

function createLogger(name: string): Logger {
  return (msg) => {
    console.log(`[${name}] ${msg}`)
  }
}

async function buildWebjar(webjar: Webjar) {
  const log = createLogger(webjar.color(webjar.artifact))
  const jarDir = `${process.env.HOME}/.m2/repository/` +
    `${webjar.group.replace(/\./g, '/')}/` +
    `${webjar.artifact}/` + webjar.version
  const jarPath = `${jarDir}/${webjar.artifact}-${webjar.version}.jar`
  const pomPath = `${jarDir}/${webjar.artifact}-${webjar.version}.pom`

  const pom = `<?xml version="1.0" encoding="UTF-8"?>
  <project xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd"
  xmlns="http://maven.apache.org/POM/4.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<modelVersion>4.0.0</modelVersion>
<groupId>${webjar.group}</groupId>
<artifactId>${webjar.artifact}</artifactId>
<version>${webjar.version}</version>
<packaging>jar</packaging>
// Build by: forntend/scripts/webjar.ts script
</project>
`
  const zip = new AdmZip()
  await webjar.build(zip, log)
  zip.addFile(`META-INF/maven/${webjar.group}/${webjar.artifact}/pom.xml`, Buffer.from(pom))
  zip.writeZip(jarPath)
  log(`Created webjar: ${yellow(jarPath)}`)
  await fs.writeFile(pomPath, pom)
  log(`Created webjar POM: ${yellow(pomPath)}`)
  log('To use it, add following to your pom.xml file:\n' + blue(
    `
    <dependency>
      <groupId>${white(webjar.group)}</groupId>
      <artifactId>${white(webjar.artifact)}</artifactId>
      <version>${white(webjar.version)}</version>
    </dependency>
  `))
}

async function build() {
  const ps : Promise<void>[] = []
  for (const webjar of webjars) {
    ps.push(buildWebjar(webjar))
  }
  await Promise.all(ps)
}

build()
  .catch(e => {
    console.error(e)
    process.exit(1)
  })
  .then(() => process.exit(0))
