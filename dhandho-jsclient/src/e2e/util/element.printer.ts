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

class ElementPrinter {

    root: ElementFinder;
    public constructor(root?: ElementFinder) {
        this.root = root;
        if (this.root === null) {
            // this.root = element.all();
        }
    }

    public print(): promise.Promise<any> {
        return this.doPrint(null, this.root);
    }

    private doPrint(pc: ParentContext, ef: ElementFinder): promise.Promise<any> {
        // this.log('doPrint, geting a promise');
        let deep: number = (pc == null ? 0 : (pc.deep + 1));
        if (deep > maxDeep) {
            return promise.rejected('maxDeep exceeded');
        }

        return promise.all([ef.getTagName(), ef.getText()]).then(
            (names) => {
                let pc2: ParentContext = {
                    deep: deep + 1,
                    childCount: 0,
                    tag: names[0],
                    text: names[1]
                };
                return pc2;
            }).then((pc2) => {
                this.println(deep, '<' + pc2.tag + '>');
                return pc2;
            }).then((pc2) => {

                let children: ElementArrayFinder = ef.all(by.xpath('./*'));

                return children.each((child: ElementFinder, index) => {
                    // this.log('each child:' + index);
                    return this.doPrint(pc2, child);
                }).then(() => {
                    return pc2;
                });
            }).then((pc2) => {
                this.println(deep, '</' + pc2.tag + '>');
            })
            ;

    }
    private log(msg: any) {
        this.println(0, msg);
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

export function printAll() {
    let body = element(by.tagName('body'));
    expect(body).toBeTruthy('body not found');
    new ElementPrinter(body).print();
}
