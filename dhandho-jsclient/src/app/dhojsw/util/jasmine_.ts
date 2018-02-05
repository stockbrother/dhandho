/* Generated from Java with JSweet 2.0.1 - http://www.jsweet.org */
import jasmine = require('jasmine');

import Matchers = jasmine.Matchers;
/**
 * This is a Bridge class.<br>
 * Why this class? Because we unable to define a 'describe' named method in a
 * class that calling describe() method in another class, the
 * def.jasmine.Globals.describe. <br>
 * So we define a bridge caller here.
 * 
 * @author wu
 * @class
 */
export class Jasmine_ {
    public static describe_(desc: string, run: () => void) {
        describe(desc, <any>(run));
    }

    public static it_$java_lang_String$java_util_function_Consumer(desc: string, fun: (p1: DoneFn) => void) {
        it(desc, <any>(fun));
    }

    public static it_(desc?: any, fun?: any): any {
        if (((typeof desc === 'string') || desc === null) && ((typeof fun === 'function' && (<any>fun).length == 1) || fun === null)) {
            return <any>Jasmine_.it_$java_lang_String$java_util_function_Consumer(desc, fun);
        } else if (((typeof desc === 'string') || desc === null) && ((typeof fun === 'function' && (<any>fun).length == 0) || fun === null)) {
            return <any>Jasmine_.it_$java_lang_String$java_lang_Runnable(desc, fun);
        } else throw new Error('invalid overload');
    }

    public static it_$java_lang_String$java_lang_Runnable(desc: string, fun: () => void) {
        it(desc, <any>(fun));
    }

    public static beforeEach_(run: () => void) {
        beforeEach(<any>(run));
    }

    public static expect_<T>(obj: T): Matchers<T> {
        return expect(obj);
    }
}
Jasmine_['__class'] = 'app.dhojsw.util.Jasmine_';



