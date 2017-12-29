package cc.dhandho;

import java.util.List;

public interface ReportMetaInfos {

	public List<String> getReportAliasList(int reportType);

	public Integer getColumnIndexByAlias(String reportType, String alias);

	public List<Integer> getColumnIndexByAliasList(final String reportType, List<String> aliasList);

	public Integer addColumnIndex(String reportType, String alias) ;
	
	public String getColumnNameByAlias(String reportType,String alias);

	public List<String> getReportTypeListByAlias(String alias);
	
	public String getFirstReportTypeByAlias(String alias);
	
}
