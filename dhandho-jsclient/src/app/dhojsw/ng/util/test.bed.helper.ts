/* Generated from Java with JSweet 2.0.1 - http://www.jsweet.org */
import core_testing = require('@angular/core/testing');

import ComponentFixture = core_testing.ComponentFixture;
import TestBed = core_testing.TestBed;
/**
 * Usage by example:<br>
 * <code>
 *
 * TestBedHelper testBed = new TestBedHelper()
 * .imports(FormsModule.class, HttpClientTestingModule.class)
 * .declarations(CommandComponent.class)
 * .providers(Logger.class)
 * .compileComponents();
 * HttpTestingController httpMock = testBed.get(HttpTestingController.class);
 * ComponentFixture<CommandComponent> fixture = testBed.createComponent(CommandComponent.class);
 *
 * </code>
 *
 * @author Wu
 * @class
 */
export class TestBedHelper {
    meta: any = {};

    public imports(...cls: any[]): TestBedHelper {
        this.meta.imports = cls;
        return this;
    }

    public declarations(...cls: any[]): TestBedHelper {
        this.meta.declarations = cls;
        return this;
    }

    public providers(...cls: any[]): TestBedHelper {
        this.meta.providers = cls;
        return this;
    }

    public compileComponents(): TestBedHelper {
        TestBed.configureTestingModule(this.meta);
        TestBed.compileComponents();
        return this;
    }

    public get<T>(class1: any): T {
        return <any>(TestBed.get(class1));
    }

    public createComponent<T>(class1: any): ComponentFixture<T> {
        return TestBed.createComponent(class1);
    }
}
