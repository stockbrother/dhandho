package cc.dhandho.importer;

import java.util.Date;

import org.apache.commons.vfs2.FileObject;

import cc.dhandho.Quarter;

public abstract class QuarterWahsedFileLoader extends WashedFileLoader {

	private Quarter quarter;

	public QuarterWahsedFileLoader(FileObject dir, Quarter quarter) {
		super(dir);
		this.quarter = quarter;
	}

	@Override
	protected boolean isIgnoreReportDate(Date date) {
		return this.quarter != null && !this.quarter.isEndDateOfQuarter(date);
	}

}
