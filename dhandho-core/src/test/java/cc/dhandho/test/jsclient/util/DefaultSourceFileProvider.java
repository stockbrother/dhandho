package cc.dhandho.test.jsclient.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
		File topFolderTest = new File("src/test/java");
		File topFolderMain = new File("src/main/java");

		for (int i = 0; i < classes.size(); i++) {
			Class<?> cls = classes.get(i);
			File topFolder;
			if (cls.getName().startsWith("cc.dhandho.test")) {
				topFolder = topFolderTest;
			} else {
				topFolder = topFolderMain;
			}
			rt.add(new SourceFile(new File(topFolder, classes.get(i).getName().replace(".", "/") + ".java")));
		}
		return rt;
	}

	private void resolveDependentClass(Class<?> clazz, Set<Class<?>> set) {

	}

}
