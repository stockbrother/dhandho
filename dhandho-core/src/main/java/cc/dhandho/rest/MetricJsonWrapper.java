package cc.dhandho.rest;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import cc.dhandho.RtException;

/**
 * 
 * @author wu
 *
 */
abstract class MetricJsonWrapper extends JsonWrapper {
	private static MetricJsonWrapper valueOf(MetricJsonWrapper parent, JsonElement json) {
		if (json.isJsonObject()) {
			return new CompositeMetric(parent, json.getAsJsonObject());
		} else {
			String alias = json.getAsString();
			return new LeafMetric(parent, alias);

		}
	}

	static List<MetricJsonWrapper> valueListOf(MetricJsonWrapper parent, JsonArray children) {
		List<MetricJsonWrapper> rt = new ArrayList<>();
		for (int i = 0; i < children.size(); i++) {
			JsonElement cI = children.get(i);
			MetricJsonWrapper childI = MetricJsonWrapper.valueOf(parent, cI);
			rt.add(childI);
		}
		return rt;
	}

	MetricJsonWrapper parent;

	MetricJsonWrapper(MetricJsonWrapper parent) {
		this.parent = parent;
	}

	abstract int resolveOffset();

	abstract void doBuildSql(MetricJsonWrapper m, JsonMetricSqlLinkQueryBuilder resolver);

	abstract String resolveNameAsTitle();

	/**
	 * 
	 * @author wu
	 *
	 */
	static class LeafMetric extends MetricJsonWrapper {
		private String reportTypeAndAlias;

		public LeafMetric(MetricJsonWrapper parent, String reportTypeAndAlias) {
			super(parent);
			this.reportTypeAndAlias = reportTypeAndAlias;

		}

		@Override
		void doBuildSql(MetricJsonWrapper parent, JsonMetricSqlLinkQueryBuilder builder) {

			String[] reportType_Alias = reportTypeAndAlias.split("\\.");

			String reportType = null;
			String alias = null;
			int offsetInline = 0;
			if (reportType_Alias.length == 2) {
				reportType = reportType_Alias[0];
				alias = reportType_Alias[1];
			} else if (reportType_Alias.length == 1) {
				alias = reportType_Alias[0];
			} else {
				throw new RtException("illegal alias format:" + alias);
			}

			int idx = alias.indexOf('[');

			if (idx >= 0) {
				String offsetS = alias.substring(idx + 1, alias.length() - 1);
				offsetInline = Integer.parseInt(offsetS);
				alias = alias.substring(0, idx);
			}

			if (reportType == null) {
				// resolve it
				reportType = builder.aliasInfos.getFirstReportTypeByAlias(alias);
				if (reportType == null) {
					throw new RtException("not fount alias:" + alias);
				}
				
			}
			String cI = builder.aliasInfos.getColumnNameByAlias(reportType, alias);
			if (cI == null) {
				throw new RtException("no this alias:" + reportType + "." + alias);
			}

			String link = reportType.toLowerCase();
			int offset = offsetInline + this.resolveOffset();
			if (offset > 0) {

				for (int i = 0; i < offset; i++) {
					builder.sql.append("next.");
				}
			} else if (offset < 0) {

				for (int i = 0; i > offset; i--) {
					builder.sql.append("prev.");
				}
			}

			builder.sql.append(link).append(".").append(cI);
		}

		@Override
		String resolveNameAsTitle() {
			return reportTypeAndAlias;
		}

		@Override
		int resolveOffset() {
			if (parent == null) {
				return 0;
			}
			return parent.resolveOffset();
		}

	}

	static class CompositeMetric extends MetricJsonWrapper {
		JsonObject obj;

		public CompositeMetric(MetricJsonWrapper parent, JsonObject obj2) {
			super(parent);
			this.obj = obj2;
		}

		String getName() {
			return obj.get("name").getAsString();
		}

		int getOffset() {
			JsonElement offset = obj.get("offset");
			return offset == null ? 0 : offset.getAsInt();
		}

		String getOperator() {
			return obj.get("operator").getAsString();
		}

		List<MetricJsonWrapper> getChildren() {
			JsonArray children = obj.get("metrics").getAsJsonArray();
			return MetricJsonWrapper.valueListOf(this, children);
		}

		@Override
		int resolveOffset() {
			int offset = this.getOffset();
			if (parent != null) {
				offset += parent.resolveOffset();
			}
			return offset;
		}

		@Override
		void doBuildSql(MetricJsonWrapper parent, JsonMetricSqlLinkQueryBuilder builder) {
			String operator = this.getOperator();
			if ("/".equals(operator)) {
				doBuild(operator, builder);
			} else if ("*".equals(operator)) {
				doBuild(operator, builder);
			} else if ("+".equals(operator)) {
				doBuild(operator, builder);
			} else if ("-".equals(operator)) {
				doBuild(operator, builder);
			} else if ("sum".equals(operator)) {
				doBuild("+", builder);
			} else if ("avg".equals(operator)) {
				doBuildAvg(builder);
			} else {
				throw new RtException("todo:" + operator);
			}
		}

		private void doBuildAvg(JsonMetricSqlLinkQueryBuilder builder) {
			List<MetricJsonWrapper> childL = this.getChildren();
			builder.sql.append("(");
			doBuild("+", builder);
			builder.sql.append("/").append(childL.size()).append(")");

		}

		private void doBuild(String operator, JsonMetricSqlLinkQueryBuilder builder) {
			List<MetricJsonWrapper> childL = this.getChildren();
			if (childL.size() < 2) {
				throw new RtException("total child metrics at least 2 for div op");
			}
			builder.sql.append("(");
			for (int i = 0; i < childL.size(); i++) {
				MetricJsonWrapper m = childL.get(i);
				m.doBuildSql(this, builder);
				if (i < childL.size() - 1) {
					builder.sql.append(operator);
				}
			}

			builder.sql.append(")");
		}

		@Override
		String resolveNameAsTitle() {
			return getName();
		}
	}

}