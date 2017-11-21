package cc.dhandho.gwt.client.app.view;

import java.util.ArrayList;
import java.util.Stack;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtd3.api.D3;
import com.github.gwtd3.api.core.Selection;
import com.github.gwtd3.api.core.UpdateSelection;
import com.github.gwtd3.api.core.Value;
import com.github.gwtd3.api.functions.BooleanDatumFunction;
import com.github.gwtd3.api.functions.DatumFunction;
import com.github.gwtd3.api.svg.Line;
import com.github.gwtd3.api.svg.Line.InterpolationMode;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.FlowPanel;

public class MyD3LineChart extends FlowPanel {
	
	
	private Selection svg;
	
	protected int width = 450, height = 320;
	
	public MyD3LineChart() {
				
		svg = D3.select(this).append("svg").attr("width", width).attr("height", height).append("g");
		final MyLine line1 = new MyLine(svg,"red");
		final MyLine line2 = new MyLine(svg,"black");
		
		line1.addPoint(true);
		line2.addPoint(true);
		
		line1.addPoint(true);
		line2.addPoint(true);
		
		createButton("Add point", IconType.PLUS_SIGN, new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				line1.addPoint(true);
				line2.addPoint(true);
			}
		});

	}

	private void createButton(String label, IconType icon, ClickHandler clickHandler) {
		Button b = new Button();
		b.setIcon(icon);
		b.setText(label);
		b.addClickHandler(clickHandler);
		this.add(b);

	}


	public static class MyLine {
		private final Stack<Coords> points = new Stack<>() ;
		private Line line;
		private Selection path;
		Selection svg;
		private boolean showPoints = false;
		protected int width = 450, height = 320;
		
		protected InterpolationMode mode = InterpolationMode.LINEAR;
		
		protected double tension;
		
		protected String stroke = "red";

		public MyLine(Selection svg,String stroke) {
			
			this.svg = svg;
			line = D3.svg().line().x(Coords.xAccessor()).y(Coords.yAccessor()).defined(Coords.definedAccessor());
			path = svg.append("path").classed(Bundle.INSTANCE.css().linedemo(), true).attr("stroke", stroke).attr("fill",
					"none");
		}
		

		public void addPoint(boolean defined) {
			
			points.push(new Coords(Random.nextInt(width), Random.nextInt(height), defined));
			update();
		}
		

		public void update( ) {
			
			line.interpolate(mode);
			line.tension(tension);
			path.attr("d", line.generate(points));

			UpdateSelection updateSelection = svg.selectAll("circle").data(showPoints ? points : new ArrayList<Coords>());
			updateSelection.enter().append("circle").attr("cx", new DatumFunction<Double>() {
				@Override
				public Double apply(Element context, Value d, int index) {
					return d.<Coords>as().x;
				}
			}).attr("cy", new DatumFunction<Double>() {
				@Override
				public Double apply(Element context, Value d, int index) {
					return d.<Coords>as().y;
				}
			}).attr("r", 10);
			updateSelection.exit().remove();
		}

		
	}
	interface MyResources extends CssResource {
		String linedemo();
	}

	public interface Bundle extends ClientBundle {
		public static final Bundle INSTANCE = GWT.create(Bundle.class);

		@Source("cc/dhandho/gwt/LineDemo.css")
		public MyResources css();
	}

	private static class Coords {
		double x, y;
		boolean defined;

		public Coords(int x, int y, boolean defined) {
			super();
			this.x = x;
			this.y = y;
			this.defined = defined;

		}

		public Coords(int x, int y) {
			this(x, y, true);
		}

		public static DatumFunction<Double> xAccessor() {
			return new DatumFunction<Double>() {
				@Override
				public Double apply(Element context, Value d, int index) {
					return d.<Coords>as().x;
				}
			};
		}

		public static DatumFunction<Double> yAccessor() {
			return new DatumFunction<Double>() {
				@Override
				public Double apply(Element context, Value d, int index) {
					return d.<Coords>as().y;
				}
			};
		}

		public static BooleanDatumFunction definedAccessor() {
			return new BooleanDatumFunction() {
				@Override
				public boolean apply(Element context, Value d, int index) {
					return d.<Coords>as().defined;
				}
			};

		}

	}
}
