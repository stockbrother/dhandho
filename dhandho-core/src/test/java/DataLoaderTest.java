import java.io.File;

import org.junit.Ignore;
import org.junit.Test;

import com.orientechnologies.orient.core.db.ODatabaseSession;

import cc.dhandho.AppContext;
import cc.dhandho.AppContextImpl;
import cc.dhandho.graphdb.DbConfig;
import cc.dhandho.util.DbInitUtil;

//http://orientdb.com/docs/3.0.x/fiveminute/java-3.html

@Ignore
public class DataLoaderTest {
	// static String host = "192.168.110.128";
	static String url = "remote:localhost";
	static String dbName = "dhandho";
	static String user = "admin";
	static String password = "admin";

	@Test
	public void test() throws InterruptedException {

		AppContext app = new AppContextImpl()
				.dbConfig(new DbConfig().dbUrl(url).dbName(dbName).dbUser(user).dbPassword(password)).create();

		// File dir = new File("D:\\data\\xueqiu\\washed\\balsheet\\");
		// File dir = new File("D:\\data\\xueqiu\\washed\\balsheet\\");
		File dir = new File("D:\\data\\xueqiu\\washed\\\\");
		
		DbInitUtil.initDb(app);
		
		//new GDBWashedFileSchemaLoader(app, dir, Quarter.Q4)/* .limit(10) */.start();

		//new GDBWashedFileValueLoader(app, dir, Quarter.Q4)/* .limit(10) */.start();
		app.destroy();
		System.out.println("done.");

	}


	private static void increaseDataVersion(ODatabaseSession db) {

	}

}
