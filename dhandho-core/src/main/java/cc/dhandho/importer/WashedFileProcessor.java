package cc.dhandho.importer;

import java.io.File;
import java.io.Reader;

public interface WashedFileProcessor {

	/**
	 *
	 * @param file
	 * @param reader
	 * @param loader
	 * @return interrupt
	 */
	public int process(File file, Reader reader, WashedCorpDataFileLoader loader);
}