package cc.dhandho.test.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.vfs2.FileObject;

import cc.dhandho.DhoDataHome;
import cc.dhandho.rest.server.DhoServer;
import cc.dhandho.test.util.TestUtil;
import junit.framework.TestCase;

public class EmptyHomeServerStartTest extends TestCase {

	public void testEmptyHome() throws IOException {
		DhoDataHome home = TestUtil.newEmptyRamHome();
		DhoServer server = TestUtil.newInMemoryTestDhandhoServer(home);
		server.start();
		{

			FileObject initF = home.getHomeFile().resolveFile(".dho.init");
			TestCase.assertTrue(initF.exists());
		}

		{// input folder

			FileObject inputF = home.getInputFolder();
			TestCase.assertTrue(inputF.exists());
			{

				FileObject fo = home.getInputFolder().resolveFile("corps");
				TestCase.assertTrue(fo.exists());
			}
			{

				FileObject fo = home.getInputFolder().resolveFile("washed");
				TestCase.assertTrue(fo.exists());
			}
		}
		{//

			FileObject fo = home.getHomeFile().resolveFile("client");
			TestCase.assertTrue(fo.exists());
		}
		{

			FileObject fo = home.getArchiveRootFolder();
			TestCase.assertTrue(fo.exists());
		}

		{

			FileObject[] childA = home.getHomeFile().getChildren();
			Map<String, FileObject> map = new HashMap<>();
			for (FileObject fo : childA) {
				map.put(fo.getName().getBaseName(), fo);
			}
			TestCase.assertEquals("", 4, map.size());
		}

		server.shutdown();
		//
		server.start();
		server.shutdown();

	}
}
