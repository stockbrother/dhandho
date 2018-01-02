package cc.dhandho;

import java.io.IOException;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;

import com.age5k.jcps.JcpsException;

public class DhandhoHome {

	FileSystemManager fileSystem;

	FileObject homeFile;
	
	FileObject dataFile;

	public FileSystemManager getFileSystem() {
		return fileSystem;
	}

	public DhandhoHome(FileSystemManager fileSystem, String home) {
		
		try {
			this.fileSystem = fileSystem;
			this.homeFile = fileSystem.resolveFile(home);
		} catch (FileSystemException e) {
			throw JcpsException.toRtException(e);
		}

	}

	public FileObject getHomeFile() {
		return this.homeFile;
	}

	public FileObject getClientFile() throws IOException {
		return resolveFile(this.homeFile, "client");
	}

	public FileObject getInputFolder() throws IOException {
		return resolveFile(this.homeFile, "input");
	}

	public FileObject getInportWashedDataFolder() throws IOException {
		return resolveFile(this.getInputFolder(), "washed");
	}

	public FileObject getInputCorpsFolder() throws IOException {
		return resolveFile(this.getInputFolder(), "corps");
	}

	public FileObject resolveFile(String path) throws IOException {
		return resolveFile(this.homeFile, path);
	}

	public FileObject resolveFile(FileObject parent, String path) throws IOException {
		return parent.resolveFile(path);
	}

}
