import { protractor, browser, element, by, ElementArrayFinder, ElementFinder } from 'protractor';
import { Logs, promise } from 'selenium-webdriver';
import util = require('util');
import { LogsUtil } from './e2e.util';

let console = LogsUtil.console;
let maxDeep = 1000;

interface ParentContext {
    deep: number;
    childCount: number;
    tag: string;
    text: string;
}

export class ElementPrinter {

    root: ElementFinder;
    public constructor(root?: ElementFinder) {
        this.root = root;
        if (this.root === null) {
            // this.root = element.all();
        }
    }

    public print() {
        this.doPrint(null, this.root);
    }

    private doPrint(pc: ParentContext, ef: ElementFinder): void {
        let deep: number = (pc == null ? 0 : (pc.deep + 1));

        if (deep > maxDeep) {
            console.info('maxDeep exceeded');
            return;
        }

        let pro1: promise.Promise<any[]> = promise.map(
            promise.all([ef.getTagName(), ef.getText()]),
            (names) => {
                return names;
            });

        pro1.then((names) => {
            let pc2: ParentContext = {

                deep: deep + 1,
                childCount: 0,
                tag: names[0],
                text: names[1]
            };
            this.println(deep, '<' + pc2.tag + '>');
            let children: ElementArrayFinder = ef.all(by.xpath('./*'));

            children.each((child: ElementFinder, index) => {
                this.doPrint(pc2, child);
            });

            this.println(deep, '</' + pc2.tag + '>');
        });



    }
    private println(intend: number, msg: any) {
        let line: string[] = [];
        for (let i = 0; i < intend; i++) {
            line.push(' ');
        }
        line.push(msg);
        console.info(line.join(' '));
    }

}
