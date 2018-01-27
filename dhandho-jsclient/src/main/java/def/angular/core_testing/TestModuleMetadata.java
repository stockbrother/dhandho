package def.angular.core_testing;

import def.angular.core.SchemaMetadata;
import jsweet.lang.Interface;
import jsweet.lang.Optional;

@Interface
public class TestModuleMetadata {

	@Optional
	Class<?>[] providers;
	@Optional
	Class<?>[] declarations;
	@Optional
	Class<?>[] imports;
	@Optional
	SchemaMetadata[] schemas;
	// aotSummaries?: () => any[];
}
