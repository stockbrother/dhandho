package cc.dhandho.rest.handler;

import java.io.IOException;

import org.apache.commons.vfs2.FileObject;

import com.age5k.jcps.JcpsException;
import com.age5k.jcps.framework.container.Container;

import cc.dhandho.AllQuotesInfos;
import cc.dhandho.input.sina.SinaAllQuotesPreprocessor;
import cc.dhandho.input.sina.SinaQuotesCollector;
import cc.dhandho.input.washed.MemoryAllQuotesWashedDataLoader;
import cc.dhandho.rest.AbstractRestRequestHandler;
import cc.dhandho.rest.RestRequestContext;

public class SinaAllQuotesDataLoadRRHandler extends AbstractRestRequestHandler {

	AllQuotesInfos allQuotesInfos;

	@Override
	public void setContainer(Container app) {
		super.setContainer(app);
		this.allQuotesInfos = app.findComponent(AllQuotesInfos.class, true);

	}

	@Override
	public void handle(RestRequestContext rrc) {
		try {

			FileObject sina = home.resolveFile("sina");

			FileObject raw = sina.resolveFile("raw");

			new SinaQuotesCollector()//
					.pauseInterval(2000)//
					.outputParentDir(raw)//
					// .maxLoops(2)//
					.start();

			FileObject to = sina.resolveFile("washed");

			new SinaAllQuotesPreprocessor(raw, to).process();
			MemoryAllQuotesWashedDataLoader loader = new MemoryAllQuotesWashedDataLoader(to, this.allQuotesInfos);
			loader.start();
		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		}
	}

}
