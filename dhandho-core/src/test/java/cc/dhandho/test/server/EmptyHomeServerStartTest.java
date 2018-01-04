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
		// an empty data home should be filled with the default content from
		// res://cc/dhandho/home/data
		DhoServer server = TestUtil.newInMemoryTestDhandhoServer(home);
		server.start();
		{

			FileObject initF = home.getDataHomeFolder().resolveFile(".dho.init");
			TestCase.assertTrue(initF.exists());
		}

		{// input folder

			FileObject inputF = home.getInputFolder();
			TestCase.assertTrue(inputF.exists());
			{

				FileObject corpsF = home.getInputFolder().resolveFile("corps");
				TestCase.assertTrue(corpsF.exists());
				{
					TestCase.assertFalse("should be archived", corpsF.resolveFile("sse.corplist.csv").exists());
					TestCase.assertFalse("should be archived", corpsF.resolveFile("sse.corplist2.csv").exists());
					TestCase.assertFalse("should be archived", corpsF.resolveFile("szse.corplist.csv").exists());
				}
			}
			{

				FileObject fo = home.getInputFolder().resolveFile("washed");
				TestCase.assertTrue(fo.exists());
			}
		}
		{//

			FileObject clientF = home.getDataHomeFolder().resolveFile("client");
			TestCase.assertTrue(clientF.exists());
			{
				TestCase.assertTrue(clientF.resolveFile("metric-defines.xml").exists());
			}
		}
		{

			FileObject fo = home.getArchiveRootFolder();
			TestCase.assertTrue(fo.exists());
		}

		{

			FileObject[] childA = home.getDataHomeFolder().getChildren();
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
