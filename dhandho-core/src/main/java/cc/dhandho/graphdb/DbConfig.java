package cc.dhandho.graphdb;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.ODatabaseType;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;

public class DbConfig {

    private String dbUrl;

    String dbName;

    String user;

    String password;

    public DbConfig dbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
        return this;
    }

    public DbConfig dbUser(String user) {
        this.user = user;
        return this;
    }

    public DbConfig dbPassword(String password) {
        this.password = password;
        return this;
    }

    public DbConfig dbName(String dbName) {
        this.dbName = dbName;
        return this;
    }

    public OrientDB createDb() {
    	return new OrientDB(dbUrl, OrientDBConfig.defaultConfig());
    }
    
    public ODatabaseSession openDb(OrientDB orient, boolean create) {
    	//orient.createIfNotExists(this.dbName, ODatabaseType.MEMORY);
    	return orient.open(dbName, user, password);
    }

}
