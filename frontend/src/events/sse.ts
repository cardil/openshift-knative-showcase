import { CloudEvent } from './u-cloudevents'
import { Endpoint, Stream } from './endpoint'

export default class ServerSentEvents implements Endpoint {
  stream(): Stream<CloudEvent> {
    return new SseStream()
  }
}

class SseStream implements Stream<CloudEvent> {
  private listeners: ((ce: CloudEvent) => void)[] = []
  
  close(): void {
    throw new Error('Method not implemented.')
  }
  reconnect(): void {
    throw new Error('Method not implemented.')
  }
  onData(listener: (data: CloudEvent<any>) => void): void {
    throw new Error('Method not implemented.')
  }
  onError(listener: (error: Error) => void): void {
    throw new Error('Method not implemented.')
  }
}
