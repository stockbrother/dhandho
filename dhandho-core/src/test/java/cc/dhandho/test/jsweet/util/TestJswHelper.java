package cc.dhandho.test.jsweet.util;

import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.jsweet.transpiler.EcmaScriptComplianceLevel;
import org.jsweet.transpiler.JSweetFactory;
import org.jsweet.transpiler.JSweetTranspiler;
import org.jsweet.transpiler.ModuleKind;
import org.jsweet.transpiler.SourceFile;
import org.jsweet.transpiler.util.EvaluationResult;

import com.age5k.jcps.JcpsException;
import com.age5k.jcps.framework.provider.Provider;

import cc.dhandho.test.jsweet.JswTestUtil;
import cc.dhandho.test.jsweet.TestTranspilationHandler;

public class TestJswHelper {

	public static File tsOutputDir = new File("target/jsweet/test");

	private JSweetTranspiler transpiler;

	private Provider<List<SourceFile>> sourceFileProvider;

	private Consumer<TestTranspilationHandler> transpileConsumer;

	private BiConsumer<TestTranspilationHandler, EvaluationResult> evalConsumer;

	private List<ModuleKind> moduleKindList = new ArrayList<>();

	private String testName;

	public TestJswHelper() {
		transpiler = new JSweetTranspiler(null, new JSweetFactory(), null, tsOutputDir, null,
				new File(JSweetTranspiler.TMP_WORKING_DIR_NAME + "/candies/js"), System.getProperty("java.class.path"));
		transpiler.setEcmaTargetVersion(EcmaScriptComplianceLevel.ES5);
		transpiler.setEncoding("UTF-8");
		transpiler.setSkipTypeScriptChecks(true);
		transpiler.setIgnoreAssertions(false);
		transpiler.setGenerateSourceMaps(false);
		transpiler.getCandiesProcessor().touch();
	}

	public TestJswHelper consumer(Consumer<TestTranspilationHandler> consumer) {
		this.transpileConsumer = consumer;
		return this;
	}

	public TestJswHelper consumer(BiConsumer<TestTranspilationHandler, EvaluationResult> consumer) {
		this.evalConsumer = consumer;
		return this;
	}

	public TestJswHelper moduleKind(ModuleKind mk) {
		this.moduleKindList.add(mk);
		return this;
	}

	public TestJswHelper execute() {
		StackTraceElement[] ele = Thread.currentThread().getStackTrace();
		this.testName = ele[2].getClassName() + "." + ele[2].getMethodName();

		if (this.moduleKindList.isEmpty()) {
			this.moduleKindList.add(ModuleKind.none);
		}

		for (ModuleKind moduleKind : moduleKindList) {
			boolean transpileOrEvel = this.evalConsumer == null;

			SourceFile[] files = sourceFileProvider.get().toArray(new SourceFile[0]);

			this.doExecute(moduleKind, transpileOrEvel, files);
		}
		return this;
	}

	public TestJswHelper sourceFiles(Provider<List<SourceFile>> sfProvider) {
		this.sourceFileProvider = sfProvider;
		return this;
	}

	public TestJswHelper sourceFiles(Class<?>[] sourceClasses) {
		return this.sourceFiles(new DefaultSourceFileProvider().add(sourceClasses));
	}

	protected void doExecute(ModuleKind moduleKind, boolean transpileOrEval, SourceFile[] files) {
		if (files.length == 0) {
			throw new JcpsException("no source files to transpile.");
		}

		ModuleKind originalModuleKind = transpiler.getModuleKind();
		File originalTsOutputDir = transpiler.getTsOutputDir();
		try {
			// logger.info("*** module kind: " + moduleKind + (transpiler.isBundle() ? "
			// (with bundle)" : "") + " ***");
			transpiler.setModuleKind(moduleKind);
			setTsOutputDir(moduleKind);
			if (transpileOrEval) {
				TestTranspilationHandler logHandler = new TestTranspilationHandler();
				transpiler.transpile(logHandler, files);
				if (this.transpileConsumer != null) {
					this.transpileConsumer.accept(logHandler);
				}
			} else {
				TestTranspilationHandler logHandler = new TestTranspilationHandler();
				SourceFile.touch(files);
				EvaluationResult rst = transpiler.eval(logHandler, files);
				if (this.evalConsumer != null) {
					this.evalConsumer.accept(logHandler, rst);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			fail("exception occured while running test " + testName + " with module kind " + moduleKind);
		} finally {
			transpiler.setModuleKind(originalModuleKind);
			transpiler.setTsOutputDir(originalTsOutputDir);
		}

	}

	private void setTsOutputDir(ModuleKind moduleKind) {

		transpiler.setTsOutputDir(
				new File(tsOutputDir, testName + "/" + moduleKind + (transpiler.isBundle() ? "_bundle" : "")));
	}

	public TestJswHelper addJsLib(File file) {
		//
		if (!file.exists()) {
			throw new JcpsException("file not exists:" + file.getAbsolutePath());
		}
		transpiler.addJsLibFiles(file);
		return this;
	}

}
