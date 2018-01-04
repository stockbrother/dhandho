package cc.dhandho.rest.server;

import java.io.IOException;

import org.apache.commons.vfs2.AllFileSelector;
import org.apache.commons.vfs2.FileObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.age5k.jcps.JcpsException;

import cc.dhandho.Constants;
import cc.dhandho.DhoDataHome;

public class HomeFolderInitializer {
	private static final Logger LOG = LoggerFactory.getLogger(HomeFolderInitializer.class);

	public void initHomeFolder(DhoDataHome dataHome) {
		FileObject homeF = dataHome.getDataHomeFolder();
		try {
			FileObject initF = homeF.resolveFile(".dho.init");
			if (initF.exists()) {
				return;
			}

			FileObject[] children = homeF.getChildren();
			if (children.length > 0) {
				throw new JcpsException(
						"home folder is not empty, since it is not inited, it must be empty:" + homeF.getURL());
			}

			FileObject fromF = dataHome.getFileSystem().resolveFile(Constants.RES_DATA_HOME);
			LOG.info("test home:" + dataHome + " is ready.");

			homeF.copyFrom(fromF, new AllFileSelector());
			// check archive folder
			FileObject archiveF = dataHome.getArchiveRootFolder();
			if (!archiveF.exists()) {
				archiveF.createFolder();
			}
			// check washed folder.
			FileObject washedF = dataHome.getInportWashedDataFolder();
			if (!washedF.exists()) {
				washedF.createFolder();
			}
			initF.createFile();

		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		}

	}
}
