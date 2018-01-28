package app.dhojsw.ng.command;

import static app.dhojsw.ng.testing.Jasmine.describe;
import static app.dhojsw.ng.testing.Jasmine.expect;
import static app.dhojsw.ng.testing.Jasmine.it;
import static def.dhojsw.jsnative.Globals.angular_async;
import static def.dhojsw.jsnative.Globals.angular_fakeAsync;
import static def.dhojsw.jsnative.Globals.angular_newTestModuleMetadataBuilder;
import static def.dhojsw.jsnative.Globals.angular_tick;

import app.dhojsw.ng.testing.Jasmine;
import def.angular.common_http_testing.HttpClientTestingModule;
import def.angular.common_http_testing.HttpTestingController;
import def.angular.common_http_testing.RequestMatch;
import def.angular.common_http_testing.TestRequest;
import def.angular.core.DebugElement;
import def.angular.core_testing.ComponentFixture;
import def.angular.core_testing.TestBed;
import def.angular.forms.FormsModule;
import def.angular.platform_browser.By;
import def.dom.Element;
import def.js.JSON;

public class CommandComponentSpec {
	public static class DescribeContext {
		HttpTestingController httpMock;

		CommandComponent comp;

		ComponentFixture<CommandComponent> fixture;

		DebugElement de;

		Element ne;

	}

	public static class FirstDescribeContext extends DescribeContext implements Runnable {
		@Override
		public void run() {

			Jasmine.beforeEach(() -> {

				TestBed.configureTestingModule(angular_newTestModuleMetadataBuilder()//
						.imports(FormsModule.class, HttpClientTestingModule.class)//
						.declarations(CommandComponent.class)//
						// .providers(LoggerService.class);
						.build()//
				);
				TestBed.compileComponents();
				httpMock = TestBed.get(HttpTestingController.class);
				fixture = TestBed.createComponent(CommandComponent.class);
				de = fixture.debugElement;
				comp = (CommandComponent) de.componentInstance;
				ne = de.nativeElement;

			});
			it("1.1.Should create the command", angular_async(() -> {
				Jasmine.expect(comp).toBeTruthy();
			}));
			it("1.2.Title check", angular_async(() -> {
				Element compiled = fixture.debugElement.nativeElement;
				it("Title should not be binded until fixture.detectChanges()", () -> {
					Jasmine.expect(compiled.querySelector("h1").textContent).toEqual("");
				});
				fixture.detectChanges();
				Jasmine.expect(compiled.querySelector("h1").textContent).toContain(comp.title);
			}));
/**
			it("1.3.Response Processing", angular_fakeAsync(() -> {
				String jsonS = ("{\r\n" + //
				"            'type': 'table',\r\n" + //
				"            'columnNames': ['corpId', 'corpName', 'highLight', 'ProfitMargin', 'AssetTurnover', 'EquityMultiplier'],\r\n"
						+ //
				"            'rowArray': [\r\n" + //
				"                ['000001', 'Name1', false, 0.01, 0.11, 1.1],\r\n" + //
				"                ['000002', 'Name2', true, 0.02, 0.12, 1.2],\r\n" + //
				"                ['000003', 'Name3', false, 0.03, 0.13, 1.3],\r\n" + //
				"                ['000004', 'Name4', false, 0.04, 0.14, 1.4]\r\n" + //
				"            ]\r\n" + //
				"        }").replace('\'', '\"');
				Object response = JSON.parse(jsonS);
				expect(comp.responseArray.length).toEqual(0);
				{
					 comp.command = "dupont -r -c 000001 -y 2016 -f 0.01";
			            DebugElement button = fixture.debugElement.query( By.css( "button" ) );
			            button.triggerEventHandler( "click", null );
			            RequestMatch rm = new RequestMatch();
			            rm.method = "POST";
			            TestRequest req = httpMock.expectOne( rm );
			            req.flush( response );
			            fixture.detectChanges();
			            angular_tick();
			            
			            expect( comp.responseArray.length ).toEqual( 1 );
			            fixture.detectChanges();
				}
				Element compiled = fixture.debugElement.nativeElement;
				it("Title should not be binded until fixture.detectChanges()", () -> {
					Jasmine.expect(compiled.querySelector("h1").textContent).toEqual("");
				});
				fixture.detectChanges();
				Jasmine.expect(compiled.querySelector("h1").textContent).toContain(comp.title);
			}));**/
		}
	}

	public static void main(String[] args) {

		describe("My First Test", new FirstDescribeContext());

		describe("My Second Test", new Runnable() {

			@Override
			public void run() {
				it("2.1", new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub

					}
				});

			}
		});
	}
}
