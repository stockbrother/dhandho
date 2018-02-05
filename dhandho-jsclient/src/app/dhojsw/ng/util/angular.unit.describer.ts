/* Generated from Java with JSweet 2.0.1 - http://www.jsweet.org */
import jasmine = require('jasmine');
import { Jasmine_ } from '../../util/Jasmine_';
import { Angular_ } from './Angular_';

import Matchers = jasmine.Matchers;

export class AngularUnitDescriber {
    desc: string;

    public constructor(desc?: any) {
        if (((typeof desc === 'string') || desc === null)) {
            let __args = Array.prototype.slice.call(arguments);

            (() => {
                this.desc = desc;
            })();
        } else if (desc === undefined) {
            let __args = Array.prototype.slice.call(arguments);

            (() => {
                this.desc = /* getName */(c => c['__class'] ? c['__class'] : c['name'])((<any>this.constructor));
            })();
        } else {
            throw new Error('invalid overload');
        }
    }

    public async<T>(run: () => void): (p1: T) => void {
        return <any>(Angular_.async_<any>(<any>(run)));
    }

    public fakeAsync<T>(run: () => void): (p1: T) => void {
        return <any>(Angular_.fakeAsync_<any>(<any>(run)));
    }

    public tick$() {
        Angular_.tick_();
    }

    public tick$double(time: number) {
        Angular_.tick_$double(time);
    }

    public tick(time?: any): any {
        if (((typeof time === 'number') || time === null)) {
            return <any>this.tick$double(time);
        } else if (time === undefined) {
            return <any>this.tick$();
        } else {
            throw new Error('invalid overload');
        }
    }

    /**
     *
     */
    public describe() {
        Jasmine_.describe_(this.desc, () => {
            this.run();
        });
    }

    itFakeAsync(desc: string, func: () => void): AngularUnitDescriber {
        Jasmine_.it_$java_lang_String$java_util_function_Consumer(desc, <any>(Angular_.fakeAsync_<any>(<any>(func))));
        return this;
    }

    itAsync(desc: string, func: () => void): AngularUnitDescriber {
        Jasmine_.it_$java_lang_String$java_util_function_Consumer(desc, <any>(Angular_.async_<any>(<any>(func))));
        return this;
    }

    public it$java_lang_String$java_lang_Runnable(desc: string, func: () => void): AngularUnitDescriber {
        Jasmine_.it_$java_lang_String$java_lang_Runnable(desc, <any>(func));
        return this;
    }

    public it(desc?: any, func?: any): any {
        if (((typeof desc === 'string') || desc === null) && ((typeof func === 'function' && (<any>func).length === 0) || func === null)) {
            return <any>this.it$java_lang_String$java_lang_Runnable(desc, func);
        } else if (((typeof desc === 'string') || desc === null) && ((typeof func === 'function' && (<any>func).length === 1) || func === null)) {
            return <any>this.it$java_lang_String$java_util_function_Consumer(desc, func);
        } else {
            throw new Error('invalid overload');
        }
    }

    it$java_lang_String$java_util_function_Consumer(desc: string, func: (p1: DoneFn) => void): AngularUnitDescriber {
        Jasmine_.it_$java_lang_String$java_util_function_Consumer(desc, <any>(func));
        return this;
    }

    beforeEach(run: () => void): AngularUnitDescriber {
        Jasmine_.beforeEach_(<any>(run));
        return this;
    }

    public expect<T>(obj: T): Matchers<T> {

        return Jasmine_.expect_<any>(obj);
    }

    /**
     *
     */
    public run() {
    }
}
