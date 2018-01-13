package cc.dhandho.test.jsweet.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jsweet.transpiler.SourceFile;

import com.age5k.jcps.framework.provider.Provider;

public class DefaultSourceFileProvider implements Provider<List<SourceFile>> {
	List<Class<?>> classes = new ArrayList<>();

	public DefaultSourceFileProvider() {

	}

	public DefaultSourceFileProvider add(Class<?>... classes) {
		for (Class<?> cls : classes) {
			this.classes.add(cls);
		}
		return this;
	}

	@Override
	public List<SourceFile> get() {
		List<SourceFile> rt = new ArrayList<>();
		File topFolder = new File("src/test/java");

		for (int i = 0; i < classes.size(); i++) {
			rt.add(new SourceFile(new File(topFolder, classes.get(i).getName().replace(".", "/") + ".java")));
		}
		return rt;
	}

}
