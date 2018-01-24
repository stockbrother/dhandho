import { Injectable } from '@angular/core';

@Injectable()
export class LoggerService {

    public info( msg: any ) {
        console.info( msg );
    }

    public trace( msg?: any, ...args: any[] ) {
        console.debug( msg, args );
    }

    public debug( msg: any, ...args: any[] ) {
        console.debug( msg, args );
    }

    public log( msg: any ) {
        console.log( msg );
    }

    public warn( msg: any ) {
        console.warn( msg );
    }

    public error( msg: any ) {
        console.error( msg );
    }
}
