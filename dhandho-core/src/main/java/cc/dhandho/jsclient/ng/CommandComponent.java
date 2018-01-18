package cc.dhandho.jsclient.ng;

import java.util.function.Function;

import def.angular.core.Component;
import def.angular.http.Http;
import def.angular.http.Response;
import def.dom.Globals;
import def.js.Array;
import def.js.PropertyDescriptor;
import def.rxjs.rxjs.Observable;
import static jsweet.util.Lang.typeof;

@Component(//
		selector = "command", //
		templateUrl = "./ng/command.component.html", styleUrls = { "./ng/command.component.css" })

public class CommandComponent {
	public String command;

	public Array<CommandResponse> responseArray = new Array<>();

	public CommandResponse lastResponse;

	String url = "/web/cmd/";

	Http http;

	LoggerService log;

	public CommandComponent(Http http, LoggerService log) {
		this.http = http;
		this.log = log;
	}

	public void onResponse(long requestTime, String command, Object json) {
		//
		PropertyDescriptor typeP = def.js.Object.getOwnPropertyDescriptor(json, "type");
		String type = (String) typeP.value;

		Array<def.js.String> names = def.js.Object.getOwnPropertyNames(json);

		CommandResponse rt = new CommandResponse(requestTime, command, json);

		this.responseArray.unshift(rt);//

	}

	public void onButtonClick() {
		Globals.console.log("on Button Click,command:" + this.command);
		String command = this.command;
		long requestTime = System.currentTimeMillis();
		Observable<Response> ores = http.post(url, command);
		ores.toPromise().thenOnFulfilledFunction(new Function<Response, Object>() {

			@Override
			public Object apply(Response t) {
				Object json = t.json();

				CommandComponent.this.onResponse(requestTime, command, json);

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
	public boolean showChart() {
		return true;
	}

	public boolean isNumber(def.js.Object json) {
		String type = typeof(json);
		log.debug("typeof,json:" + json + ",is:" + type);
		return type.equals("number");
	}
}
