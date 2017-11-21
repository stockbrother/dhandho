package cc.dhandho;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryAliasInfos extends AbstractAliasInfos {

	private Map<String, AtomicInteger> maxColumnIndexMap = new HashMap<>();

	@Override
	public Integer addColumnIndex(String reportType, String alias) {

		Map<String, Integer> aliasMap = this.reportAliasColumnMap.get(reportType);
		if (aliasMap == null) {
			aliasMap = new HashMap<>();
			this.reportAliasColumnMap.put(reportType, aliasMap);
		}

		if (aliasMap.containsKey(alias)) {
			throw new RtException("duplicated.");
		}

		AtomicInteger max = this.maxColumnIndexMap.get(reportType);
		if (max == null) {
			max = new AtomicInteger();
			this.maxColumnIndexMap.put(reportType, max);
		}
		Integer rt = max.incrementAndGet();
		aliasMap.put(alias, rt);

		List<String> aliasList = this.reportAliasListMap.get(reportType);
		if (aliasList == null) {
			aliasList = new ArrayList<>();
			this.reportAliasListMap.put(reportType, aliasList);
		}
		aliasList.add(alias);

		return rt;
	}
}
