package cc.dhandho.gwt.client.app.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

import cc.dhandho.gwt.client.app.InitDataJson.TableRowJson;

/**
 * Select the group of metrics and show them as a chart.
 * @author wu
 *
 */
public class GroupSelectionLine extends HorizontalPanel implements ClickHandler {

	MainPanel mainPanel;

	String group;

	Label title;
	CheckBox select;

	public boolean isJinRon;

	public GroupSelectionLine(String group, TableRowJson groupDefine, MainPanel mainPanel) {
		String YN = groupDefine.get(0).isString().stringValue();
		this.isJinRon = "Y".equalsIgnoreCase(YN);

		this.title = new Label(group + (isJinRon ? "/JR" : "/Non-JR"));
		this.add(this.title);

		this.group = group;

		this.mainPanel = mainPanel;
		this.select = new CheckBox();
		this.add(this.select);
		this.select.addClickHandler(this);
	}

	public void setSelect(boolean check) {
		this.select.setValue(check);
		if (check) {
			mainPanel.chartList.addChart(this.isJinRon, this.group);//
		} else {
			mainPanel.chartList.removeChart(this.group);//
		}

	}

	@Override
	public void onClick(ClickEvent event) {

		this.setSelect(this.select.getValue());
	}

}
