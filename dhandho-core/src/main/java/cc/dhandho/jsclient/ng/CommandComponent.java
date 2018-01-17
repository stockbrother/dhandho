package cc.dhandho.jsclient.ng;

import java.util.function.Function;

import def.angular.core.Component;
import def.angular.http.Http;
import def.angular.http.Response;
import def.dom.Globals;
import def.rxjs.rxjs.Observable;

@Component(//
		selector = "command", //
		templateUrl = "./ng/command.component.html"

)

public class CommandComponent {
	public String command;
	public JsonResponse response;
	String url = "/web/cmd/";
	Http http;
	LoggerService log;

	public CommandComponent(Http http, LoggerService log) {
		this.http = http;
		this.log = log;
	}

	public void onButtonClick() {
		Globals.console.log("on Button Click,command:" + this.command);

		Observable<Response> ores = http.post(url, command);
		ores.toPromise().thenOnFulfilledFunction(new Function<Response, Object>() {

			@Override
			public Object apply(Response t) {
				Object json = t.json();
				CommandComponent.this.response = JsonResponse.valueOf(json);
				log.debug("on Button Click,command:" + response);
				return null;
			}
		});

	}
}
