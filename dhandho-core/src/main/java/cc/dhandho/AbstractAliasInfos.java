package cc.dhandho;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAliasInfos implements AliasInfos {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractAliasInfos.class);

	protected Map<String, Map<String, Integer>> reportAliasColumnMap = new HashMap<>();

	protected Map<String, List<String>> reportAliasListMap = new HashMap<>();

	public static final String[] reportTypes = new String[] { "BALSHEET", "INCSTATEMENT", "CFSTATEMENT" };

	@Override
	public List<String> getReportAliasList(int reportType) {
		return reportAliasListMap.get(reportType);
	}

	@Override
	public Integer getColumnIndexByAlias(String reportType, String alias) {
		Map<String, Integer> aliasMap = reportAliasColumnMap.get(reportType);
		return aliasMap.get(alias);
	}

	@Override
	public String getColumnNameByAlias(String reportType, String alias) {
		Integer cI = this.getColumnIndexByAlias(reportType, alias);

		if (cI == null) {
			return null;
		}

		return "d_" + cI;
	}

	@Override
	public List<Integer> getColumnIndexByAliasList(final String reportType, List<String> aliasList) {
		Map<String, Integer> aliasMap = reportAliasColumnMap.get(reportType);
		List<Integer> rt = new ArrayList<>();

		for (final String alias : aliasList) {
			Integer i = aliasMap.get(alias);
			rt.add(i);
		}
		return rt;
	}

	@Override
	public String getFirstReportTypeByAlias(String alias) {
		List<String> list = getReportTypeListByAlias(alias);
		if (list.isEmpty()) {
			return null;
		} else if (list.size() == 1) {
			return list.get(0);
		} else {
			for (String type : reportTypes) {
				if (list.contains(type)) {
					return type;
				}
			}
			throw new RtException("too many report types:" + list + " and none of them are recognized.");
		}
	}

	@Override
	public List<String> getReportTypeListByAlias(String alias) {
		List<String> rt = new ArrayList<>();

		for (Map.Entry<String, Map<String, Integer>> en : this.reportAliasColumnMap.entrySet()) {
			if (en.getValue().containsKey(alias)) {
				rt.add(en.getKey());
			}
		}

		return rt;
	}
}
