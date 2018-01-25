package app.dhojsw.ng.my;

import app.dhojsw.ng.DhoTester;

public class MyComponentSpec {

	public static void main(String[] args) {
		DhoTester tester = new DhoTester();
		
		tester.jasmine_describe("My First Test", new Runnable() {
			@Override
			public void run() {
			}
		});
	}
}
