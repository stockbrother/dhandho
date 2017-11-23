package cc.dhandho.gwt.client.app.view;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * Show svg chart.
 * 
 * @author wu
 *
 */
public class SvgChart extends FlowPanel {

	public SvgChart() {
	}

	public void setSvg(int width, int height, String svg) {
		this.getElement().getStyle().setProperty("width", width + "px");
		this.getElement().getStyle().setProperty("height", height + "px");
		this.getElement().setInnerHTML(svg);

	}

	public void unsetSvg(String msg) {
		this.getElement().setInnerHTML(msg);
	}

}
