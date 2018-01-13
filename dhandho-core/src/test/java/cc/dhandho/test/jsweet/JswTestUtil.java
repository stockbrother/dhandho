package cc.dhandho.test.jsweet;

import java.io.File;

import org.jsweet.transpiler.EcmaScriptComplianceLevel;
import org.jsweet.transpiler.JSweetFactory;
import org.jsweet.transpiler.JSweetTranspiler;
import org.jsweet.transpiler.SourceFile;

import cc.dhandho.test.jsweet.util.TestJswHelper;

public class JswTestUtil {
	public static final String TEST_DIRECTORY_NAME = "src/test/java";

	public static File tsOutputDir = new File("target/jsweet/test");

	private static JSweetTranspiler transpiler;

	private static TestJswHelper transpilerHelper;

	public static JSweetTranspiler getDefaultTranspiler() {
		if (transpiler == null) {

			transpiler = createTranspiler();
		}
		return transpiler;
	}

	protected static TestJswHelper newDefaultTestJswHelper(Class testClass) {

		return new TestJswHelper(testClass, getDefaultTranspiler());
	}

	protected static JSweetTranspiler createTranspiler() {
		return createTranspiler(new JSweetFactory());
	}

	protected static JSweetTranspiler createTranspiler(JSweetFactory factory) {
		return createTranspiler(null, factory);
	}

	protected static JSweetTranspiler createTranspiler(File configurationFile, JSweetFactory factory) {
		JSweetTranspiler transpiler = new JSweetTranspiler(configurationFile, factory, null, tsOutputDir, null,
				new File(JSweetTranspiler.TMP_WORKING_DIR_NAME + "/candies/js"), System.getProperty("java.class.path"));
		transpiler.setEcmaTargetVersion(EcmaScriptComplianceLevel.ES5);
		transpiler.setEncoding("UTF-8");
		transpiler.setSkipTypeScriptChecks(true);
		transpiler.setIgnoreAssertions(false);
		transpiler.setGenerateSourceMaps(false);
		transpiler.getCandiesProcessor().touch();
		return transpiler;
	}

	public static SourceFile[] getSourceFile(Class<?>[] mainClasses) {

		SourceFile[] rt = new SourceFile[mainClasses.length];
		for (int i = 0; i < rt.length; i++) {
			rt[i] = new SourceFile(
					new File(TEST_DIRECTORY_NAME + "/" + mainClasses[i].getName().replace(".", "/") + ".java"));
		}

		return rt;
	}

}
