package cc.dhandho;

import java.io.IOException;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;

public class DhandhoHome {

	FileSystemManager fileSystem;

	FileObject homeFile;
	
	FileObject dataFile;

	public DhandhoHome(FileSystemManager fileSystem, String home) {
		
		try {
			this.fileSystem = fileSystem;
			this.homeFile = fileSystem.resolveFile(home);
		} catch (FileSystemException e) {
			throw RtException.toRtException(e);
		}

	}

	public FileObject getHomeFile() {
		return this.homeFile;
	}

	public FileObject getClientFile() throws IOException {
		return resolveFile(this.homeFile, "client");
	}

	public FileObject getInportFile() throws IOException {
		return resolveFile(this.homeFile, "inport");
	}

	public FileObject getInportWashedDataFolder() throws IOException {
		return resolveFile(this.getInportFile(), "washed");
	}

	public FileObject getInportCorpsFile() throws IOException {
		return resolveFile(this.getInportFile(), "corps");
	}

	public FileObject resolveFile(String path) throws IOException {
		return resolveFile(this.homeFile, path);
	}

	public FileObject resolveFile(FileObject parent, String path) throws IOException {
		return parent.resolveFile(path);
	}

}
