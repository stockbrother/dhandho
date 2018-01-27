package def.angular.core_testing;

import def.angular.core.DebugElement;

public class ComponentFixture<T> {
	public DebugElement debugElement;

	public T componentInstance;

	public Object nativeElement;
	
	public native void detectChanges();
	
}
