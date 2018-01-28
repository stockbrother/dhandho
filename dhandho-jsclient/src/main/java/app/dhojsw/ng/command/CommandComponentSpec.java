package app.dhojsw.ng.command;

import static def.angular.core_testing.Globals.async;
import static def.angular.core_testing.Globals.fakeAsync;
import static def.angular.core_testing.Globals.tick;
import static def.jasmine.Globals.beforeEach;
import static def.jasmine.Globals.describe;
import static def.jasmine.Globals.expect;
import static def.jasmine.Globals.it;

import app.dhojsw.ng.service.LoggerService;
import def.angular.common_http_testing.HttpClientTestingModule;
import def.angular.common_http_testing.HttpTestingController;
import def.angular.common_http_testing.RequestMatch;
import def.angular.common_http_testing.TestRequest;
import def.angular.core.DebugElement;
import def.angular.core_testing.ComponentFixture;
import def.angular.core_testing.TestBed;
import def.angular.core_testing.TestModuleMetadata;
import def.angular.forms.FormsModule;
import def.angular.platform_browser.By;
import def.dom.Element;
import def.js.JSON;

public class CommandComponentSpec {
	public static class DescribeContext<C> {
		HttpTestingController httpMock;

		C comp;

		ComponentFixture<C> fixture;

		DebugElement de;

		Element ne;

	}

	public static class FirstDescribeContext extends DescribeContext<CommandComponent> implements Runnable {
		@Override
		public void run() {

			beforeEach(() -> {
				TestModuleMetadata meta = new TestModuleMetadata();
				meta.imports = new Class<?>[] { FormsModule.class, HttpClientTestingModule.class };
				meta.declarations = new Class<?>[] { CommandComponent.class };
				meta.providers = new Class[] { LoggerService.class };
				TestBed.configureTestingModule(meta);
				TestBed.compileComponents();
				httpMock = TestBed.get(HttpTestingController.class);
				fixture = TestBed.createComponent(CommandComponent.class);
				de = fixture.debugElement;
				comp = (CommandComponent) de.componentInstance;
				ne = de.nativeElement;

			});
			it("1.1.Should create the command", async(() -> {
				expect(comp).toBeTruthy();
			}));
			it("1.2.Title check", async(() -> {
				Element compiled = fixture.debugElement.nativeElement;
				it("Title should not be binded until fixture.detectChanges()", (done) -> {
					expect(compiled.querySelector("h1").textContent).toEqual("");
				});
				fixture.detectChanges();
				expect(compiled.querySelector("h1").textContent).toContain(comp.title);
			}));

			it("1.3.Response Processing", fakeAsync(() -> {
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
					DebugElement button = fixture.debugElement.query(By.css("button"));
					boolean yes = true;

					button.triggerEventHandler("click", null);

					RequestMatch rm = new RequestMatch();
					rm.method = "POST";
					TestRequest req = httpMock.expectOne(rm);

					req.flush(response);

					fixture.detectChanges();

					tick();

					expect(comp.responseArray.length).toEqual(1);

					fixture.detectChanges();

				}

			}));
		}
	}

	public static void main(String[] args) {

		describe("Command Component Test1", new FirstDescribeContext());

		describe("Command Component Test2", () -> {
			it("2.1", (done) -> {
				done.$apply();
			});

		});
	}
}
