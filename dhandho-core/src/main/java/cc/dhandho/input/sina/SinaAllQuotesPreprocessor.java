package cc.dhandho.input.sina;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.Charset;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import au.com.bytecode.opencsv.CSVWriter;
import cc.dhandho.rest.server.AllQuotesLoader;
import cc.dhandho.util.JsonUtil;
/**
 * @see AllQuotesLoader
 * @author wu
 *
 */
public class SinaAllQuotesPreprocessor {

	private static Logger LOG = LoggerFactory.getLogger(SinaAllQuotesPreprocessor.class);

	private static String[] HEADERS = new String[] { "symbol", "code", "name", "trade", "pricechange", "changepercent",
			"buy", "sell", "settlement", "open", "high", "low", "volume", "amount", "ticktime", "per", "pb", "mktcap",
			"nmc", "turnoverratio" };
	private static Charset charSet = Charset.forName("gb2312");
	FileObject sourceDir;
	FileObject targetDir;

	public SinaAllQuotesPreprocessor(FileObject sourceDir, FileObject targetDir) {
		this.sourceDir = sourceDir;
		this.targetDir = targetDir;
	}

	public void process() {
		try {
			this.doProcess();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} //
	}

	private void doProcess() throws IOException {
		if (!this.targetDir.exists()) {
			this.targetDir.createFolder();
		}
		for (FileObject f : this.sourceDir.getChildren()) {
			if (f.getType().equals(FileType.FOLDER)) {
				this.processFileGroup(f);
			}
		}
	}

	private void processFileGroup(FileObject dir) throws IOException {
		String name = dir.getName().getBaseName();

		FileObject output = this.targetDir.resolveFile(name + ".quotes" + ".csv");
		if (output.exists()) {
			// ignore.
			LOG.warn("exists" + output.getURL());
			return;//
		}

		FileObject outputWork = this.targetDir.resolveFile(name + ".quotes" + ".csv.work");

		CSVWriter cw = new CSVWriter(
				new OutputStreamWriter(outputWork.getContent().getOutputStream(), Charset.forName("UTF-8")), ',',
				CSVWriter.NO_QUOTE_CHARACTER);
		cw.writeNext(new String[] { "Header", "" });
		//cw.writeNext(new String[] { "单位", "1" });
		cw.writeNext(new String[] { "日期格式", "yyyyMMddHHmmssSSS" });
		cw.writeNext(new String[] { "报告日期", name });
		cw.writeNext(new String[] { "Body", "" });
		cw.writeNext(HEADERS);
		int total = 0;
		for (FileObject f : dir.getChildren()) {
			String fname = f.getName().getBaseName();
			if (!fname.endsWith(".json")) {
				LOG.warn("ignore:" + f.getURL());
				// ignore
				continue;
			}
			Reader r = new InputStreamReader(f.getContent().getInputStream(), charSet);

			// Object jso = JSONValue.parse(r);
			JsonElement root = JsonUtil.parse(r);

			if (root == null) {
				LOG.info("found 'null',ignore:" + f.getURL());
			} else if (root.isJsonArray()) {
				JsonArray jsa = (JsonArray) root;

				LOG.info("process:" + f.getURL() + ",array length:" + jsa.size());
				for (int i = 0; i < jsa.size(); i++) {
					JsonObject jo = jsa.get(i).getAsJsonObject();
					String[] line = new String[HEADERS.length];
					for (int j = 0; j < HEADERS.length; j++) {
						String key = HEADERS[j];
						line[j] = String.valueOf(jo.get(key).getAsString());
					}
					cw.writeNext(line);
					total++;
				}
			} else {
				LOG.error("unkown json object:" + root);
			}

		}
		cw.close();
		if (total == 0) {
			outputWork.delete();
		} else {
			outputWork.moveTo(output);
		}
	}

}
