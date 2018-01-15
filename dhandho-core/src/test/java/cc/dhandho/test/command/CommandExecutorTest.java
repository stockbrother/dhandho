package cc.dhandho.test.command;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.age5k.jcps.JcpsException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import cc.dhandho.rest.RestRequestContext;
import cc.dhandho.rest.RestRequestHandler;
import cc.dhandho.rest.command.CommandExecutor;
import cc.dhandho.rest.server.DhoServer;
import cc.dhandho.test.util.TestUtil;

public class CommandExecutorTest {

	@Test
	public void smokeTest() {

		DhoServer ds = TestUtil.mockDhoServerWithHandler(new RestRequestHandler() {

			@Override
			public void handle(RestRequestContext arg0) {
				try {
					arg0.getWriter().beginObject();
					arg0.getWriter().endObject();
				} catch (IOException e) {
					throw new JcpsException(e);
				}
			}
		});
		CommandExecutor exe = new CommandExecutor(ds);
		{

			JsonElement json = exe.execute("help");
			Assert.assertNotNull(json);
		}
		{
			JsonElement json = exe.execute("dupont -a -y 2016");
			Assert.assertTrue(json instanceof JsonObject);

			Assert.assertNotNull(json);
		}
	}
}
