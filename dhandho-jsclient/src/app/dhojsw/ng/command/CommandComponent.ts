/* Generated from Java with JSweet 2.0.1 - http://www.jsweet.org */
import jsnative = require( 'jsnative' );
import common_http = require( '@angular/common/http' );
import core = require( '@angular/core' );
import rxjs = require( 'rxjs' );
import { Logger } from '../service/Logger';
import { CommandResponse } from './CommandResponse';

import js_isNumber = jsnative.js_isNumber;
import HttpClient = common_http.HttpClient;
import Component = core.Component;
import Observable = rxjs.Observable;
@core.Component( { templateUrl: './command.component.html', styleUrls: ['./command.component.css'] } )
export class CommandComponent {
    public title: string;

    public command: string;

    public responseArray: Array<CommandResponse> = <any>( new Array<any>() );

    url: string = '/web/cmd/';

    http: HttpClient;

    log: Logger;

    public constructor( http: HttpClient, log: Logger ) {
        if ( this.title === undefined ) this.title = null;
        if ( this.command === undefined ) this.command = null;
        if ( this.http === undefined ) this.http = null;
        if ( this.log === undefined ) this.log = null;
        this.title = 'Command Component';
        this.http = http;
        this.log = log;
    }

    public onResponse( requestTime: number, command: string, json: any ) {
        let typeP: PropertyDescriptor = Object.getOwnPropertyDescriptor( json, 'type' );
        let type: string = <string>typeP.value;
        this.log.debug( 'type:' + type );
        let rt: CommandResponse = new CommandResponse( requestTime, command, json );
        this.responseArray.unshift( rt );
    }

    public onButtonClick() {
        let command: string = this.command;
        let requestTime: number = Date.now();
        let ores: Observable<any> = this.http.post( this.url, command );
        ores.toPromise().then(( ( requestTime, command ) => {
            return ( json ) => {
                this.log.debug( json );
                this.onResponse( requestTime, command, json );
                this.log.debug( 'post response:' + json );
                return null;
            }
        } )( requestTime, command ) ).catch(( t ) => {
            this.log.error( 'catched exception' );
            this.log.error( t );
            return null;
        } );
    }

    public showChart(): boolean {
        return true;
    }

    public isNumber( json: any ): boolean {
        return js_isNumber( json );
    }
}
CommandComponent['__class'] = 'app.dhojsw.ng.command.CommandComponent';



