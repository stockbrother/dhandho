package cc.dhandho.jsclient.ng;

import java.util.function.Function;

import def.angular.core.Component;
import def.angular.http.Http;
import def.angular.http.Response;
import def.dom.Globals;
import def.js.Array;
import def.js.PropertyDescriptor;
import def.rxjs.rxjs.Observable;

@Component(//
		selector = "command", //
		templateUrl = "./ng/command.component.html"

)

public class CommandComponent {
	public String command;
	public JsonData responseData ;
	String url = "/web/cmd/";
	Http http;
	LoggerService log;

	public CommandComponent(Http http, LoggerService log) {
		this.http = http;
		this.log = log;
	}
	public static JsonData valueOf(Object json) {
		//
		PropertyDescriptor typeP = def.js.Object.getOwnPropertyDescriptor(json, "type");
		String type = (String) typeP.value;

		Array<def.js.String> names = def.js.Object.getOwnPropertyNames(json);
		if (type.equals("table")) {
			TableJsonData rt = TableJsonData.valueOf(json);
			return rt;
		} else {
			return new AnyJsonData(json);
		}
		//return null;

	}
	public void onButtonClick() {
		Globals.console.log("on Button Click,command:" + this.command);

		Observable<Response> ores = http.post(url, command);
		ores.toPromise().thenOnFulfilledFunction(new Function<Response, Object>() {

			@Override
			public Object apply(Response t) {
				Object json = t.json();
				CommandComponent.this.responseData = valueOf(json);
				log.debug("post response:" + json);
				return null;
			}
		}).catchOnRejectedFunction(new Function<Object, Object>() {

			@Override
			public Object apply(Object t) {
				log.debug("catchOnRejectedFunction,command:" + command);
				return null;
			}
		});

	}
}
