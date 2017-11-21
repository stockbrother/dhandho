package tmp1;

import tmp1.client.Tmp1Test;
import com.google.gwt.junit.tools.GWTTestSuite;
import junit.framework.Test;
import junit.framework.TestSuite;

public class Tmp1Suite extends GWTTestSuite {
	public static Test suite() {
		TestSuite suite = new TestSuite("Tests for DhandhoEntryPoint");
		suite.addTestSuite(Tmp1Test.class);
		return suite;
	}
}
