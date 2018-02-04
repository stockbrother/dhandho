package cc.dhandho;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author wu
 *
 */
public class AllQuotesInfos {
	private static final Logger LOG = LoggerFactory.getLogger(AllQuotesInfos.class);

	private Map<String, Double> quotesMap = new HashMap<>();

	// last loaded timestamp.
	private long lastLoaded = -1;

	public long getLastLoaded() {
		return lastLoaded;
	}

	public void setLastLoaded(long lastLoaded) {
		this.lastLoaded = lastLoaded;
	}

	public void put(Date reportDate, String corpId, BigDecimal value) {

		this.quotesMap.put(corpId, value.doubleValue());
		for (int i = 0; i < 1; i++) {

		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("put,corpId:" + corpId + ",value:" + value);
		}
	}

	public Double getBuyPrice(String corpId) {
		return this.quotesMap.get(corpId);
	}

}
