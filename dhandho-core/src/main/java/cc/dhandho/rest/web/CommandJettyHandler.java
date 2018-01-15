package cc.dhandho.rest.web;

import java.io.IOException;
import java.io.Reader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.google.gson.JsonElement;

import cc.dhandho.rest.command.CommandExecutor;
import cc.dhandho.util.FileUtil;
import cc.dhandho.util.JsonUtil;

public class CommandJettyHandler extends AbstractHandler {

	CommandExecutor commands;

	CommandJettyHandler(CommandExecutor commands) {
		this.commands = commands;
	}

	@Override
	public void handle(String target, Request request, HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
		Reader reader = req.getReader();
		String line = FileUtil.readAsString(reader);
		//if line.length == 0 ,check the url is '/cmd/' or '/cmd'. the later will lead to a redirect from post to get. 
		JsonElement json = commands.execute(line);
		JsonUtil.write(json, res.getWriter());
		request.setHandled(true);
	}
}
