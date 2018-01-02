package cc.dhandho.jetty;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import com.age5k.jcps.JcpsException;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Deprecated //use servlet and war 
public class WebServer {

    private static final Logger LOG = LoggerFactory.getLogger(WebServer.class);

    Server server;

    public void start() {

        server = new Server(8080);
        ContextHandler context0 = new ContextHandler();
        context0.setContextPath("/msgs/");

        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[] { context0 });
        Gson gson = new GsonBuilder().create();
        context0.setHandler(new MessagesHandler(gson));

        server.setHandler(contexts);
        try {
            server.start();
        } catch (Exception e) {
            throw JcpsException.toRtException(e);
        }

    }

    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            throw JcpsException.toRtException(e);
        }
    }

    private static class MessagesHandler extends AbstractHandler {
        Gson gson;

        public MessagesHandler(Gson gson) {
            this.gson = gson;
        }

        @Override
        public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {

            request.setHandled(true);
            

        }
    }
}
