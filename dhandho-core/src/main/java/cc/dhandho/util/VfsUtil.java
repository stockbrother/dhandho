package cc.dhandho.util;

import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileType;

import com.age5k.jcps.JcpsException;

public class VfsUtil {

	public static FileObject resolveFile(FileObject parent, String path) {
		try {
			return parent.resolveFile(path);
		} catch (FileSystemException e) {
			throw JcpsException.toRtException(e);
		}
	}

	public static FileObject getTheLastModifiedChild(FileObject dir) {
		try {
			FileObject[] childA = dir.getChildren();
			if (childA.length == 0) {
				return null;
			}
			Arrays.sort(childA, new Comparator<FileObject>() {

				@Override
				public int compare(FileObject o1, FileObject o2) {
					try {
						return (int) (o1.getContent().getLastModifiedTime() - o2.getContent().getLastModifiedTime());
					} catch (FileSystemException e) {
						throw JcpsException.toRtException(e);
					}
				}
			});

			return childA[childA.length - 1];
		} catch (FileSystemException e) {
			throw JcpsException.toRtException(e);
		}
	}

	public static long getLastModifiedTime(FileObject to, boolean recusive) {

		//
		try {
			long rt = to.getContent().getLastModifiedTime();
			if (to.getType().equals(FileType.FOLDER)) {
				for (FileObject child : to.getChildren()) {
					long rtI = child.getContent().getLastModifiedTime();
					if (rtI > rt) {
						rt = rtI;
					}
				}
			}
			return rt;

		} catch (FileSystemException e) {
			throw JcpsException.toRtException(e);
		}
	}

	public static boolean containsAnyFile(FileObject to) {
		try {
			if (to.getType().equals(FileType.FILE)) {
				return true;
			}

			if (to.getType().equals(FileType.FOLDER)) {
				for (FileObject child : to.getChildren()) {
					if (containsAnyFile(child)) {
						return true;
					}
				}
			}
			return false;

		} catch (FileSystemException e) {
			throw JcpsException.toRtException(e);
		}
	}
}