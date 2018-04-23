export interface IAppIdentity {
    hostname: string;
    envVars: IEnvVar[];
}

export interface IEnvVar {
    [s: string]: string;
}
