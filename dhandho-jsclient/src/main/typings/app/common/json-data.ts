/**
 * @deprecated
 */
export abstract class JsonData {
    public type: string = null;

    public json: any = null;

    public constructor( type: string, json: any ) {
        this.json = json;
        this.type = type;
    }
}
