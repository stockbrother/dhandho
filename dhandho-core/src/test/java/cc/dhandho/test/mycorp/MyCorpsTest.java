package cc.dhandho.test.mycorp;

import com.age5k.jcps.framework.container.Container;

import cc.dhandho.mycorp.MyCorps;
import cc.dhandho.test.util.TestUtil;
import junit.framework.TestCase;

public class MyCorpsTest extends TestCase {

	public void test() {
		
		Container c = TestUtil.newContainerWithDefaultTestHome();
		
		MyCorps mcs = c.addComponent(MyCorps.class,MyCorps.class);
		
	}
}
