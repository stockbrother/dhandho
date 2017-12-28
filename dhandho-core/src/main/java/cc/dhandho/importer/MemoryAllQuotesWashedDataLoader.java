package cc.dhandho.importer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.vfs2.FileObject;

import cc.dhandho.AllQuotesInfos;
import cc.dhandho.RtException;

/**
 * Load all-quotes data into memory
 * 
 * @author wu
 *
 */
public class MemoryAllQuotesWashedDataLoader extends WashedFileLoader {

	AllQuotesInfos allQuotesInfos;

	public AllQuotesInfos getAllQuotesInfos() {
		return allQuotesInfos;
	}

	public MemoryAllQuotesWashedDataLoader(FileObject dir, AllQuotesInfos allQuotesInfos) {
		super(dir);
		this.allQuotesInfos = allQuotesInfos;
	}

	@Override
	protected void onRowData(String reportType, String corpId, Date reportDate, List<String> keyList,
			List<BigDecimal> valueList) {
		if (valueList.size() != 1) {
			throw new RtException("invalid data");
		}

		allQuotesInfos.put(reportDate, corpId, valueList.get(0));

	}

	@Override
	protected boolean isIgnoreReportDate(Date date) {
		return false;
	}

}
