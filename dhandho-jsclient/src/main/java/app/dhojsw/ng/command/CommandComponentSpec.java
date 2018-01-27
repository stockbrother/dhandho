package app.dhojsw.ng.command;

import app.dhojsw.ng.testing.Jasmine;

import static app.dhojsw.ng.testing.Jasmine.describe;
import static app.dhojsw.ng.testing.Jasmine.it;
import static def.dhojsw.jsnative.Globals.angular_async;
import static def.dhojsw.jsnative.Globals.angular_newTestModuleMetadataBuilder;

import def.angular.common_http_testing.HttpClientTestingModule;
import def.angular.common_http_testing.HttpTestingController;
import def.angular.core.DebugElement;
import def.angular.core_testing.ComponentFixture;
import def.angular.core_testing.TestBed;
import def.angular.forms.FormsModule;

public class CommandComponentSpec {
	public static class DescribeContext {
		HttpTestingController httpMock;

		CommandComponent comp;

		ComponentFixture<CommandComponent> fixture;

		DebugElement de;

		Object ne;

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
				Object compiled = fixture.debugElement.nativeElement;
				it( "Title should not be binded until fixture.detectChanges()", () -> {
		            //Jasmine.expect( compiled.querySelector( 'h1' ).textContent ).toEqual( '' );
		        } );
				fixture.detectChanges();
				//Jasmine.expect( compiled.querySelector( "h1" ).textContent ).toContain( comp.title );
			}));
			

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
