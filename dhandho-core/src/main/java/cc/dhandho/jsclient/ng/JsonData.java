package cc.dhandho.jsclient.ng;

public abstract class JsonData extends JsonWrapper {
	public String type;
	public Object json;

	public JsonData(String type, Object json) {
		this.json = json;
		this.type = type;
	}

	

}
