package cc.dhandho.test.jsweet.util;

import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.apache.commons.lang3.ArrayUtils;
import org.jsweet.transpiler.JSweetTranspiler;
import org.jsweet.transpiler.ModuleKind;
import org.jsweet.transpiler.SourceFile;
import org.jsweet.transpiler.util.EvaluationResult;
import org.junit.Rule;
import org.junit.rules.TestName;

import com.age5k.jcps.JcpsException;
import com.age5k.jcps.framework.provider.Provider;

import cc.dhandho.test.jsweet.JswTestUtil;
import cc.dhandho.test.jsweet.TestTranspilationHandler;

public class TestJswHelper {

	@Rule
	public final TestName testNameRule = new TestName();

	private JSweetTranspiler transpiler;

	private Provider<List<SourceFile>> sourceFileProvider;

	private Class testClass;

	private Consumer<TestTranspilationHandler> transpileConsumer;

	private BiConsumer<TestTranspilationHandler, EvaluationResult> evalConsumer;

	public TestJswHelper(Class testClass, JSweetTranspiler transpiler) {
		this.transpiler = transpiler;
		this.testClass = testClass;
	}

	public TestJswHelper consumer(Consumer<TestTranspilationHandler> consumer) {
		this.transpileConsumer = consumer;
		return this;
	}

	public TestJswHelper consumer(BiConsumer<TestTranspilationHandler, EvaluationResult> consumer) {
		this.evalConsumer = consumer;
		return this;
	}

	public TestJswHelper execute() {
		return execute(new ModuleKind[] { ModuleKind.none, ModuleKind.commonjs });
	}

	protected TestJswHelper execute(ModuleKind[] moduleKinds) {

		for (ModuleKind moduleKind : moduleKinds) {
			execute(moduleKind);
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

	protected void execute(ModuleKind moduleKind) {
		boolean transpileOrEvel = this.evalConsumer == null;
		doExecute(moduleKind, transpileOrEvel);
	}

	protected void doExecute(ModuleKind moduleKind, boolean transpileOrEval) {

		SourceFile[] files = sourceFileProvider.get().toArray(new SourceFile[0]);

		this.doExecute(moduleKind, transpileOrEval, files);
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
			fail("exception occured while running test " + getCurrentTestName() + " with module kind " + moduleKind);
		} finally {
			transpiler.setModuleKind(originalModuleKind);
			transpiler.setTsOutputDir(originalTsOutputDir);
		}

		if (moduleKind == ModuleKind.none && !transpiler.isBundle() && files.length > 1) {
			ArrayUtils.reverse(files);
			transpiler.setBundle(true);
			try {
				doExecute(moduleKind, transpileOrEval, files);
			} finally {
				transpiler.setBundle(false);
				ArrayUtils.reverse(files);
			}
		}
	}

	protected final String getCurrentTestName() {
		return testClass.getSimpleName() + "." + testNameRule.getMethodName();
	}

	private void setTsOutputDir(ModuleKind moduleKind) {

		transpiler.setTsOutputDir(new File(JswTestUtil.tsOutputDir,
				getCurrentTestName() + "/" + moduleKind + (transpiler.isBundle() ? "_bundle" : "")));
	}

}
