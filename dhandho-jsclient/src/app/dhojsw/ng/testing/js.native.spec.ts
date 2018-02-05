/* Generated from Java with JSweet 2.0.1 - http://www.jsweet.org */
import jsnative = require('jsnative');
import jasmine = require('jasmine');

import js_isNumber = jsnative.js_isNumber;
import ArrayLikeMatchers = jasmine.ArrayLikeMatchers;
export class JsNativeSpec {
    public static main(args: string[]) {
        describe('JsNativeSpec Test1:Some native test', () => {
            it('js_isNumber() method test', (t: DoneFn) => {
                let yes: boolean = js_isNumber(1.1);
                expect(yes).toBe(true);
                t();
            });
        });
        describe('JsNativeSpec Test2: String expect test', () => {
            it('expect String', (done) => {
                let matchers: ArrayLikeMatchers<string> = expect('hello');
                matchers.toBeTruthy('Not truth');
                done();
            });
        });
    }
}

JsNativeSpec.main(null);
