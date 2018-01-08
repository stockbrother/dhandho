package cc.dhandho.mycorp;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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

	String[] header = new String[] { "公司代码" };

	@Override
	public void setContainer(Container arg0) {
		this.dataHomeProvider = arg0.findComponentLater(DhoDataHome.class, true);
	}

	public boolean add(String corpId) {
		return this.add(corpId, false);
	}

	public boolean add(String corpId, boolean write) {
		if (this.corpIds.contains(corpId)) {
			return false;
		}
		this.corpIds.add(corpId);
		if (write) {
			this.save();
		}
		return true;
	}

	public List<String> getCorpIdList() {
		return this.corpIds;
	}

	public Stream<String> corpIdStream() {
		return this.corpIds.stream();
	}

	public void save() {
		DhoDataHome home = this.dataHomeProvider.get();
		FileObject client = home.getClientFile();
		FileObject fo = VfsUtil.resolveFile(client, "my-corps.csv");

		CSVWriter w = new CSVWriter(VfsUtil.getWriter(fo, Charset.forName("utf8")));
		w.writeNext(this.header);
		for (int i = 0; i < corpIds.size(); i++) {
			String corpId = corpIds.get(i);
			w.writeNext(new String[] { corpId });
		}
		CsvUtil.close(w);
	}

	public MyCorps load() {

		DhoDataHome home = this.dataHomeProvider.get();
		FileObject client = home.getClientFile();
		FileObject fo = VfsUtil.resolveFile(client, "my-corps.csv");
		if (VfsUtil.exists(fo)) {

			CSVReader r = new CSVReader(VfsUtil.getReader(fo, Charset.forName("utf8")));

			corpIds.clear();
			int i = 0;
			while (true) {
				String[] line = CsvUtil.readNext(r);
				if (i == 0) {
					// ignore header.
					i++;
					continue;
				}
				if (line == null) {
					break;
				}
				String corpId = line[0];
				corpIds.add(corpId);
			}
			CsvUtil.close(r);
			
		}
		return this;
	}

	public boolean remove(String corpId, boolean save) {
		boolean rt = this.corpIds.remove(corpId);
		if (save) {
			this.save();
		}
		return rt;
	}
}
