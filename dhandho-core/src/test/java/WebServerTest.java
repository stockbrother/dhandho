import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import cc.dhandho.rest.handler.EchoJsonHandler;

@Ignore
public class WebServerTest {

    private Logger LOG = LoggerFactory.getLogger(WebServerTest.class);

    protected static Gson GSON4LOG = new GsonBuilder().setPrettyPrinting().create();

    protected static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void test() throws IOException {

        //WebServer webServer = new WebServer();
        //webServer.start();

        HttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        HttpClient httpclient = HttpClientBuilder.create().setConnectionManager(cm).build();

        String uri = "http://localhost:8080/msgs/";

        HttpPost req = new HttpPost(uri);
        // req.addHeader(AjaxCometHttpRequestHandler.HK_ACTION, "message");
        // req.addHeader(AjaxCometHttpRequestHandler.HK_SESSION_ID,
        // this.sid);
        req.setHeader("Connection", "close");// NOTE,
        req.setHeader("Content-Type", "application/json; charset=UTF-8");


        JsonObject json = new JsonObject();
        json.addProperty("id", "id1");
        json.addProperty("timestamp", System.currentTimeMillis());
        json.addProperty("handler", EchoJsonHandler.class.getName());
        if (LOG.isTraceEnabled()) {//
            String msg = GSON4LOG.toJson(json);
            LOG.trace("sending http request,ajax json message array:" + msg);
        }

        StringEntity entity = new StringEntity(GSON.toJson(json));
        req.setEntity(entity);

        // execute is here
        final HttpResponse res = httpclient.execute(req);
        StatusLine sline = res.getStatusLine();
        LOG.info(sline.toString());
        {
            StringBuffer sb = new StringBuffer();
            Header[] headers = res.getAllHeaders();
            for (Header header : headers) {
                sb.append(header.toString()).append("\n");
            }
            LOG.info(sb.toString());
        }
        Reader reader = new InputStreamReader(res.getEntity().getContent());
        if (LOG.isInfoEnabled()) {
            StringBuffer sb = new StringBuffer();
            char[] buf = new char[1024];
            while (true) {
                int len = reader.read(buf);
                if (len == -1) {
                    break;
                }
                sb.append(buf, 0, len);
            }

            String msgS = sb.toString();
            LOG.info(msgS);
            reader = new StringReader(msgS);
        }
        if (sline.getStatusCode() == 200) {
            //Message msg2 = GSON.fromJson(reader, Message.class);
           // LOG.info(msg2.toString());

        } else if (sline.getStatusCode() == 302) {

        }

        //webServer.stop();

    }
}
