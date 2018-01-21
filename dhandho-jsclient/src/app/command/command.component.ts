import { Component } from '@angular/core';
import { HttpModule, Http, Response } from '@angular/http';
import { CommandResponse } from './command-response';
import { LoggerService } from '../service/logger.service';
import { Observable } from 'rxjs/Observable';

@Component( {
    selector: 'app-command',
    templateUrl: './command.component.html',
    styleUrls: ['./command.component.css']
} )
export class CommandComponent {

    public title: string;

    public command: string;

    public responseArray: Array<CommandResponse> = <any>( new Array<any>() );

    public lastResponse: CommandResponse;

    url = '/web/cmd/';

    http: Http;

    log: LoggerService;

    public constructor( http: Http, log: LoggerService ) {
        this.title = 'Command Component';
        if ( this.command === undefined ) { this.command = null; }
        if ( this.lastResponse === undefined ) { this.lastResponse = null; }
        if ( this.http === undefined ) { this.http = null; }
        if ( this.log === undefined ) { this.log = null; }
        this.http = http;
        this.log = log;
    }

    public onResponse( requestTime: number, command: string, json: any ) {
        const typeP: PropertyDescriptor = Object.getOwnPropertyDescriptor( json, 'type' );
        const type: string = <string>typeP.value;
        const names: Array<string> = Object.getOwnPropertyNames( json );
        const rt: CommandResponse = new CommandResponse( requestTime, command, json );
        this.responseArray.unshift( rt );
    }

    public onButtonClick() {
        // console.log( "on Button Click,command:" + this.command );
        const command: string = this.command;
        const requestTime: number = /* currentTimeMillis */Date.now();
        const ores = this.http.post( this.url, command );

        ores.toPromise().then(( value: Response ) => {
            let text1: string = value.toString();
            let text: string = value.text();
            console.log(value);
            const json: any = JSON.parse( text1 );
            this.onResponse( requestTime, command, json );
            this.log.debug( 'post response:' + json );
            return null;
        } ).catch(( t: any ) => {
            this.log.debug( 'catchOnRejectedFunction,command:' + command );
            return null;
        } );
    }

    public showChart(): boolean {
        return true;
    }

    public isNumber( json: Object ): boolean {
        const type: string = typeof json;
        this.log.debug( 'typeof,json:' + json + ',is:' + type );
        return /* equals */( <any>( ( o1: any, o2: any ) => {
            if ( o1 && o1.equals ) {
                return o1.equals( o2 );
            } else {
                return o1 === o2;
            }
        } )( type, 'number' ) );
    }
}
