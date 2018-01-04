package cc.dhandho.test.server;

import java.io.IOException;

import org.apache.commons.vfs2.FileObject;
import org.junit.Test;

import cc.dhandho.DhoDataHome;
import cc.dhandho.commons.vfs.FilesIterator;
import cc.dhandho.rest.server.DhoServer;
import cc.dhandho.test.util.TestUtil;
import junit.framework.TestCase;

public class DhandhoServerTest extends TestCase {

	@Override
	public void setUp() {

	}

	@Override
	public void tearDown() {

	}

	@Test
	public void testServerStart() throws IOException {

		DhoServer ds = TestUtil.newInMemoryTestDhandhoServer();
		ds.start();
		DhoDataHome home = ds.getHome();
		{
			// check archive folder
			FileObject ar = home.getArchiveRootFolder();
			FileObject[] children = ar.getChildren();
			TestCase.assertEquals(2, children.length);
			// name

		}
		{// corps folder must be archived.
			FileObject fo = home.getInputFolder();

			FilesIterator fit = new FilesIterator(home.getInputCorpsFolder()) {

				@Override
				protected void onFile(String type, FileObject file, int number) throws IOException {

					TestCase.assertTrue("not archived? file found:" + file.getURL(), false);

				}
			};
			fit.start();
		}

		{// washed folder must be archived.
			FilesIterator fit = new FilesIterator(home.getInportWashedDataFolder()) {

				@Override
				protected void onFile(String type, FileObject file, int number) throws IOException {

					TestCase.assertTrue("not archived? file found:" + file.getURL(), false);

				}
			};

		}

		ds.shutdown();
	}
}
