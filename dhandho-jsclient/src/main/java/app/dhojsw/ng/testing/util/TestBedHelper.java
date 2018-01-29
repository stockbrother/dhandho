package app.dhojsw.ng.testing.util;

import def.angular.core_testing.ComponentFixture;
import def.angular.core_testing.TestBed;
import def.angular.core_testing.TestModuleMetadata;

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
