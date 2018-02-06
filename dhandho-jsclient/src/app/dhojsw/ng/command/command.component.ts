/* Generated from Java with JSweet 2.0.1 - http://www.jsweet.org */
import jsnative = require('jsnative');
import common_http = require('@angular/common/http');
import core = require('@angular/core');
import rxjs = require('rxjs/Observable');
import { Logger } from '../service/logger';
import { BackendInterface } from '../service/backend.interface';
import { CommandResponse } from './command.response';

import js_isNumber = jsnative.js_isNumber;
import HttpClient = common_http.HttpClient;
import Component = core.Component;
import Observable = rxjs.Observable;

@Component({
    templateUrl: './command.component.html',
    styleUrls: ['./command.component.css']
})
export class CommandComponent {
    public title: string;

    public command: string;

    public responseArray: Array<CommandResponse> = <any>(new Array<any>());

    url: string = '/web/cmd/';

    backend: BackendInterface;

    log: Logger;

    public constructor(http: BackendInterface, log: Logger) {
        this.title = 'Command Component';
        this.backend = http;
        this.log = log;
    }

    public onResponse(requestTime: number, command: string, json: any) {
        let typeP: PropertyDescriptor = Object.getOwnPropertyDescriptor(json, 'type');
        let type: string = <string>typeP.value;
        this.log.debug('type:' + type);
        let rt: CommandResponse = new CommandResponse(requestTime, command, json);
        this.responseArray.unshift(rt);
    }

    public onButtonClick() {
        let command: string = this.command;
        let requestTime: number = Date.now();
        let ores: Observable<any> = this.backend.post(this.url, command);
        ores.toPromise().then(((trequestTime, tcommand) => {
            return (json) => {
                this.log.debug(json);
                this.onResponse(trequestTime, tcommand, json);
                this.log.debug('post response:' + json);
                return null;
            };
        })(requestTime, command)).catch((t) => {
            this.log.error('catched exception');
            this.log.error(t);
            return null;
        });
    }

    public showChart(): boolean {
        return true;
    }

    public isNumber(json: any): boolean {
        return js_isNumber(json);
    }
}
