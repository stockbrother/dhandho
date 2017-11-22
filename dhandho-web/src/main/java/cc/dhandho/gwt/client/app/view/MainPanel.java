package cc.dhandho.gwt.client.app.view;

import java.util.List;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import cc.dhandho.gwt.client.app.InitDataJson;
import cc.dhandho.gwt.client.app.InitDataJson.TableRowJson;
import cc.dhandho.gwt.client.app.control.DataLoadClickHandler;
import cc.dhandho.gwt.client.app.control.SendClickHandler;
import cc.dhandho.gwt.client.common.ClientPanelUiObject;
import cc.dhandho.gwt.client.core.ConsolePanel;
import cc.dhandho.gwt.client.core.Handlers;

public class MainPanel extends ClientPanelUiObject<VerticalPanel> {
	//send to show chart.
	public Button sendButton;
	
	public Button dataLoadButton;

	//the corpId to show chart for.
	public TextBox corpId;

	//TODO remove
	public Label errorLabel;

	//TODO remove
	public MyDialogBox dialogBox;

	//the title list of chart, click to check and show them one by one.
	public VerticalPanel groupList;

	//init data loaded from server side.
	public InitDataJson mdTable;

	//the console of web client.
	public ConsolePanel console;

	//the chart list view.
	public ChartBoxGroupListView chartList;
	
	public MainPanel(Handlers handlers) {
		super(new VerticalPanel(), handlers, null);
		super.mainPanel = this;
		corpId = new TextBox();
		corpId.setText("000002");

		groupList = new VerticalPanel();

		sendButton = new Button("Send");
		dataLoadButton = new Button("Load Data");
		errorLabel = new Label();

		// We can add style names to widgets
		sendButton.addStyleName("sendButton");

		console = new ConsolePanel(this);

		chartList = new ChartBoxGroupListView(this, true);

		add(corpId);
		add(groupList);
		add(dataLoadButton);
		add(sendButton);
		add(chartList);
		add(console);
		add(errorLabel);

		// Create the popup dialog box
		dialogBox = new MyDialogBox(this);

		// Add a handler to send the name to the server
		SendClickHandler handler = new SendClickHandler(this);
		// sendButton.addClickHandler(handler);
		sendButton.addClickHandler(handler);
		
		dataLoadButton.addClickHandler(new DataLoadClickHandler(this));

	}

	private RootPanel get(String id) {
		RootPanel rt = RootPanel.get(id);
		if (rt == null) {
			throw new RuntimeException("no root panel with id:" + id);
		}
		return rt;

	}

	public void init(InitDataJson mdTable) {
		this.mdTable = mdTable;
		doLoadGroupList();

	}

	private void doLoadGroupList() {
		List<TableRowJson> rowL = mdTable.metricDefineGroupTable.getRowList();
		for (TableRowJson row : rowL) {
			String name = row.get(2).isString().stringValue();
			GroupSelectionLine group = new GroupSelectionLine(name, row, this);
			groupList.add(group);
			group.setSelect(true);
			this.console.print("add group:" + group);

		}

	}

	public void doLoadTemplate() {
		{

			String jsonS = ("{" //
					+ "'corpId':'000002'"// end of corpId
					+ ",'dates':[2016,2015,2014,2013,2012]"// end of years
					+ ",'metrics':" //
					+ "  [" //
					+ "    { 'name':'净资产收益率'," //
					+ "      'offset':0," //
					+ "      'operator':'*'," //
					+ "      'metrics':[" //
					+ "        { 'name':'总资产收益率'," //
					+ "          'offset':0," //
					+ "          'operator':'/'," //
					+ "          'metrics':[" //
					+ "            '净利润'," //
					+ "            '资产总计'" //
					+ "          ]" //
					+ "        }," //
					+ "        { 'name':'权益乘数'," //
					+ "          'offset':0," //
					+ "          'operator':'/'," //
					+ "          'metrics':[" //
					+ "          '资产总计'," //
					+ "          '所有者权益_或股东权益_合计'" //
					+ "          ]" //
					+ "        }" //
					+ "      ]" //
					+ "    }" //
					+ "  ]" // end of formulas
					+ "}" // end of message
			).replaceAll("'", "\"");
			JSONObject jsonO = (JSONObject) JSONParser.parse(jsonS);
			// TemplateLine tLine = new TemplateLine("净资产收益率", jsonO, this);
			// this.templateList.add(tLine);
		}
		{

			String jsonS = ("{" //
					+ "'corpId':'000002'"// end of corpId
					+ ",'dates':[2016,2015,2014,2013,2012]"// end of years
					+ ",'metrics':[" //
					+ "  '资产总计'," //
					+ "  '所有者权益_或股东权益_合计'" //
					+ " ]" //
					+ "}" // end of message
			).replaceAll("'", "\"");
			JSONObject jsonO = (JSONObject) JSONParser.parse(jsonS);
			// TemplateLine tLine = new TemplateLine("资产与权益", jsonO, this);
			// this.templateList.add(tLine);
		}
	}
}
