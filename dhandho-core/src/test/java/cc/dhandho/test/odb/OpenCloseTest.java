package cc.dhandho.test.odb;

import java.io.IOException;

import org.apache.commons.vfs2.FileObject;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.ODatabaseType;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;

import cc.dhandho.test.util.TestUtil;
import junit.framework.TestCase;

public class OpenCloseTest extends TestCase {

	public void xtestMemoryOpenClose() throws IOException {
		String dbName = "db1";
		String user = "admin";
		String password = "admin";
		String url = "embedded:test";

		OrientDB odb = new OrientDB(url, OrientDBConfig.defaultConfig());

		try {
			odb.create(dbName, ODatabaseType.MEMORY);

			for (int i = 0; i < 10; i++) {
				ODatabaseSession dbs = odb.open(dbName, user, password);
				dbs.close();
			}
		} finally {
			odb.close();
		}

	}

	public void testPlocalOpenClose() throws IOException {
		String userDir = System.getProperty("user.dir");
		String userDir2 = "file:///" + userDir.replace("\\", "/") + "/";
		String folderPrefix = userDir2 + "target/odb/open_close_test_";
		FileObject fo = TestUtil.newAnyFolder(folderPrefix);

		String folder = fo.getURL().toString();
		folder = folder.substring(userDir2.length());
		String dbName = "db1";
		String user = "admin";
		String password = "admin";
		String url = "embedded:" + folder;

		OrientDB odb = new OrientDB(url, OrientDBConfig.defaultConfig());

		try {
			odb.create(dbName, ODatabaseType.PLOCAL);

			for (int i = 0; i < 10; i++) {
				ODatabaseSession dbs = odb.open(dbName, user, password);
				dbs.close();
			}
		} finally {
			odb.close();
		}

	}

}
