package cc.dhandho.test;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.ODatabaseType;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.exception.OSchemaException;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import cc.dhandho.graphdb.DbUtil;
import cc.dhandho.graphdb.OResultSetHandler;
import junit.framework.TestCase;

public class OrientDbTest extends TestCase {

	public void setUp() {

	}

	public void tearDown() {

	}

	public void testTwoMemoryDB() {
		String dbName = "db1";
		String user = "admin";
		String password = "admin";
		String url = "memory:test";
		String class1 = "class1";
		{

			OrientDB odb = new OrientDB(url, OrientDBConfig.defaultConfig());
			odb.create(dbName, ODatabaseType.MEMORY);
			{
				ODatabaseSession dbs = odb.open(dbName, user, password);
				OClass oclazz1 = dbs.createVertexClass(class1);
				dbs.close();
			}
			{
				OSchemaException ex = null;
				ODatabaseSession dbs = odb.open(dbName, user, password);
				try {
					OClass oclazz1 = dbs.createVertexClass(class1);
				} catch (OSchemaException e) {
					ex = e;
				}
				TestCase.assertNotNull("", ex);

				dbs.close();
			}
			odb.close();
		}
		
		{

			OrientDB odb = new OrientDB(url, OrientDBConfig.defaultConfig());
			odb.create(dbName, ODatabaseType.MEMORY);
			{
				ODatabaseSession dbs = odb.open(dbName, user, password);
				OClass oclazz1 = dbs.createVertexClass(class1);
				dbs.close();
			}
			odb.close();
		}

	}

	public void testOpenClose() throws IOException {
		String dbName = "db1";
		String user = "admin";
		String password = "admin";
		String url = "memory:test";

		OrientDB odb = new OrientDB(url, OrientDBConfig.defaultConfig());
		odb.create(dbName, ODatabaseType.MEMORY);

		for (int i = 0; i < 10; i++) {
			ODatabaseSession dbs = odb.open(dbName, user, password);
			dbs.close();
		}

	}

	public void testNoTx() throws IOException {
		String dbName = "db1";
		String user = "admin";
		String password = "admin";
		String url = "memory:test";
		String class1 = "class1";
		String class2 = "class2";

		OrientDB odb = new OrientDB(url, OrientDBConfig.defaultConfig());
		odb.create(dbName, ODatabaseType.MEMORY);

		{
			ODatabaseSession dbs = odb.open(dbName, user, password);
			OClass oclazz1 = dbs.createVertexClass(class1);
			OClass oclass2 = dbs.createEdgeClass(class2);
			OVertex v1 = dbs.newVertex(oclazz1);
			v1.setProperty("string", "StringValue1");
			v1.setProperty("integer", 1);
			OVertex v2 = dbs.newVertex(oclazz1);
			v2.setProperty("string", "StringValue2");
			v2.setProperty("integer", 2);

			OEdge e12 = dbs.newEdge(v1, v2, class2);
			v1.save();
			v2.save();
			e12.save();

			dbs.close();
		}
		{
			ODatabaseSession dbs = odb.open(dbName, user, password);
			String sql = "select from " + class1 + " where string=? and integer=?";
			DbUtil.executeQuery(dbs, sql, new Object[] { "StringValue1", 1 }, new OResultSetHandler<OVertex>() {

				@Override
				public OVertex handle(OResultSet rst) {
					Assert.assertTrue("no vertext found for:" + class1, rst.hasNext());
					OVertex row1 = rst.next().getVertex().get();
					Assert.assertNotNull(row1);
					Assert.assertFalse("too many vertext found for:" + class1, rst.hasNext());
					return null;
				}
			});

			dbs.close();
		}
		odb.close();

	}

	@Test
	public void testDefaultTx() throws IOException {
		String dbName = "db1";
		String user = "admin";
		String password = "admin";
		String url = "memory:test";
		String class1 = "class1";
		String class2 = "class2";

		OrientDB odb = new OrientDB(url, OrientDBConfig.defaultConfig());
		odb.create(dbName, ODatabaseType.MEMORY);

		{
			ODatabaseSession dbs = odb.open(dbName, user, password);

			OClass oclazz1 = dbs.createVertexClass(class1);
			OClass oclass2 = dbs.createEdgeClass(class2);

			dbs.begin();
			OVertex v1 = dbs.newVertex(oclazz1);
			OVertex v2 = dbs.newVertex(oclazz1);
			OEdge e12 = dbs.newEdge(v1, v2, class2);
			v1.save();
			v2.save();
			e12.save();
			dbs.commit();

			OResultSet rs = dbs.query("select from " + class1 + " where 1=1");
			Assert.assertTrue("no vertext found for:" + class1, rs.hasNext());
			rs.close();
			dbs.close();
		}
		{
			ODatabaseSession dbs = odb.open(dbName, user, password);
			OResultSet rs = dbs.query("select from " + class1 + " where 1=1");
			Assert.assertTrue("no vertext found for:" + class1, rs.hasNext());

			OVertex row = rs.next().getVertex().get();
			Assert.assertNotNull(row);
			rs.close();
			dbs.close();
		}
		odb.close();

	}
}
