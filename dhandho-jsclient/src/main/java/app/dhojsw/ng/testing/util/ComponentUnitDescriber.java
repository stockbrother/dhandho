package app.dhojsw.ng.testing.util;

import def.angular.common_http_testing.HttpTestingController;
import def.angular.core.DebugElement;
import def.angular.core_testing.ComponentFixture;
import def.dom.Element;

public class ComponentUnitDescriber<C> extends AngularUnitDescriber {
	public HttpTestingController httpMock;

	public C comp;

	public ComponentFixture<C> fixture;

	public DebugElement de;

	public Element ne;
	
	public ComponentUnitDescriber(String desc) {
		super(desc);
	}

}
