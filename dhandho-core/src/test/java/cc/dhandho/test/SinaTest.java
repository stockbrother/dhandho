package cc.dhandho.test;

import java.io.File;

import org.apache.commons.vfs2.FileObject;

import cc.dhandho.sina.SinaAllQuotesPreprocessor;
import cc.dhandho.sina.SinaQuotesCollector;
import junit.framework.TestCase;

public class SinaTest extends TestCase {

	public void test() throws Exception {

		FileObject data = TestUtil.newTempFolder();

		FileObject sinaData = data.resolveFile("sina");

		FileObject outputParentDir = data.resolveFile("raw");

		new SinaQuotesCollector()//
				.pauseInterval(2000)//
				.outputParentDir(outputParentDir)//
				.maxLoops(2)//
				.start();

		FileObject to = data.resolveFile("sina" + File.separator + "washed");

		new SinaAllQuotesPreprocessor(outputParentDir, to).process();

	}
}
