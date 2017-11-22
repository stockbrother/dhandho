import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

import org.junit.Ignore;
import org.junit.Test;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import cc.dhandho.AppContext;
import cc.dhandho.AppContextImpl;
import cc.dhandho.graphdb.DbConfig;

//http://orientdb.com/docs/3.0.x/fiveminute/java-3.html

@Ignore
public class MyMain {
	static String url = "remote:192.168.110.128";
	static String dbName = "test";
	static String user = "admin";
	static String password = "admin";

	@Test
	public void test() throws InterruptedException {

		AppContext app = new AppContextImpl()
				.dbConfig(new DbConfig().dbUrl(url).dbName(dbName).dbUser(user).dbPassword(password)).create();
		Semaphore s = new Semaphore(0);
		ODatabaseSession ds = app.openDB();
		{
				long start1 = System.currentTimeMillis();
				// let's do something with this session!
				createSchema(ds);
				createPeople(ds);
				executeAQuery(ds);
				executeAnotherQuery(ds);
				long start2 = System.currentTimeMillis();
				System.out.println("take:" + (start2 - start1));
				// todo conditional close session.
				ds.close();//
				s.release();
			}
	
		s.acquire();
		app.destroy();
		System.out.println("done.");

	}

	public static void mainx(String[] args) {

		OrientDB orient = new OrientDB(url, OrientDBConfig.defaultConfig());
		ODatabaseSession db = orient.open(dbName, user, password);
		long start1 = System.currentTimeMillis();
		// let's do something with this session!
		createSchema(db);
		createPeople(db);
		executeAQuery(db);
		executeAnotherQuery(db);
		long start2 = System.currentTimeMillis();

		db.close();
		orient.close();
		System.out.println("done," + (start2 - start1));
	}

	private static void createSchema(ODatabaseSession db) {
		OClass person = db.getClass("Person");

		if (person == null) {
			person = db.createVertexClass("Person");
		}

		if (person.getProperty("name") == null) {
			person.createProperty("name", OType.STRING);
			person.createIndex("Person_name_index", OClass.INDEX_TYPE.NOTUNIQUE, "name");
		}

		if (db.getClass("FriendOf") == null) {
			db.createEdgeClass("FriendOf");
		}

	}

	private static void executeAQuery(ODatabaseSession db) {
		String query = "SELECT expand(out('FriendOf').out('FriendOf')) from Person where name = ?";
		OResultSet rs = db.query(query, "Alice");

		while (rs.hasNext()) {
			OResult item = rs.next();
			System.out.println("friend: " + item.getProperty("name"));
		}

		rs.close(); // REMEMBER TO ALWAYS CLOSE THE RESULT SET!!!
	}

	private static void executeAQueryS(ODatabaseSession db) {
		String query = "SELECT expand(out('FriendOf').out('FriendOf')) from Person where name = ?";
		OResultSet rs = db.query(query, "Alice");
		rs.stream().forEach(x -> System.out.println("friend: " + x.getProperty("name")));
		rs.close();
	}

	private static void executeAnotherQuery(ODatabaseSession db) {
		String query = " MATCH                                           "
				+ "   {class:Person, as:a, where: (name = :name1)}, "
				+ "   {class:Person, as:b, where: (name = :name2)}, "
				+ "   {as:a} -FriendOf-> {as:x} -FriendOf-> {as:b}  "
				+ " RETURN x.name as friend                         ";

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name1", "Alice");
		params.put("name2", "Jim");

		OResultSet rs = db.query(query, params);

		while (rs.hasNext()) {
			OResult item = rs.next();
			System.out.println("friend: " + item.getProperty("name"));
		}

		rs.close();
	}

	private static void createPeople(ODatabaseSession db) {
		OVertex alice = createPerson(db, "Alice", "Foo");
		OVertex bob = createPerson(db, "Bob", "Bar");
		OVertex jim = createPerson(db, "Jim", "Baz");

		OEdge edge1 = alice.addEdge(bob, "FriendOf");
		edge1.save();
		OEdge edge2 = bob.addEdge(jim, "FriendOf");
		edge2.save();
	}

	private static OVertex createPerson(ODatabaseSession db, String name, String surname) {
		OVertex result = db.newVertex("Person");
		result.setProperty("name", name);
		result.setProperty("surname", surname);
		result.save();
		return result;
	}
}
