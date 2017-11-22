
import org.junit.Ignore;
import org.junit.Test;

import cc.dhandho.xueqiu.XueqiuDataWasher;

import java.io.File;
import java.nio.charset.Charset;

@Ignore
public class XueqiuDataTest {

    @Test
    public void washTest() {
        File source = new File("d:\\data\\xueqiu\\raw");
        File target = new File("d:\\data\\xueqiu\\washed");

        XueqiuDataWasher washer = new XueqiuDataWasher(source, Charset.forName("UTF-8"), target);
        //washer.setLimitOfFiles(1);
        washer.types("balsheet", "cfstatement", "incstatement");

        washer.execute();

    }
}
