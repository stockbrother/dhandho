package app.dhojsw.ng.testing.util;

import java.util.function.Consumer;

import def.jasmine.DoneFn;
import def.jasmine.jasmine.Matchers;

public class UnitDescriber implements Runnable {

	protected String desc;

	public UnitDescriber(String desc) {
		this.desc = desc;
	}

	public <T> Consumer<T> async(Runnable run) {
		return Angular_.async_(run);
	}

	public <T> Consumer<T> fakeAsync(Runnable run) {
		return Angular_.fakeAsync_(run);
	}

	public void tick() {
		Angular_.tick_();
	}

	/**
	 * 
	 */
	public void describe() {
		
		Jasmine_.describe_(this.desc, () -> {
			this.run();
		});
	}

	protected UnitDescriber itFakeAsync(String desc, Runnable func) {
		Jasmine_.it_(desc, Angular_.fakeAsync_(func));
		return this;
	}

	protected UnitDescriber itAsync(String desc, Runnable func) {
		Jasmine_.it_(desc, Angular_.async_(func));
		return this;
	}

	protected UnitDescriber it(String desc, Consumer<DoneFn> func) {
		Jasmine_.it_(desc, func);
		return this;
	}

	protected UnitDescriber beforeEach(Runnable run) {
		Jasmine_.beforeEach_(run);
		return this;
	}

	public <T> Matchers<T> expect(T obj) {
		return Jasmine_.expect_(obj);
	}

	@Override
	public void run() {

	}

}
