/* Generated from Java with JSweet 2.0.1 - http://www.jsweet.org */
import core_testing = require('@angular/core/testing');

import async = core_testing.async;
import fakeAsync = core_testing.fakeAsync;
import tick = core_testing.tick;
/**
 * A bridge class.
 *
 * @author Wu
 * @class
 */
export class Angular_ {
    public static async_<T>(run: () => void): (p1: T) => void {
        return <any>(async(<any>(run)));
    }

    public static fakeAsync_<T>(run: () => void): (p1: T) => void {
        return <any>(fakeAsync(<any>(run)));
    }

    public static tick_$() {
        tick();
    }

    public static tick_$double(time: number) {
        tick(time);
    }

    public static tick_(time?: any): any {
        if(((typeof time === 'number') || time === null)) {
            return <any>Angular_.tick_$double(time);
        } else if(time === undefined) {
            return <any>Angular_.tick_$();
        } else throw new Error('invalid overload');
    }
}
Angular_['__class'] = 'app.dhojsw.ng.util.Angular_';



