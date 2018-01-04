package cc.dhandho;

import java.io.IOException;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileType;

import com.age5k.jcps.JcpsException;

import cc.dhandho.util.VfsUtil;

public class DhoDataHome {

	FileSystemManager fileSystem;

	FileObject homeFile;

	// FileObject dataFile;

	public DhoDataHome(FileSystemManager fileSystem, String home) {
		this.fileSystem = fileSystem;
		try {
			this.homeFile = fileSystem.resolveFile(home);
		} catch (FileSystemException e) {
			throw JcpsException.toRtException(e);
		}
	}

	public DhoDataHome(FileSystemManager fileSystem, FileObject homeFile) {

		this.fileSystem = fileSystem;
		this.homeFile = homeFile;

	}

	public FileSystemManager getFileSystem() {
		return fileSystem;
	}

	public FileObject getDataHomeFolder() {
		return this.homeFile;
	}

	public FileObject getClientFile() {
		return resolveFile(this.homeFile, "client");
	}

	public FileObject getInputFolder() {
		return resolveFile(this.homeFile, "input");
	}

	/**
	 * Operation Log Folder.
	 * 
	 * @return
	 * @throws IOException
	 */
	public FileObject getArchiveRootFolder() {
		return resolveFile(this.homeFile, "archive");
	}

	public FileObject newArchiveFolder() {
		FileObject af = this.getArchiveRootFolder();
		String prefix = "ar" + System.currentTimeMillis();
		try {

			for (int i = 0;; i++) {
				String name = prefix + "." + i;
				FileObject rt = resolveFile(af, name);
				if (rt.exists()) {
					continue;
				}
				rt.createFolder();
				return rt;
			}
		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		}

	}

	public FileObject getInportWashedDataFolder() {
		return resolveFile(this.getInputFolder(), "washed");
	}

	public FileObject getInputCorpsFolder() {
		return resolveFile(this.getInputFolder(), "corps");
	}

	public FileObject resolveFile(String path) {
		return resolveFile(this.homeFile, path);
	}

	public FileObject archiveIfNotEmpty(FileObject from, boolean remainTopFolder) {
		if (VfsUtil.containsAnyFile(from)) {
			return archive(from, remainTopFolder);
		}		
		return null;
	}

	public FileObject archive(FileObject from, boolean remainTopFolder) {
		try {
			boolean isFolder = from.getType().equals(FileType.FOLDER);
			FileObject af = this.newArchiveFolder();
			String name = from.getName().getBaseName();
			FileObject to = af.resolveFile(name);
			if (to.exists()) {
				throw new JcpsException("target file/folder exists:" + to.getURL());
			}
			from.moveTo(to);
			if (remainTopFolder && isFolder) {
				//
				from.createFolder();
			}
			return to;
		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		}

	}

	public FileObject resolveFile(FileObject parent, String path) {

		try {
			return parent.resolveFile(path);
		} catch (FileSystemException e) {
			throw JcpsException.toRtException(e);
		}

	}

}
