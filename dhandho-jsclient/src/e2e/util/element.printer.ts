import { protractor, browser, element, by, ElementArrayFinder, ElementFinder } from 'protractor';
import { Logs, promise } from 'selenium-webdriver';

import { e2e } from './e2e.util';

interface ParentContext {
    deep: number;
    childCount: number;
    tag: string;
    text: string;
}

class ElementPrinter {

    static maxDeep: number = 1000;

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
        if (deep > ElementPrinter.maxDeep) {
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
/**
 * print all element.
 *
 * @param root
 */
export function printAll(root?: ElementFinder) {
    if (!e2e.util.elementPrintAllEnabled) {
        return;
    }
    if (root == null) {
        let body = element(by.tagName('body'));
        expect(body).toBeTruthy('body not found');
        root = body;
    }
    new ElementPrinter(root).print();
}
