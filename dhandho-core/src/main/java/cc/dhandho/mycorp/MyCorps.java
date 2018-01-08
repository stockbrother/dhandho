package cc.dhandho.mycorp;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.vfs2.FileObject;

import com.age5k.jcps.framework.container.Container;
import com.age5k.jcps.framework.provider.Provider;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import cc.dhandho.DhoDataHome;
import cc.dhandho.util.CsvUtil;
import cc.dhandho.util.VfsUtil;

public class MyCorps implements Container.Aware {

	Provider<DhoDataHome> dataHomeProvider;

	private List<String> corpIds = new ArrayList<>();

	@Override
	public void setContainer(Container arg0) {
		this.dataHomeProvider = arg0.findComponentLater(DhoDataHome.class, true);
	}

	public void add(String corpId) {

	}

	public void write() {
		DhoDataHome home = this.dataHomeProvider.get();
		FileObject client = home.getClientFile();
		FileObject fo = VfsUtil.resolveFile(client, "my-corps.csv");

		CSVWriter w = new CSVWriter(VfsUtil.getWriter(fo, Charset.forName("utf8")));
		for (int i = 0; i < corpIds.size(); i++) {
			String corpId = corpIds.get(i);
			w.writeNext(new String[] { corpId });
		}
		CsvUtil.close(w);
	}

	public void load() {

		DhoDataHome home = this.dataHomeProvider.get();
		FileObject client = home.getClientFile();
		FileObject fo = VfsUtil.resolveFile(client, "my-corps.csv");
		CSVReader r = new CSVReader(VfsUtil.getReader(fo, Charset.forName("utf8")));
		
		corpIds.clear();

		while (true) {
			String[] line = CsvUtil.readNext(r);
			if (line == null) {
				break;
			}
			String corpId = line[0];
			corpIds.add(corpId);
		}
		CsvUtil.close(r);

	}
}
