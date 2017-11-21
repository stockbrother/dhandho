package cc.dhandho.importer;

import java.io.*;
import java.nio.charset.Charset;

public abstract class FileReaderIterator extends FilesIterator {

    public FileReaderIterator(File dir) {
        super(dir);
    }

    @Override
    protected void onFile(String type, File file, int number) {

        InputStream is;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Charset cs = Charset.forName("UTF-8");
        Reader reader = new InputStreamReader(is, cs);
        this.onReader(type, file, reader, number);
    }

    protected abstract void onReader(String type, File file, Reader reader, int number);
}
