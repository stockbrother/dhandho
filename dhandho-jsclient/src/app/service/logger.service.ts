import { Injectable } from '@angular/core';

@Injectable()
export class LoggerService {

    public debug( msg: any ) {
        // console.debug(/* valueOf */new String( msg ).toString() );
    }

    public log( msg: any ) {
        // console.log(/* valueOf */new String( msg ).toString() );
    }

    public warn( msg: any ) {
        // console.warn(/* valueOf */new String( msg ).toString() );
    }

    public error( msg: any ) {
        // console.error(/* valueOf */new String( msg ).toString() );
    }
}
