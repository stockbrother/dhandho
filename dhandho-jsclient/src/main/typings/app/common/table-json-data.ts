import { JsonData } from './json-data';

export class TableJsonData extends JsonData {
    constructor( json: any ) {
        super( 'table', json );
    }

    public static valueOf( json: any ): TableJsonData {
        const rt: TableJsonData = new TableJsonData( json );
        return rt;
    }
}
