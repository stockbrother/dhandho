/* Generated from Java with JSweet 2.0.1 - http://www.jsweet.org */
import core_testing = require('@angular/core/testing');
import { JasmineDescriber } from '../../util/JasmineDescriber';
import { Logger } from './Logger';

import TestBed = core_testing.TestBed;
export class LoggerSpec extends JasmineDescriber {
    public static main(args: string[]) {
        new LoggerSpec('Logger Test').describe();
    }

    logger: Logger;

    public constructor(desc: string) {
        super(desc);
        if(this.logger === undefined) this.logger = null;
    }

    /**
     * 
     */
    public run() {
        this.beforeEach(() => {
            let meta: any = {};
            meta.providers = [Logger];
            TestBed.configureTestingModule(meta);
            TestBed.compileComponents();
            this.logger = <any>(TestBed.get(Logger));
        });
        this.it$java_lang_String$java_util_function_Consumer('Assert logger is injected.', (done) => {
            this.expect<any>(this.logger).toBeTruthy('Logger not able being injected.');
            this.logger.debug('debug msg');
            this.logger.info('info msg');
            this.logger.warn('warn msg');
            this.logger.error('error msg');
            done();
        });
    }
}
LoggerSpec['__class'] = 'app.dhojsw.ng.service.LoggerSpec';
LoggerSpec['__interfaces'] = ['java.lang.Runnable'];





LoggerSpec.main(null);
