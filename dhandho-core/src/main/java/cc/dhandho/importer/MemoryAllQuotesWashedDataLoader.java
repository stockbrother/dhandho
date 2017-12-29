package cc.dhandho.importer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.apache.commons.vfs2.FileObject;

import cc.dhandho.AllQuotesInfos;

/**
 * Load all-quotes data into memory <code>
 * <code>
	日期格式,yyyyMMddHHmmssSSS
	报告日期,20171229122851582
	Body,
	symbol,code,name,trade,pricechange,changepercent,buy,sell,settlement,open,high,low,volume,amount,ticktime,per,pb,mktcap,nmc,turnoverratio
	sz300735,300735,N光弘,14.390,4.400,44.044,14.390,0.000,9.990,11.990,14.390,11.990,27287,389955,11:35:03,23.59,5.107,510384.52,127610.52,0.03077
 * </code>
 * 
 * </code>
 * 
 * @author wu
 *
 */
public class MemoryAllQuotesWashedDataLoader extends CsvReaderIterator {

	AllQuotesInfos allQuotesInfos;

	CsvHeaderRowMap headers;
	Map<String,Integer> bodyFirstRow;
	boolean body;
	Date reportDate;
	public AllQuotesInfos getAllQuotesInfos() {
		return allQuotesInfos;
	}

	public MemoryAllQuotesWashedDataLoader(FileObject dir, AllQuotesInfos allQuotesInfos) {
		super(dir);
		this.allQuotesInfos = allQuotesInfos;
	}

	@Override
	protected void onRow(CsvRow row) {

		if (headers == null && "Header".equals(row.getString(0, false))) {
			headers = new CsvHeaderRowMap();
			return;
		}

		if (!body && "Body".equals(row.getString(0, false))) {
			body = true;
			return;
		}

		if (!body) {
			// header area
			String key = row.getString(0, true);
			headers.put(key, row);
		} else {
			if(reportDate == null) {
				 reportDate = headers.getAsDate("报告日期");						 
			}
			
			if(this.bodyFirstRow == null) {
				this.bodyFirstRow = row.getAsIndexMap();
				return;
			}
			// body area			
			
			String corpId = row.getString(bodyFirstRow.get("code"), true);
			BigDecimal price = row.getAsBigDecimal(bodyFirstRow.get("buy"), true);
			allQuotesInfos.put(reportDate, corpId, price);
		}

	}

}
