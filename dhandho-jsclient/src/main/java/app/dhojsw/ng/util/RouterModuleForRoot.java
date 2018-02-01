package app.dhojsw.ng.util;

import app.dhojsw.ng.AppComponent;
import app.dhojsw.ng.command.CommandComponent;
import app.dhojsw.ng.my.MyComponent;
import def.angular.core.ModuleWithProviders;
import def.angular.router.Route;
import def.angular.router.RouterModule;
import def.angular.router.Routes;
import jsweet.lang.Array;

/**
 * 
 * @author wu
 *
 */
public class RouterModuleForRoot {

	public static ModuleWithProviders forRoot() {
		Array<Route> routes = new Array<>();
		push(routes, "app", AppComponent.class);
		push(routes, "commond", CommandComponent.class);
		push(routes, "my", MyComponent.class);

		return RouterModule.forRoot(routes);
	}

	public static void push(Array<Route> routes, String path, Class<?> component) {
		Route r = new Route();
		r.component = component;
		r.path = path;
		routes.push(r);
	}
}
