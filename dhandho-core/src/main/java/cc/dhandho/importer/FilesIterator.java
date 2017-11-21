package cc.dhandho.importer;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.dhandho.RtException;

public abstract class FilesIterator {

	private static final Logger LOG = LoggerFactory.getLogger(WashedFileLoader.class);

	protected boolean typeToUpperCase;
	protected boolean interrupted;

	private File dir;

	private int nextNumber;

	public FilesIterator(File dir) {
		this.dir = dir;
	}

	public void start() {
		doLoadInternal(this.dir);
	}

	private void doLoadInternal(File dir) {
		if (!dir.exists()) {
			throw new RtException("dir:" + dir.getAbsolutePath() + " do not exists.");
		}
		if (dir.isFile()) {
			File f = dir;
			String fname = f.getName();
			String[] fnames = fname.split("\\.");
			if (fnames[fnames.length - 1].equals("csv")) {

				String ftype = fnames[fnames.length - 2];
				if (this.typeToUpperCase) {
					ftype = ftype.toUpperCase();
				}
				onFile(ftype, f, this.nextNumber++);
			}

		} else {
			// is directory
			if (this.interrupted) {
				LOG.warn("interrupted.");
				return;
			}

			File[] fs = dir.listFiles();

			if (fs == null) {
				throw new RtException("??");
			}

			for (File f : fs) {
				// is directory
				if (this.interrupted) {
					LOG.warn("interrupted.");
					return;
				}
				this.doLoadInternal(f);
			}

		}
	}

	protected abstract void onFile(String type, File file, int number);
}
