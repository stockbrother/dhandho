package cc.dhandho.test.mycorp;

import java.util.List;

import com.age5k.jcps.framework.container.Container;

import cc.dhandho.mycorp.MyCorps;
import cc.dhandho.test.util.TestUtil;
import junit.framework.TestCase;

public class MyCorpsTest extends TestCase {

	public void test() {

		Container c = TestUtil.newContainerWithDefaultTestHome();

		MyCorps mcs = c.addComponent(MyCorps.class, MyCorps.class);
		{
			mcs.load();
			List<String> idL = mcs.getCorpIdList();
			TestCase.assertEquals(6, idL.size());
			TestCase.assertEquals(true, idL.contains("000001"));
			TestCase.assertEquals(true, idL.contains("000002"));
			TestCase.assertEquals(true, idL.contains("000004"));
			TestCase.assertEquals(true, idL.contains("000005"));
			TestCase.assertEquals(true, idL.contains("000006"));
			TestCase.assertEquals(true, idL.contains("000100"));

			TestCase.assertEquals(false, mcs.add("000001"));
			TestCase.assertEquals(true, mcs.add("000101"));
			TestCase.assertEquals(true, mcs.add("000201"));
			mcs.save();
		}
		{

			mcs.load();
			List<String> idL = mcs.getCorpIdList();
			TestCase.assertEquals(6 + 2, idL.size());
			TestCase.assertEquals(true, idL.contains("000101"));
			TestCase.assertEquals(true, idL.contains("000201"));

		}
	}
}
