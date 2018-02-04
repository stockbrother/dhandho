/* Generated from Java with JSweet 2.0.1 - http://www.jsweet.org */
import core = require('@angular/core');

import Injectable = core.Injectable;
@Injectable()
export class Logger {
    public info(msg: any, ...args: any[]) {
        (o => o.info.apply(o, [msg].concat(<any[]>args)))(console);
    }

    public debug(msg: any, ...args: any[]) {
        (o => o.debug.apply(o, [msg.toString()].concat(<any[]>args)))(console);
    }

    public trace(msg: any, ...args: any[]) {
        (o => o.info.apply(o, [msg].concat(<any[]>args)))(console);
    }

    public warn(msg: any, ...args: any[]) {
        (o => o.warn.apply(o, [msg].concat(<any[]>args)))(console);
    }

    public error(msg: any, ...args: any[]) {
        (o => o.info.apply(o, [msg].concat(<any[]>args)))(console);
    }
}
Logger['__class'] = 'app.dhojsw.ng.service.Logger';



