package cc.dhandho.input.xueqiu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

/**
 * Convert original file format to the target format acceptable.
 * 
 * @author wu
 *
 */
public abstract class AbstractDataWasher implements Interruptable {
	private static final Logger LOG = LoggerFactory.getLogger(AbstractDataWasher.class);

	private File sourceDir;
	private File targetDir;

	Set<String> types = new HashSet<>();

	private boolean interrupted;
	Charset sourceCharSet;
	protected int processed;
	protected int limitOfFiles = Integer.MAX_VALUE;

	public AbstractDataWasher(File sourceDir, Charset sourceCharSet, File targetDir) {
		this.sourceDir = sourceDir;
		this.targetDir = targetDir;
		this.sourceCharSet = sourceCharSet;
	}

	public AbstractDataWasher types(String... types) {
		for (String type : types) {
			this.types.add(type);
		}
		return this;
	}

	public void execute() {
		try {
			this.process(this.sourceDir);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} //
	}

	private boolean process(File file) throws IOException {
		if (this.interrupted) {
			LOG.warn("interrupted.");
			return true;
		}
		if (file.isFile()) {
			String name = file.getName();
			if (!name.endsWith(".csv")) {
				// ignore
				if (LOG.isInfoEnabled()) {
					LOG.info("ignore file:" + file.getAbsolutePath());
				}
				return false;
			}
			String type = null;

			for (String typeI : types) {
				if (name.startsWith(typeI)) {
					type = typeI;
				}
			}

			if (type == null) {
				// ignore
				if (LOG.isInfoEnabled()) {
					LOG.info("ignore(for type reason) file:" + file.getAbsolutePath() + "");
				}
				return false;
			}
			String code = name.substring(type.length(), name.length() - ".csv".length());
			this.doProcess(file, type, code);
			if (this.processed >= limitOfFiles) {
				return true;
			}
			return false;
		}
		// isDirectory
		for (File f : file.listFiles()) {
			boolean stop = this.process(f);
			if (stop) {
				return true;
			}
		}

		return false;
	}

	public void setLimitOfFiles(int limitOfFiles) {
		this.limitOfFiles = limitOfFiles;
	}
	/**
	 * <code>
	    Header,		
		报告日期,2015-12-31,2014-12-31,2013-12-31,2012-12-31,2011-12-31,2010-12-31,2009-12-31,2008-12-31,
		日期格式,yyyy-MM-dd
		公司代码,300201		
		单位,10000
		备注,zcfzb
		Body,
		... ... 
		</code>
	 */

	private void doProcess(File file, String type, String code) throws IOException {

		File typeDir = new File(this.targetDir.getAbsolutePath(), type);
		File areaDir = new File(typeDir, code.substring(0, 4));

		File output = new File(areaDir, code + "." + type + ".csv");
		if (output.exists()) {
			LOG.info("skip of file for it's already exists:" + output.getAbsolutePath());
			return;
		}
		if (!areaDir.exists()) {
			areaDir.mkdirs();
		}
		LOG.info("generating output file:" + output.getAbsolutePath());

		Reader fr = new InputStreamReader(new FileInputStream(file), this.sourceCharSet);

		CSVReader r = new CSVReader(fr);

		CSVWriter w = new CSVWriter(new OutputStreamWriter(new FileOutputStream(output), Charset.forName("UTF-8")), ',',
				CSVWriter.NO_QUOTE_CHARACTER);

		this.process(file, type, code, r, w);

		w.close();
		this.processed++;

	}

	protected abstract void process(File file, String type, String code, CSVReader r, CSVWriter w) throws IOException;

	@Override
	public void interrupt() {
		this.interrupted = true;
	}

}
