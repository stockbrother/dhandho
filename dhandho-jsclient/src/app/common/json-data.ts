export abstract class JsonData {
    public type: string;

    public json: any;

    public constructor( type: string, json: any ) {
        if ( this.type === undefined ) { this.type = null; }
        if ( this.json === undefined ) { this.json = null; }
        this.json = json;
        this.type = type;
    }
}
