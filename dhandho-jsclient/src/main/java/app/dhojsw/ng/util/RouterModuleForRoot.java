package app.dhojsw.ng.util;

import app.dhojsw.ng.PageNotFoundComponent;
import app.dhojsw.ng.command.CommandComponent;
import app.dhojsw.ng.my.MyComponent;
import def.angular.core.ModuleWithProviders;
import def.angular.router.Route;
import def.angular.router.RouterModule;
import jsweet.lang.Array;

/**
 * 
 * @author wu
 *
 */
public class RouterModuleForRoot {

	public static ModuleWithProviders forRoot() {

		Array<Route> routes = new Array<>();		
		push(routes, "command", CommandComponent.class);
		push(routes, "my", MyComponent.class);
		push(routes, "**", PageNotFoundComponent.class);

		return RouterModule.forRoot(routes);
	}

	public static void push(Array<Route> routes, String path, Class<?> component) {
		Route r = new Route();
		r.component = component;
		r.path = path;
		routes.push(r);
	}
}
