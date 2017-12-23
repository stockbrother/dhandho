package cc.dhandho;

import java.io.IOException;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;

public class DhandhoHome {

	String home;

	FileSystemManager fileSystem;

	FileObject homeFile;

	public DhandhoHome(String home) {
		this.home = home;
		try {
			this.fileSystem = VFS.getManager();
			this.homeFile = fileSystem.resolveFile(home);
		} catch (FileSystemException e) {
			throw RtException.toRtException(e);
		}

	}

	public FileObject getHomeFile() {
		return this.homeFile;
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

	public FileObject resolveFile(FileObject parent, String path) throws IOException {
		return parent.resolveFile(path);
	}

}
