package def.dhojsw.jsnative;

import def.angular.core_testing.TestModuleMetadata;

/**
 * TestModuleMetadata is ts type,not a class. Jsweet seems cannot translate it
 * correctly. new TestModuleMetadata() not legal in TS.<br>
 * And if it defined with '@Interface',the problem is that the Class<?>[] field
 * assignment is translated to string literal.
 * 
 * @author wu
 *
 */
public abstract class TestModuleMetadataBuilder {
	public native TestModuleMetadataBuilder imports(Class<?>... classes);

	public native TestModuleMetadataBuilder declarations(Class<?>... classes);

	public native TestModuleMetadataBuilder providers(Class<?>... classes);

	public native TestModuleMetadata build();
}
