package app.dhojsw.ng.testing.util;

import app.dhojsw.ng.command.CommandComponent;
import app.dhojsw.ng.service.Logger;
import def.angular.common_http_testing.HttpClientTestingModule;
import def.angular.common_http_testing.HttpTestingController;
import def.angular.core_testing.ComponentFixture;
import def.angular.core_testing.TestBed;
import def.angular.core_testing.TestModuleMetadata;
import def.angular.forms.FormsModule;

/**
 * Usage by example:<br>
 * <code>
 * 
 * TestBedHelper testBed = new TestBedHelper()
 *    	.imports(FormsModule.class, HttpClientTestingModule.class)
 *		.declarations(CommandComponent.class)
 *		.providers(Logger.class)
 *		.compileComponents();
 * HttpTestingController httpMock = testBed.get(HttpTestingController.class);
 * ComponentFixture<CommandComponent> fixture = testBed.createComponent(CommandComponent.class);
 * 
 * </code>
 * 
 * @author Wu
 *
 */
public class TestBedHelper {
	TestModuleMetadata meta = new TestModuleMetadata();

	public TestBedHelper imports(Class<?>... cls) {
		meta.imports = cls;
		return this;
	}

	public TestBedHelper declarations(Class<?>... cls) {
		meta.declarations = cls;
		return this;
	}

	public TestBedHelper providers(Class<?>... cls) {
		meta.providers = cls;
		return this;
	}

	public TestBedHelper compileComponents() {

		TestBed.configureTestingModule(meta);
		TestBed.compileComponents();
		return this;
	}

	public <T> T get(Class<T> class1) {
		//
		return TestBed.get(class1);
	}

	public <T> ComponentFixture<T> createComponent(Class<T> class1) {
		//
		return TestBed.createComponent(class1);
	}

}