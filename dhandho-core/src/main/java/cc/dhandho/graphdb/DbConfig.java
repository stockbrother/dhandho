package cc.dhandho.graphdb;

import com.orientechnologies.orient.core.db.ODatabaseType;

public class DbConfig {

	private String dbUrl;

	String dbName;

	String user;

	String password;
	
	ODatabaseType dbType;

	public String getDbUrl() {
		return this.dbUrl;
	}

	public String getDbName() {
		return this.dbName;
	}

	public String getUser() {
		return this.user;
	}

	public String getPassword() {
		return this.password;
	}

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
	public DbConfig dbType(ODatabaseType dbType) {
		this.dbType = dbType;
		return this;
	}
	
	public ODatabaseType getDbType() {
		return this.dbType;
	}

}
