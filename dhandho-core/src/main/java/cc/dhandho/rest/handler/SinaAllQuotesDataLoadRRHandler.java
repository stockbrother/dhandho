package cc.dhandho.rest.handler;

import java.io.IOException;

import org.apache.commons.vfs2.FileObject;

import com.age5k.jcps.JcpsException;
import com.age5k.jcps.framework.container.Container;

import cc.dhandho.DhoDataHome;
import cc.dhandho.input.sina.SinaAllQuotesPreprocessor;
import cc.dhandho.input.sina.SinaQuotesCollector;
import cc.dhandho.rest.AbstractRestRequestHandler;
import cc.dhandho.rest.RestRequestContext;
import cc.dhandho.rest.server.InputDataMainLoader;

public class SinaAllQuotesDataLoadRRHandler extends AbstractRestRequestHandler {

	protected DhoDataHome dataHome;
	
	@Override
	public void setContainer(Container app) {	
		super.setContainer(app);
		this.dataHome = app.findComponent(DhoDataHome.class, true);
	}

	@Override
	public void handleInternal(RestRequestContext rrc) {
		try {

			FileObject sina = dataHome.getInputFolder().resolveFile("sina");

			FileObject raw = sina.resolveFile("raw");

			new SinaQuotesCollector()//
					.pauseInterval(2000)//
					.outputParentDir(raw)//
					// .maxLoops(2)//
					.start();

			FileObject to = sina.resolveFile("washed");

			new SinaAllQuotesPreprocessor(raw, to).process();
			// TODO only load sina,not all loaded.
			InputDataMainLoader l = app.findComponent(InputDataMainLoader.class, true);
			l.load();
		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		}
	}

}
