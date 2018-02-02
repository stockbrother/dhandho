package app.dhojsw.ng.stock;

import app.dhojsw.ng.util.ComponentUnitDescriber;
import def.angular.common_http_testing.RequestMatch;
import def.angular.common_http_testing.TestRequest;
import def.angular.core.DebugElement;
import def.angular.platform_browser.By;
import def.js.JSON;

public class StockListComponentSpec extends ComponentUnitDescriber<StockListComponent> {

	public StockListComponentSpec() {
		super(StockListComponent.class);
	}

	@Override
	public void run() {
		this.beforeEach(new BeforeEach<StockListComponent>(this));
		this.itFakeAsync("Test click refresh stock list.", () -> {
			expect(comp).toBeTruthy();
			String jsonS = ("{\r\n" + //
					"            'type': 'table',\r\n" + //
					"            'columnNames': ['corpId', 'corpName'],\r\n"
							+ //
					"            'rowArray': [\r\n" + //
					"                ['000001', 'Name1'],\r\n" + //
					"                ['000002', 'Name2'],\r\n" + //
					"                ['000003', 'Name3'],\r\n" + //
					"                ['000004', 'Name4']\r\n" + //
					"            ]\r\n" + //
					"        }").replace('\'', '\"');
					Object response = JSON.parse(jsonS);

					expect(comp.responseArray.length).toEqual(0);
					{
						
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

		});
	}

	public static void main(String[] args) {
		new StockListComponentSpec().describe();
	}
}
