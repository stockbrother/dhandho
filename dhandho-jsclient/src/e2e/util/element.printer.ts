import { protractor, browser, element, by, ElementArrayFinder, ElementFinder } from 'protractor';
import util = require('util');
import { LogsUtil } from './e2e.util';

let console = LogsUtil.console;

export class ElementPrinter {
    root: ElementFinder;
    public constructor(root?: ElementFinder) {
        this.root = root;
        if (this.root === null) {
            // this.root = element.all();
        }
    }

    public toString = (): string => {
        let rt: any[] = ['start:'];
        this.append(rt, this.root);
        return rt.join();
    }

    private append(buf: any[], ef: ElementFinder): void {
        buf.push(',ef:' + ef);
        let children: ElementArrayFinder = ef.all(by.xpath('//child::*'));
        children.each((elementFinder: ElementFinder, index) => {
            console.info('each[' + index + ']');
            elementFinder.getTagName().then((tag) => {
                console.info('tag:' + tag);
            });
        });
        children.count().then((count) => {
            buf.push(',count:' + count);
            console.info(',count:' + count);
        });


    }
    private thisNotWork(buf: any[], ef: ElementFinder): void {
        ef.getText().then((text) => {
            buf.push(',text:' + text);
        }, (error: any) => {
            buf.push(',getText()[Error]:' + error);
        });

        ef.getTagName().then((tag) => {
            buf.push('<' + tag + '>');
            ef.getText().then((text) => {
                buf.push(text);
            });
            buf.push('</' + tag + '>');
        }, (error: any) => {
            buf.push(',getTagName()[Error]:' + error);
        });
    }
}
