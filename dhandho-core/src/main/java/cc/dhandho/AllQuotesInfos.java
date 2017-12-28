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

	public void put(Date reportDate, String corpId, BigDecimal value) {

		this.quotesMap.put(corpId, value.doubleValue());
		LOG.info("put,corpId:" + corpId + ",value:" + value);

	}

}
