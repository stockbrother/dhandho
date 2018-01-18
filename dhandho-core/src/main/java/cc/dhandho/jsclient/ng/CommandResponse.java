package cc.dhandho.jsclient.ng;

public class CommandResponse extends JsonWrapper {
	
	public String command;
	
	public Object json;

	public long responseTime;
	
	public long requestTime;
	
	public CommandResponse(long requestTime, String command, Object json) {
		this.requestTime = requestTime;
		this.json = json;
		this.command = command;
		responseTime = System.currentTimeMillis();
		
	}
	
	public long getTimeCost() {
		return this.responseTime - this.requestTime;
	}

	

}
