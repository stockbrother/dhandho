package def.angular.core_testing;

import jsweet.lang.Interface;

@Interface
public abstract class TestBed {
	public static native TestBed configureTestingModule(TestModuleMetadata meta);
	public static native TestBed compileComponents() ;
}
