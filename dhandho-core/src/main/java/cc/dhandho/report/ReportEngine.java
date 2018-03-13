package cc.dhandho.report;

/**
 * 
 * @author wu
 *
 */
public interface ReportEngine {
	
	public Double getMetricValue(String corpId, int year, String metric);
	
	public Double[][] getMetricValue(String corpId, int[] years, String[] metrics);

	public CorpDatedMetricReportData getReport(String corpId, int[] years, String[] metrics);

	public CorpDupontProfileReport getDupontProfileReport(String corpId, int[] years);

	public String getCorpName(String corpId);
	
}
