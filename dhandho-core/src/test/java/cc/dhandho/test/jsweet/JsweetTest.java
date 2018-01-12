package cc.dhandho.test.jsweet;

import java.io.File;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.apache.commons.lang3.ArrayUtils;
import org.jsweet.transpiler.EcmaScriptComplianceLevel;
import org.jsweet.transpiler.JSweetFactory;
import org.jsweet.transpiler.JSweetTranspiler;
import org.jsweet.transpiler.ModuleKind;
import org.jsweet.transpiler.SourceFile;
import org.jsweet.transpiler.util.EvaluationResult;
import org.junit.Rule;
import org.junit.rules.TestName;

import junit.framework.TestCase;

public class JsweetTest extends TestCase {
	protected static final String TEST_DIRECTORY_NAME = "src/test/java";
	protected static final String TMPOUT_DIR = "tempOut";
	protected static JSweetTranspiler transpiler;
	@Rule
	public final TestName testNameRule = new TestName();

	@Override
	public void setUp() {
		if (transpiler == null) {
			transpiler = createTranspiler(new JSweetFactory());
		}
	}

	public void testTranspile() {
		transpile(logHandler -> {
			assertEquals("There should be 0 problems", 0, logHandler.reportedProblems.size());
		}, getSourceFile(new Class<?>[] { JsView.class, MyTestMain.class, }));
	}

	public void testEval() {
		System.out.println("testEval");
		eval((logHandler, r) -> {
			logHandler.assertNoProblems();
			System.out.println("show:"+(Object)r.get("show"));
			System.out.println("showX:"+(Object)r.get("showX"));
		}, getSourceFile(new Class<?>[] { JsView.class, MyTestMain.class, }));
	}

	protected SourceFile[] getSourceFile(Class<?>[] mainClasses) {

		SourceFile[] rt = new SourceFile[mainClasses.length];
		for (int i = 0; i < rt.length; i++) {
			rt[i] = new SourceFile(
					new File(TEST_DIRECTORY_NAME + "/" + mainClasses[i].getName().replace(".", "/") + ".java"));
		}

		return rt;
	}

	protected static JSweetTranspiler createTranspiler(JSweetFactory factory) {
		return createTranspiler(null, factory);
	}

	protected static JSweetTranspiler createTranspiler(File configurationFile, JSweetFactory factory) {
		JSweetTranspiler transpiler = new JSweetTranspiler(configurationFile, factory, null, new File(TMPOUT_DIR), null,
				new File(JSweetTranspiler.TMP_WORKING_DIR_NAME + "/candies/js"), System.getProperty("java.class.path"));
		transpiler.setEcmaTargetVersion(EcmaScriptComplianceLevel.ES5);
		transpiler.setEncoding("UTF-8");
		transpiler.setSkipTypeScriptChecks(true);
		transpiler.setIgnoreAssertions(false);
		transpiler.setGenerateSourceMaps(false);
		return transpiler;
	}

	protected void transpile(Consumer<TestTranspilationHandler> assertions, SourceFile... files) {
		transpile(new ModuleKind[] { ModuleKind.none, ModuleKind.commonjs }, assertions, files);
	}

	protected void transpile(ModuleKind[] moduleKinds, Consumer<TestTranspilationHandler> assertions,
			SourceFile... files) {
		for (ModuleKind moduleKind : moduleKinds) {
			transpile(moduleKind, assertions, files);
		}
	}

	protected final String getCurrentTestName() {
		return getClass().getSimpleName() + "." + testNameRule.getMethodName();
	}

	private void initOutputDir() {
		transpiler.setTsOutputDir(new File(new File(TMPOUT_DIR),
				getCurrentTestName() + "/" + transpiler.getModuleKind() + (transpiler.isBundle() ? "_bundle" : "")));
	}

	protected void transpile(ModuleKind moduleKind, Consumer<TestTranspilationHandler> assertions,
			SourceFile... files) {
		ModuleKind initialModuleKind = transpiler.getModuleKind();
		File initialOutputDir = transpiler.getTsOutputDir();
		try {
			// logger.info("*** module kind: " + moduleKind + (transpiler.isBundle() ? "
			// (with bundle)" : "") + " ***");
			TestTranspilationHandler logHandler = new TestTranspilationHandler();
			transpiler.setModuleKind(moduleKind);
			initOutputDir();
			transpiler.transpile(logHandler, files);
			if (assertions != null) {
				assertions.accept(logHandler);
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("exception occured while running test " + getCurrentTestName() + " with module kind " + moduleKind);
		} finally {
			transpiler.setModuleKind(initialModuleKind);
			transpiler.setTsOutputDir(initialOutputDir);
		}
		if (moduleKind == ModuleKind.none && !transpiler.isBundle() && files.length > 1) {
			ArrayUtils.reverse(files);
			transpiler.setBundle(true);
			try {
				transpile(moduleKind, assertions, files);
			} finally {
				transpiler.setBundle(false);
				ArrayUtils.reverse(files);
			}
		}
	}

	protected void eval(BiConsumer<TestTranspilationHandler, EvaluationResult> assertions, SourceFile... files) {
		eval(new ModuleKind[] { ModuleKind.none, ModuleKind.commonjs }, assertions, files);
	}

	protected void eval(ModuleKind[] moduleKinds, BiConsumer<TestTranspilationHandler, EvaluationResult> assertions,
			SourceFile... files) {
		for (ModuleKind moduleKind : moduleKinds) {
			eval(moduleKind, assertions, files);
		}
	}

	protected void eval(ModuleKind moduleKind, BiConsumer<TestTranspilationHandler, EvaluationResult> assertions,
			SourceFile... files) {
		eval(moduleKind, true, assertions, files);
	}

	protected void eval(ModuleKind moduleKind, boolean testBundle,
			BiConsumer<TestTranspilationHandler, EvaluationResult> assertions, SourceFile... files) {
		ModuleKind initialModuleKind = transpiler.getModuleKind();
		File initialOutputDir = transpiler.getTsOutputDir();
		try {
			// logger.info("*** module kind: " + moduleKind + (transpiler.isBundle() ? "
			// (with bundle)" : "") + " ***");
			TestTranspilationHandler logHandler = new TestTranspilationHandler();
			EvaluationResult res = null;
			transpiler.setModuleKind(moduleKind);

			// touch will force the transpilation even if the files were
			// already
			// transpiled
			SourceFile.touch(files);
			initOutputDir();
			res = transpiler.eval(logHandler, files);
			// logger.trace(getCurrentTestName() + " -- result=" + res.getExecutionTrace());
			if (assertions != null) {
				assertions.accept(logHandler, res);
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("exception occured while running test " + getCurrentTestName() + " with module kind " + moduleKind);
		} finally {
			transpiler.setModuleKind(initialModuleKind);
			transpiler.setTsOutputDir(initialOutputDir);
		}
		if (testBundle && moduleKind == ModuleKind.none && !transpiler.isBundle() && files.length > 1) {
			ArrayUtils.reverse(files);
			transpiler.setBundle(true);
			try {
				eval(moduleKind, assertions, files);
			} finally {
				transpiler.setBundle(false);
				ArrayUtils.reverse(files);
			}
		}

	}
}
