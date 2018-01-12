package cc.dhandho.test.jsweet;
import static jsweet.util.Lang.$export;
public class JsView {
	
	public JsView() {
	}
	
	public void show() {
		$export("JsView_show_Called", "yes");
		$export("showX", "JsView");
	}
	
}
