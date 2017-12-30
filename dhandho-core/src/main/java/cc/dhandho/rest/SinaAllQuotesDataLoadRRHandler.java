package cc.dhandho.rest;

import java.io.IOException;

import org.apache.commons.vfs2.FileObject;

import cc.dhandho.AllQuotesInfos;
import cc.dhandho.commons.container.Container;
import cc.dhandho.importer.MemoryAllQuotesWashedDataLoader;
import cc.dhandho.sina.SinaAllQuotesPreprocessor;
import cc.dhandho.sina.SinaQuotesCollector;

public class SinaAllQuotesDataLoadRRHandler extends AbstractRestRequestHandler {

	AllQuotesInfos allQuotesInfos;

	@Override
	public void setContainer(Container app) {
		super.setContainer(app);
		this.allQuotesInfos = app.findComponent(AllQuotesInfos.class, true);

	}

	@Override
	public void execute(RestRequestContext rrc) throws IOException {

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

	}

}
