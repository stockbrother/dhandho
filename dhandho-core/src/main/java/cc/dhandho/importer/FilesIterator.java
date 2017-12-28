package cc.dhandho.importer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.dhandho.RtException;

public abstract class FilesIterator {

	private static final Logger LOG = LoggerFactory.getLogger(WashedFileLoader.class);

	protected boolean typeToUpperCase;
	protected boolean interrupted;

	private FileObject dir;

	private int nextNumber;

	private boolean printFileContentWhenFailure = true;

	public FilesIterator(FileObject dir) {
		this.dir = dir;
	}

	public void start() throws IOException {
		doLoadInternal(this.dir);
	}

	private void doLoadInternal(FileObject dir) throws IOException {
		if (!dir.exists()) {
			throw new RtException("dir:" + dir.getURL() + " does not exists.");
		}

		if (dir.getType() == FileType.FILE) {
			FileObject f = dir;
			String fname = f.getName().getBaseName();
			String[] fnames = fname.split("\\.");
			if (fnames[fnames.length - 1].equals("csv")) {

				String ftype = fnames[fnames.length - 2];
				if (this.typeToUpperCase) {
					ftype = ftype.toUpperCase();
				}
				try {
					onFile(ftype, f, this.nextNumber++);
				} catch (Throwable t) {
					if (this.printFileContentWhenFailure) {
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						FileUtil.writeContent(f, baos);
						LOG.info(baos.toString("utf8"));

					}
					throw new RtException("exeception got when process file:" + f.getURL(), t);
				}

			}

		} else {
			// is directory
			if (this.interrupted) {
				LOG.warn("interrupted.");
				return;
			}

			FileObject[] fs = dir.getChildren();

			if (fs == null) {
				throw new RtException("??");
			}

			for (FileObject f : fs) {
				// is directory
				if (this.interrupted) {
					LOG.warn("interrupted.");
					return;
				}
				this.doLoadInternal(f);
			}

		}
	}

	protected abstract void onFile(String type, FileObject file, int number) throws IOException;
}
