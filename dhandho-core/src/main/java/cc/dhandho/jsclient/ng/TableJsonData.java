package cc.dhandho.jsclient.ng;

public class TableJsonData extends JsonData {

	private TableJsonData(Object json) {
		super("table",json);
	}

	public static TableJsonData valueOf(Object json) {
		TableJsonData rt = new TableJsonData(json);
		return rt;
	}

}
