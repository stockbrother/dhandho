package def.angular.core_testing;

import jsweet.lang.ObjectType;
import jsweet.lang.Optional;

/**
 * @author wu
 *
 */
@ObjectType
public class TestModuleMetadata {
	@Optional
	public Class<?>[] imports;
	@Optional
	public Class<?>[] declarations;
	@Optional
	public Class<?>[] providers;
	
}
