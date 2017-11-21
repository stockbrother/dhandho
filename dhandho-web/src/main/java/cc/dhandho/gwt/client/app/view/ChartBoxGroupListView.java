package cc.dhandho.gwt.client.app.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ChartBoxGroupListView extends VerticalPanel {

	MainPanel mainPanel;

	private List<ChartBoxGroup> groupList;

	private boolean isJinRon;

	public ChartBoxGroupListView(MainPanel mainPanel, boolean jinRon) {
		this.mainPanel = mainPanel;
		this.isJinRon = jinRon;
		this.groupList = new ArrayList<>();
	}

	public void addChart(boolean isJinRon,String chart) {
		ChartBoxGroup cg = null;

		if (groupList.size() > 0) {
			cg = groupList.get(groupList.size() - 1);
			if (cg.map.size() >= 3) {
				cg = null;
			}
		}
		if (cg == null) {
			cg = new ChartBoxGroup(this.mainPanel);
			this.add(cg);
			this.groupList.add(cg);

		}
		cg.addChart(isJinRon, chart);
	}

	public void removeChart(String chart) {
		ChartBoxGroup cg = this.findGroupByChart(chart);
		if (cg == null) {
			return;
		}
		cg.removeChart(chart);

	}

	private ChartBoxGroup findGroupByChart(String chart) {
		for (ChartBoxGroup cg : this.groupList) {

			if (cg.getChart(chart) != null) {
				return cg;
			}
		}
		return null;
	}

	public static class ChartBoxGroup extends HorizontalPanel {

		private Map<String, ChartBox> map;
		MainPanel mainPanel;

		ChartBoxGroup(MainPanel mainPanel) {
			this.mainPanel = mainPanel;
			this.map = new HashMap<>();
		}

		public ChartBox getChart(String chart) {
			return map.get(chart);
		}

		public void addChart(boolean isJinRon, String chart) {
			if (this.map.get(chart) != null) {
				return;
			}
			ChartBox cc = new ChartBox(isJinRon, chart, this.mainPanel);
			this.add(cc);
			this.map.put(chart, cc);
		}

		public void removeChart(String chart) {
			ChartBox cc = this.map.remove(chart);
			if (cc != null) {
				this.remove(cc);//
			}
		}

		public void updateAll(boolean isJinRon) {

			for (ChartBox cc : this.map.values()) {
				cc.update(isJinRon);
			}
		}

	}

	public void updateAll(boolean isJinRon) {

		for (ChartBoxGroup cg : this.groupList) {
			cg.updateAll(isJinRon);
		}
	}

}
