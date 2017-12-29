package cc.dhandho.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cc.dhandho.JsonBuilder;

public class MetricQueryJsonBuilder extends JsonBuilder {
	private String corpId;
	private List<Date> dateList = new ArrayList<>();
	
	public MetricQueryJsonBuilder() {
		super();
	}

}
