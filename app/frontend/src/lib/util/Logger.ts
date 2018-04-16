export interface ILogger {
  namespace: string;

  info (msg: any);

  warn (msg: any);

  error (msg: any);
}

export class Logger implements ILogger {
  public namespace: string;

  constructor(namespace: string) {
    this.namespace = namespace;
  }

  public info(msg: any, data?: any) {
    console.info(`[${this.namespace}] ${msg}`, data);
  }

  public warn(msg: any, data?: any) {
    console.warn(msg, data);
  }

  public error(msg: any, data?: any) {
    console.error(msg, data);
  }

}
