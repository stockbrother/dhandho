package app.dhojsw.ng.testing.util;

import java.util.function.Consumer;

import def.jasmine.DoneFn;
import def.jasmine.jasmine.Matchers;

/**
 * Usage as a example:<br>
 * <code>
 * public class LoggerSpec extends UnitDescriber {
 *
 *	public static void main(String[] args) {
 *		new LoggerSpec("Logger Test").describe();
 *	}
 *
 *	Logger logger;
 *
 *	public LoggerSpec(String desc) {
 *		super(desc);
 *	}
 *
 *	&#64;Override
 *	public void run() {
 *
 *		this.beforeEach(() -> {
 *			TestModuleMetadata meta = new TestModuleMetadata();
 *			meta.providers = new Class<?>[] { Logger.class };
 *			TestBed.configureTestingModule(meta);
 *			TestBed.compileComponents();
 *			logger = TestBed.get(Logger.class);
 *		});
 *
 *		this.it("Assert logger is injected.", (done) -> {
 *			this.expect(this.logger).toBeTruthy("Logger not able being injected.");
 *			this.logger.debug("debug msg");
 *			this.logger.info("info msg");
 *			this.logger.warn("warn msg");
 *			this.logger.error("error msg");
 *			done.$apply();
 *		});
 *	}
 * }
 * </code>
 * 
 * @author Wu
 *
 */
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
