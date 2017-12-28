package cc.dhandho.importer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import org.apache.commons.vfs2.FileObject;

public abstract class FileReaderIterator extends FilesIterator {

	public FileReaderIterator(FileObject dir) {
		super(dir);
	}

	@Override
	protected void onFile(String type, FileObject file, int number) throws IOException {
	
		InputStream is = file.getContent().getInputStream();
		Charset cs = Charset.forName("UTF-8");
		Reader reader = new InputStreamReader(is, cs);
		this.onReader(type, file, reader, number);
		
	}

	protected abstract void onReader(String type, FileObject file, Reader reader, int number) throws IOException;
}
