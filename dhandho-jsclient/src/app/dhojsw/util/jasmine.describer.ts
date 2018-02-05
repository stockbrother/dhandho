/* Generated from Java with JSweet 2.0.1 - http://www.jsweet.org */
import jasmine = require('jasmine');
import { Jasmine_ } from './Jasmine_';

import Matchers = jasmine.Matchers;
/**
 * 
 * @author Wu
 * @param {string} desc
 * @class
 */
export class JasmineDescriber {
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

    /**
     *
     */
    public describe() {
        Jasmine_.describe_(this.desc, () => {
            this.run();
        });
    }

    public it$java_lang_String$java_util_function_Consumer(desc: string, func: (p1: DoneFn) => void): JasmineDescriber {
        Jasmine_.it_$java_lang_String$java_util_function_Consumer(desc, <any>(func));
        return this;
    }

    public it(desc?: any, func?: any): any {
        if (((typeof desc === 'string') || desc === null) && ((typeof func === 'function' && (<any>func).length === 1) || func === null)) {
            return <any>this.it$java_lang_String$java_util_function_Consumer(desc, func);
        } else if (((typeof desc === 'string') || desc === null) && ((typeof func === 'function' && (<any>func).length === 0) || func === null)) {
            return <any>this.it$java_lang_String$java_lang_Runnable(desc, func);
        } else {
            throw new Error('invalid overload');
        }
    }

    it$java_lang_String$java_lang_Runnable(desc: string, func: () => void): JasmineDescriber {
        Jasmine_.it_$java_lang_String$java_lang_Runnable(desc, <any>(func));
        return this;
    }

    beforeEach(run: () => void): JasmineDescriber {
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




