package cc.dhandho.report.dupont;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.age5k.jcps.JcpsException;
import com.age5k.jcps.framework.provider.Provider;

import cc.dhandho.mycorp.MyCorps;
import cc.dhandho.rest.server.DbProvider;

public class CenterPointDistanceBasedFilterDupontPointFinder extends DupontPointFinder {

	private static final Logger LOG = LoggerFactory.getLogger(CenterPointDistanceBasedFilterDupontPointFinder.class);

	private CorpFilter filter;

	private String centerCorpId;

	Provider<MyCorps> myCorpsProvider;

	private static Predicate<Entry<String, CorpPoint>> NotNullFilter = new Predicate<Entry<String, CorpPoint>>() {
		@Override
		public boolean test(Entry<String, CorpPoint> t) {
			CorpPoint v = t.getValue();
			return !v.containsNull();
		}
	};

	public CenterPointDistanceBasedFilterDupontPointFinder(int year, String[] clazzA, DbProvider dbProvider) {
		super(year, clazzA, dbProvider);
	}

	public CenterPointDistanceBasedFilterDupontPointFinder myCorpsProvider(Provider<MyCorps> mcp) {
		this.myCorpsProvider = mcp;
		return this;
	}

	public CenterPointDistanceBasedFilterDupontPointFinder centerCorpId(String corpId) {
		this.centerCorpId = corpId;
		return this;
	}

	public CenterPointDistanceBasedFilterDupontPointFinder filter(CorpFilter filter) {

		this.filter = filter;
		return this;
	}

	@Override
	protected Map<String, CorpPoint> filterPoints(Map<String, CorpPoint> pointMap) {
		if (filter.groupName != null) {
			return filterPoints(pointMap, filter.groupName);
		} else {
			return filterPoints(pointMap, filter.centerAroundPercentage);
		}
	}

	protected Map<String, CorpPoint> filterPoints(Map<String, CorpPoint> pointMap, String groupName) {
		if (!groupName.equals("mycorps")) {
			throw new JcpsException("todo");
		}
		Map<String, CorpPoint> rt = new HashMap<>();
		List<String> idL = this.myCorpsProvider.get().getCorpIdList();
		pointMap.entrySet().stream()//
				.filter(NotNullFilter)//
				.filter(new Predicate<Entry<String, CorpPoint>>() {

					@Override
					public boolean test(Entry<String, CorpPoint> t) {
						String id = t.getKey();
						return idL.contains(id);
					}
				})//
				.forEach(new Consumer<Entry<String, CorpPoint>>() {

					@Override
					public void accept(Entry<String, CorpPoint> t) {
						rt.put(t.getKey(), t.getValue());
					}
				});
		;

		return rt;

	}

	protected Map<String, CorpPoint> filterPoints(Map<String, CorpPoint> pointMap, float centerPercentage) {

		CorpPoint theCenter = pointMap.get(this.centerCorpId);
		if (theCenter == null) {
			LOG.warn("point not found for corpId:" + this.centerCorpId);
			return pointMap;
		}

		Map.Entry<String, CorpPoint>[] pointArray = pointMap.entrySet()//
				.stream()//
				.filter(NotNullFilter)// filter out the point with null x or y value.
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

		int size = (int) (total * centerPercentage);
		Map<String, CorpPoint> rt = new HashMap<>();
		for (int i = 0; i < size; i++) {
			rt.put(pointArray[i].getKey(), pointArray[i].getValue());
		}
		return rt;
	}

}
