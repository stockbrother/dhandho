package cc.dhandho.report.query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;

import cc.dhandho.ReportMetaInfos;
import cc.dhandho.RtException;

/**
 * <code>
 * String jsonS = (""//
				+ "  [" //
				+ "    { 'name':'净资产收益率'," //
				+ "      'offset':0," //
				+ "      'operator':'*'," //
				+ "      'children':[" //
				+ "        { 'name':'总资产收益率'," //
				+ "          'offset':0," //
				+ "          'operator':'/'," //
				+ "          'components':[" //
				+ "            '净利润'," //
				+ "            '资产总计'," //
				+ "          ]" //
				+ "        }," //
				+ "        { 'name':'权益乘数'," //
				+ "          'offset':0," //
				+ "          'operator':'/'," //
				+ "          'components':[" //
				+ "          '资产总计'," //
				+ "          '所有者权益_或股东权益_合计'," //
				+ "          ]" //
				+ "        }" //
				+ "      ]" //
				+ "    }" //
				+ "  ]" //				
		).replaceAll("'", "\"");
	</code>
 *
 * Join is not supported by orientdb.
 * But link looks good.
 * @deprecated
 * @author wu
 *
 */
public class JsonMetricSqlJoinQueryBuilder {

	public static JsonMetricSqlJoinQueryBuilder newInstance(JsonReader reader, ReportMetaInfos aliasInfos) {

		JsonMetricSqlJoinQueryBuilder rt = new JsonMetricSqlJoinQueryBuilder();
		rt.reader = reader;
		rt.aliasInfos = aliasInfos;
		return rt;
	}

	private JsonReader reader;

	private ReportMetaInfos aliasInfos;

	private StringBuffer sql;

	private List<String> reportTypeList;

	private JsonMetricSqlJoinQueryBuilder() {

	}

	public JsonMetricSqlJoinQueryBuilder execute() throws IOException {

		this.sql = new StringBuffer();
		this.reportTypeList = new ArrayList<>();

		JsonElement json = Streams.parse(reader);
		JsonObject query = json.getAsJsonObject();
		Query q = new Query(query);
		q.doBuildSql(this);

		return this;
	}

	public String getSql() {
		return sql.toString();
	}

	/**
	 * 
	 * @author wu
	 *
	 */
	private static class Query {
		JsonObject obj;

		public Query(JsonObject obj) {
			this.obj = obj;
		}

		public List<Integer> getYearArray() {
			JsonArray array = obj.get("years").getAsJsonArray();

			List<Integer> rt = new ArrayList<>();
			for (int i = 0; i < array.size(); i++) {
				rt.add(array.get(i).getAsInt());
			}
			return rt;
		}

		public List<Metric> getMetricList() {
			JsonArray metrics = obj.get("metrics").getAsJsonArray();

			return Metric.valueListOf(metrics);

		}

		public void doBuildSql(JsonMetricSqlJoinQueryBuilder builder) {

			builder.sql.append("select ");
			List<Metric> ml = this.getMetricList();

			for (int i = 0; i < ml.size(); i++) {
				Metric m = ml.get(i);
				m.doBuildSql(null, builder);
				if (i < ml.size() - 1) {
					builder.sql.append(",");
				}

			}
			builder.sql.append(" from ");

			for (int i = 0; i < builder.reportTypeList.size(); i++) {
				String table = builder.reportTypeList.get(i);

				builder.sql.append(table).append(" r").append((i + 1));
				if (i < builder.reportTypeList.size() - 1) {
					builder.sql.append(",");
				}
			}

			builder.sql.append(" where ");

			for (int i = 0; i < builder.reportTypeList.size(); i++) {
				String table = builder.reportTypeList.get(i);

				builder.sql.append("r").append((i + 1)).append(".corpId=");

				if (i < builder.reportTypeList.size() - 1) {
					builder.sql.append("r").append((i + 2)).append(".corpId").append(" and ");
				} else {
					builder.sql.append("?");
				}
			}

		}
	}

	/**
	 * 
	 * @author wu
	 *
	 */
	private static abstract class Metric {
		private static Metric valueOf(JsonElement json) {
			if (json.isJsonObject()) {
				return new CompositeMetric(json.getAsJsonObject());
			} else {
				String alias = json.getAsString();
				return new LeafMetric(alias);

			}
		}

		static List<Metric> valueListOf(JsonArray children) {
			List<Metric> rt = new ArrayList<>();
			for (int i = 0; i < children.size(); i++) {
				JsonElement cI = children.get(i);
				Metric childI = Metric.valueOf(cI);
				rt.add(childI);
			}
			return rt;
		}

		abstract void doBuildSql(Metric m, JsonMetricSqlJoinQueryBuilder resolver);

	}

	/**
	 * 
	 * @author wu
	 *
	 */
	private static class LeafMetric extends Metric {
		private String reportTypeAndAlias;

		public LeafMetric(String reportTypeAndAlias) {
			this.reportTypeAndAlias = reportTypeAndAlias;

		}

		@Override
		void doBuildSql(Metric parent, JsonMetricSqlJoinQueryBuilder builder) {

			String[] reportType_Alias = reportTypeAndAlias.split("\\.");

			String reportType = null;
			String alias = null;
			if (reportType_Alias.length == 2) {
				reportType = reportType_Alias[0];
				alias = reportType_Alias[1];
			} else if (reportType_Alias.length == 1) {
				alias = reportType_Alias[0];

				List<String> reportTypeL = builder.aliasInfos.getReportTypeListByAlias(alias);
				if (reportTypeL.isEmpty()) {
					throw new RtException("not fount alias:" + alias);
				} else if (reportTypeL.size() > 1) {
					throw new RtException("too many alias found:" + alias);
				} else {
					reportType = reportTypeL.get(0);
				}
			} else {
				throw new RtException("illegal alias format:" + alias);
			}

			String cI = builder.aliasInfos.getColumnNameByAlias(reportType, alias);
			if (cI == null) {
				throw new RtException("no this alias:" + reportType + "." + alias);
			}

			String table = reportType;
			int idx = builder.reportTypeList.indexOf(reportType);

			if (idx == -1) {
				builder.reportTypeList.add(reportType);
				idx = builder.reportTypeList.size() - 1;
			}

			builder.sql.append("r" + (idx + 1)).append(".").append(cI);

		}

	}

	private static class CompositeMetric extends Metric {
		JsonObject obj;

		public CompositeMetric(JsonObject obj2) {
			this.obj = obj2;
		}

		String getName() {
			return obj.get("name").getAsString();
		}

		int getOffset() {
			return obj.get("offset").getAsInt();
		}

		String getOperator() {
			return obj.get("operator").getAsString();
		}

		List<Metric> getChildren() {
			JsonArray children = obj.get("metrics").getAsJsonArray();
			return Metric.valueListOf(children);
		}

		@Override
		void doBuildSql(Metric parent, JsonMetricSqlJoinQueryBuilder builder) {
			String operator = this.getOperator();
			if ("/".equals(operator)) {
				doBuild(operator, builder);
			} else if ("*".equals(operator)) {
				doBuild(operator, builder);
			} else {
				throw new RtException();
			}
		}

		private void doBuild(String operator, JsonMetricSqlJoinQueryBuilder builder) {
			List<Metric> childL = this.getChildren();
			if (childL.size() < 2) {
				throw new RtException("total child metrics at least 2 for div op");
			}
			builder.sql.append("(");
			for (int i = 0; i < childL.size(); i++) {
				Metric m = childL.get(i);
				m.doBuildSql(this, builder);
				if (i < childL.size() - 1) {
					builder.sql.append(operator);
				}
			}

			builder.sql.append(")");
		}
	}

}
