package app.dhojsw.ng.command;

import static app.dhojsw.ng.testing.Jasmine.beforeEach;
import static app.dhojsw.ng.testing.Jasmine.describe;
import static app.dhojsw.ng.testing.Jasmine.it;

import def.angular.core_testing.TestBed;
import def.angular.core_testing.TestModuleMetadata;

public class CommandComponentSpec {

	public static void main(String[] args) {

		describe("My First Test", new Runnable() {
			@Override
			public void run() {
				beforeEach(new Runnable() {

					@Override
					public void run() {
						TestModuleMetadata meta = new TestModuleMetadata(); 
						TestBed.configureTestingModule(meta).compileComponents();
					}
				});
				it("1.1", new Runnable() {
					@Override
					public void run() {

					}
				});

			}
		});

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
