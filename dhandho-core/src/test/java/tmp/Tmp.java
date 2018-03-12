package tmp;

import cc.dhandho.rest.server.DbProvider;
import cc.dhandho.test.util.TestUtil;

public class Tmp {
	public static class A {

	}

	public static void main(String[] args) {
	//	System.out.println(A.class.getName());
	//	System.out.println(A.class.getSimpleName());
		DbProvider dbp = TestUtil.newInMemoryTestDbProvider();
		dbp.createDbIfNotExist();
		System.out.println("done.");
	}
}
