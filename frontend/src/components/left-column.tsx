import React from 'react'
import { Stream } from '../events/endpoint'
import { CloudEvent } from 'cloudevents'
import InfoProps from './info-props'

interface LeftColumnProps extends InfoProps {
  stream: Stream<CloudEvent>
}

interface LeftColumnState {
  events: CloudEvent[]
}

class LeftColumn extends React.Component<LeftColumnProps, LeftColumnState> {
  engine: string;

  constructor(props: LeftColumnProps) {
    super(props)
    this.state = {
      events: []
    }
    this.engine = props.info.project.platform.replace(/^([a-zA-Z0-9]+)\/.+$/, '$1')
    props.stream.onData((ce: CloudEvent) => {
      this.setState({
        events: [ce, ...this.state.events]
      })
    })
  }
  
  render(): React.ReactNode {
    return (
      <section className="left-column">
      <h2>What can I do from here?</h2>

      <p>
        Invoke a hello endpoint: <a href="/hello">/hello</a>.<br />
        <span className="note">💡 It will send CloudEvent to <code>K_SINK = {this.props.info.config.sink}</code></span>
      </p>

      <h4>Collected events:</h4>
      <ul id="events-list">
        {this.renderEvents()}
      </ul>

      <h4>Powered by:</h4>
      <div className="references">
        {this.renderEngine()}

        <img className="thumbnail"
          src="//raw.githubusercontent.com/knative/website/master/assets/icons/logo.svg"
          alt="Knative" />
      </div>
      <p>This application has been written with {this.engine} to showcase Knative.</p>
    </section>
    )
  }

  renderEvents(): React.ReactNode {
    return this.state.events.map((ce: CloudEvent, i: number) => {
      return (<li key={i}><code>{JSON.stringify(ce.data)}</code></li>)
    })
  }

  renderEngine(): React.ReactNode {
    switch (this.engine) {
      case 'Express':
        return (
          <img className="thumbnail"
            alt="Express"
            src="//upload.wikimedia.org/wikipedia/commons/d/d9/Node.js_logo.svg" />
        )
      case 'Quarkus':
        return (
          <img className="thumbnail"
            alt="Quarkus"
            src="//design.jboss.org/quarkus/logo/final/SVG/quarkus_logo_vertical_rgb_default.svg" />
        )
      default:
        throw new Error(`Unsupported engine: ${this.engine}`);
    }
  }
}

export default LeftColumn
