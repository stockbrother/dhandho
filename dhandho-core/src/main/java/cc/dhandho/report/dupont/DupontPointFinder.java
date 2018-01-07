package cc.dhandho.report.dupont;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import com.age5k.jcps.JcpsException;
import com.age5k.jcps.framework.handler.Handler3;
import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import cc.dhandho.graphdb.DbUtil;
import cc.dhandho.graphdb.OResultSetHandler;
import cc.dhandho.graphdb.dataver.DbUpgrader0_0_1;
import cc.dhandho.input.xueqiu.DateUtil;
import cc.dhandho.rest.server.DbProvider;

public class DupontPointFinder {

	private String[] typeArray;
	private Map<String, Integer> typeIndexMap;// index of type in array.
	private DbProvider dbProvider;
	private int year;
	
	public DupontPointFinder(int year, String[] clazzA, DbProvider dbProvider) {
		this.year = year;
		this.dbProvider = dbProvider;
		this.typeArray = clazzA;
		this.typeIndexMap = new HashMap<>();
		for (int i = 0; i < clazzA.length; i++) {
			this.typeIndexMap.put(typeArray[i], i);
		}
	}

	public int getTypeIndex(String type) {
		return this.typeIndexMap.get(type);
	}

	public Map<String,CorpPoint> find() {
		Date reportDate = DateUtil.newDateOfYearLastDay(year, TimeZone.getDefault());
		Object[] argA = new Object[this.typeArray.length + 1];
		argA[0] = reportDate;
		StringBuilder sql = new StringBuilder();
		sql.append("select from " + DbUpgrader0_0_1.V_DUPONT_VNODE + " where reportDate = ? and (");
		for (int i = 0; i < this.typeArray.length; i++) {
			argA[i + 1] = this.typeArray[i];
			sql.append("define = ?");
			if (i < this.typeArray.length - 1) {
				sql.append(" or ");
			}
		}
		sql.append(")");

		return dbProvider.executeWithDbSession(new Handler3<ODatabaseSession, Map<String,CorpPoint>>() {

			@Override
			public Map<String,CorpPoint> handle(ODatabaseSession t) {

				return DbUtil.executeQuery(t, sql.toString(), argA, new OResultSetHandler<Map<String,CorpPoint>>() {

					@Override
					public Map<String,CorpPoint> handle(OResultSet arg0) {
						Map<String, CorpPoint> pointMap = new HashMap<>();
						while (arg0.hasNext()) {
							OResult rI = arg0.next();
							OVertex vI = rI.getVertex().get();
							String corpId = vI.getProperty("corpId");
							String type = vI.getProperty("define");
							Double value = vI.getProperty("value");
							CorpPoint point = pointMap.get(corpId);
							if (point == null) {
								point = new CorpPoint();
								point.corpId = corpId;
								point.point = new Double[typeArray.length];
								pointMap.put(corpId, point);
							}
							Integer idx = DupontPointFinder.this.typeIndexMap.get(type);
							if (idx == null) {
								throw new JcpsException("type index not found in map:" + type);
							}

							point.point[idx] = value;

						}
						pointMap = filterPoints(pointMap);
						return pointMap;
					}

				});
			}
		});
	}

	protected Map<String, CorpPoint> filterPoints(Map<String, CorpPoint> pointMap) {
		return pointMap;
	}

}
