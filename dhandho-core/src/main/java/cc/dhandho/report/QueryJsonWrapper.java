package cc.dhandho.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import cc.dhandho.RtException;
import cc.dhandho.rest.JsonWrapper;
import cc.dhandho.util.DbInitUtil;
import cc.dhandho.xueqiu.DateUtil;

/**
 * 
 * @author wu
 *
 */
public class QueryJsonWrapper extends JsonWrapper {
	JsonObject obj;

	List<Date> dateList_cache;
	
	List<MetricJsonWrapper> metricList_cache;
	

	Map<Integer, String> metricIndex2NameMap;

	public QueryJsonWrapper(JsonObject obj) {
		this.obj = obj;
	}

	public Object[] getParameterArray() {

		List<Date> dL = this.getDateList();
		Object[] rt = new Object[dL.size() + 1];
		rt[0] = this.getCorpId();
		for (int i = 0; i < dL.size(); i++) {
			rt[i + 1] = dL.get(i);
		}
		return rt;
	}
	
	public int getWidth() {
		JsonElement json = obj.get("width");
		if(json == null) {
			return 600;
		}
		return json.getAsInt();
	}
	
	public int getHeight() {
		JsonElement json = obj.get("height");
		if(json == null) {
			return 400;
		}
		return json.getAsInt();
	}

	public String getCorpId() {
		return obj.get("corpId").getAsString();
	}

	public List<Date> getDateList() {
		if (this.dateList_cache != null) {
			return this.dateList_cache;
		}
		JsonArray array = obj.get("dates").getAsJsonArray();

		List<Date> rt = new ArrayList<>();
		for (int i = 0; i < array.size(); i++) {
			Date dI = null;
			JsonElement jI = array.get(i);

			if (jI.isJsonPrimitive()) {
				JsonPrimitive jP = jI.getAsJsonPrimitive();
				if (jP.isNumber()) {
					int year = jP.getAsInt();
					dI = DateUtil.newDateOfYearLastDay(year, TimeZone.getDefault());
				} else {
					throw new RtException("todo");
				}
			} else {
				throw new RtException("todo");
			}

			rt.add(dI);
		}
		this.dateList_cache = rt;
		return rt;
	}

	public List<MetricJsonWrapper> getMetricList() {
		if(metricList_cache != null) {
			return metricList_cache;
		}
		JsonArray metrics = obj.get("metrics").getAsJsonArray();

		metricList_cache = MetricJsonWrapper.valueListOf(null, metrics);
		return metricList_cache; 

		
	}

	public void doBuildSql(JsonMetricSqlLinkQueryBuilder builder) {

		builder.sql.append("select corpId,reportDate,");
		this.metricIndex2NameMap = new HashMap<>();
		List<MetricJsonWrapper> ml = this.getMetricList();

		for (int i = 0; i < ml.size(); i++) {
			MetricJsonWrapper m = ml.get(i);

			m.doBuildSql(null, builder);

			builder.sql.append(" as m" + (i + 1));
			String mName = m.resolveNameAsTitle();
			this.metricIndex2NameMap.put((i + 1), mName);

			if (i < ml.size() - 1) {
				builder.sql.append(",");
			}

		}

		builder.sql.append(" from ").append(DbInitUtil.V_MASTER_REPORT);

		builder.sql.append(" where corpId=? ");
		List<Date> dL = this.getDateList();
		if (dL.size() > 0) {

			builder.sql.append(" and (");

			for (int i = 0; i < dL.size(); i++) {
				builder.sql.append("reportDate=?");
				if (i < dL.size() - 1) {
					builder.sql.append(" or ");
				}
			}
			builder.sql.append(")");
		}
		builder.sql.append(" order by reportDate desc");

	}

	public String getMetricName(int idx) {
		return this.metricIndex2NameMap.get(idx);
	}
}