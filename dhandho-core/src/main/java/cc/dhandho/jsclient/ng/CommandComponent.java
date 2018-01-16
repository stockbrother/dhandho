package cc.dhandho.jsclient.ng;

import java.util.function.Function;

import def.angular.core.Component;
import def.angular.http.Http;
import def.angular.http.Response;
import def.dom.Globals;
import def.js.JSON;
import def.rxjs.rxjs.Observable;

@Component(//
		selector = "command", //
		templateUrl = "./ng/command.component.html"

)

public class CommandComponent {
	public String command;
	public String response;
	String url = "/web/cmd/";
	Http http;
	public CommandComponent(Http http) {
		this.http = http;
	}

	public void onButtonClick() {
		Globals.console.log("on Button Click,command:" + this.command);
		
		Observable<Response> ores = http.post(url, command);
		ores.toPromise().thenOnFulfilledFunction(new Function<Response, Object>() {

			@Override
			public Object apply(Response t) {
				Object json = t.json();
				String response = JSON.stringify(json);
				CommandComponent.this.response = response;
				Globals.console.log("on Button Click,command:" + response);
				return null;
			}
		});
		
	}
}
