package app.dhojsw.ng.command;

import static def.dhojsw.jsnative.Globals.js_isNumber;
import def.angular.common_http.HttpClient;
import def.angular.core.Component;
import def.js.Array;
import def.js.PropertyDescriptor;
import def.rxjs.rxjs.Observable;

@Component(//
		selector = "app-command", //
		templateUrl = "./command.component.html", //
		styleUrls = { "./command.component.css" }//
)

public class CommandComponent {
	public String title;
	public String command;
	public Array<CommandResponse> responseArray = new Array<>();
	String url = "/web/cmd/";
	HttpClient http;

	public CommandComponent(HttpClient http) {
		this.title = "Command Component";
		this.http = http;
	}

	public void onResponse(double requestTime, String command, Object json) {
		PropertyDescriptor typeP = def.js.Object.getOwnPropertyDescriptor(json, "type");
		String type = (String) typeP.value;

		Array<def.js.String> names = def.js.Object.getOwnPropertyNames(json);
		CommandResponse rt = new CommandResponse(requestTime, command, json);
		this.responseArray.unshift(rt);
	}

	public void onButtonClick() {
		// console.log( "on Button Click,command:" + this.command );
		String command = this.command;
		double requestTime = def.js.Date.now();
		Observable<Object> ores = this.http.post(this.url, command);

		ores.toPromise().then((json) -> {
			// console.log( json );
			this.onResponse(requestTime, command, json);
			// this.log.debug( "post response:" + json );
			return null;
		})._catch((Object t) -> {
			// this.log.error( "catched exception" );
			// this.log.error( t );
			return null;
		});
	}

	public boolean showChart() {
		return true;
	}

	public boolean isNumber(Object json) {
		return js_isNumber(json);
	}
}
