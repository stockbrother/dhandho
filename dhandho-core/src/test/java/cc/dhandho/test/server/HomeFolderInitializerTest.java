package cc.dhandho.test.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.vfs2.FileObject;

import cc.dhandho.DhoDataHome;
import cc.dhandho.rest.server.HomeFolderInitializer;
import cc.dhandho.test.util.TestUtil;
import junit.framework.TestCase;

public class HomeFolderInitializerTest extends TestCase {

	public void test() throws IOException {
		DhoDataHome home = TestUtil.newEmptyRamHome();
		new HomeFolderInitializer().initHomeFolder(home);
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
					TestCase.assertTrue("", corpsF.resolveFile("sse.corplist.csv").exists());
					TestCase.assertTrue("", corpsF.resolveFile("sse.corplist2.csv").exists());
					TestCase.assertTrue("", corpsF.resolveFile("szse.corplist.csv").exists());
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

	}
}
