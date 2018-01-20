export class CommandResponse {
    public command: string;
    public json: any;
    public responseTime: number;
    public requestTime: number;
    public constructor( requestTime: number, command: string, json: any ) {

        if ( this.command === undefined ) {
            this.command = null;
        }
        if ( this.json === undefined ) {
            this.json = null;
        }
        if ( this.responseTime === undefined ) {
            this.responseTime = 0;
        }
        if ( this.requestTime === undefined ) {
            this.requestTime = 0;
        }
        this.requestTime = requestTime;
        this.json = json;
        this.command = command;
        this.responseTime = /* currentTimeMillis */Date.now();
    }

    public getTimeCose(): number {
        return this.responseTime - this.requestTime;
    }
}

