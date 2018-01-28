package def.jasmine.jasmine;

import jsweet.lang.Interface;

@Interface
public abstract class Matchers<T> {

	public abstract boolean toBe(Object expected);

	public abstract boolean toEqual(Object expected);

	public abstract boolean toMatch(String string);

	public abstract boolean toBeDefined();

	public abstract boolean toBeUndefined();

	public abstract boolean toBeNull();

	public abstract boolean toBeNaN();

	public abstract boolean toBeTruthy();

	public abstract boolean toBeFalsy();

	public abstract boolean toHaveBeenCalled();

	public abstract boolean toHaveBeenCalledBefore(Spy spy);

	public abstract boolean toHaveBeenCalledWith(Object[] params);

	public abstract boolean toHaveBeenCalledTimes(Number number);

	public abstract boolean toContain(Object any);

	public abstract boolean toBeLessThan(Number number);

	public abstract boolean toBeLessThanOrEqual(Number number);

	public abstract boolean toBeGreaterThan(Number number);

	public abstract boolean toBeGreaterThanOrEqual(Number number);

	public abstract boolean toThrow();

	//
	public abstract boolean toBe(Object expected, Object failOutput);

	public abstract boolean toEqual(Object expected, Object failOutput);

	public abstract boolean toMatch(String string, Object failOutput);

	public abstract boolean toBeDefined(Object failOutput);

	public abstract boolean toBeUndefined(Object failOutput);

	public abstract boolean toBeNull(Object failOutput);

	public abstract boolean toBeNaN(Object failOutput);

	public abstract boolean toBeTruthy(Object failOutput);

	public abstract boolean toBeFalsy(Object failOutput);

	public abstract boolean toContain(Object any, Object failOutput);

	public abstract boolean toBeLessThan(Number number, Object failOutput);

	public abstract boolean toBeLessThanOrEqual(Number number, Object failOutput);

	public abstract boolean toBeGreaterThan(Number number, Object failOutput);

	public abstract boolean toBeGreaterThanOrEqual(Number number, Object failOutput);

	public abstract boolean toThrow(Object failOutput);
}
