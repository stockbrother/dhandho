package cc.dhandho.report.dupont;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.dhandho.report.dupont.node.DefineNode;
import cc.dhandho.rest.server.DbProvider;

public class PointsFilterDupontSvgBuilder extends DupontPointFinder {

	private static final Logger LOG = LoggerFactory.getLogger(PointsFilterDupontSvgBuilder.class);

	private double filter = 1.0D;

	private String centerCorpId;

	public PointsFilterDupontSvgBuilder(int year, String[] clazzA, DbProvider dbProvider) {
		super(year, clazzA, dbProvider);
	}

	public PointsFilterDupontSvgBuilder centerCorpId(String corpId) {
		this.centerCorpId = corpId;
		return this;
	}

	public PointsFilterDupontSvgBuilder filter(double filter) {
		assert filter >= 0.0D && filter <= 1D;
		this.filter = filter;
		return this;
	}

	@Override
	protected Map<String, CorpPoint> filterPoints(Map<String, CorpPoint> pointMap) {

		CorpPoint theCenter = pointMap.get(this.centerCorpId);
		if (theCenter == null) {
			LOG.warn("point not found for corpId:" + this.centerCorpId);
			return pointMap;
		}

		Map.Entry<String, CorpPoint>[] pointArray = pointMap.entrySet()//
				.stream()//
				.filter(new Predicate<Entry<String, CorpPoint>>() {
					@Override
					public boolean test(Entry<String, CorpPoint> t) {
						CorpPoint v = t.getValue();
						return !v.containsNull();
					}
				})// filter out the point with null x or y value.
				.sorted(new Comparator<Map.Entry<String, CorpPoint>>() {

					@Override
					public int compare(Entry<String, CorpPoint> o1, Entry<String, CorpPoint> o2) {
						CorpPoint p1 = o1.getValue();
						double d1 = theCenter.distance(p1);
						CorpPoint p2 = o2.getValue();

						double d2 = theCenter.distance(p2);
						return (int) ((d1 - d2) * 10000);// TODO magic number?
					}
				}).toArray(Map.Entry[]::new);
		int total = pointArray.length;
		
		int size = (int) (total * this.filter);
		Map<String, CorpPoint> rt = new HashMap<>();
		for (int i = 0; i < size; i++) {
			rt.put(pointArray[i].getKey(), pointArray[i].getValue());
		}
		return rt;
	}

}
