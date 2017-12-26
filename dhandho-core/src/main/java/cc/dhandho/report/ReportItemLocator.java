package cc.dhandho.report;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import cc.dhandho.util.Visitor;

/**
 * 1-1 ReportItemEntity
 * 
 * @see ReportItemLocators
 * @author Wu
 *
 */
public class ReportItemLocator {
	private ReportItemLocator parent;
	private String key;
	private List<ReportItemLocator> childList = new ArrayList<>();
	private int order;

	public ReportItemLocator(ReportItemLocator parent, String key) {
		this.parent = parent;
		this.key = key;
	}

	public static ReportItemLocator newInstance(String key) {
		return new ReportItemLocator(null, key);
	}

	public ReportItemLocator newChild(String key) {
		ReportItemLocator rt = ReportItemLocator.newInstance(key);
		this.add(rt);
		return rt;
	}

	public ReportItemLocator add(ReportItemLocator child) {
		if (child.parent != null) {
			throw new RuntimeException("child has parent:" + child.parent);
		}
		child.parent = this;
		this.childList.add(child);
		return this;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public ReportItemLocator getParent() {
		return parent;
	}

	public boolean isRoot() {
		return this.parent == null;
	}

	public void write(Writer writer) throws IOException {
		write(0, writer);
	}

	private void write(int depth, Writer writer) throws IOException {
		for (int i = 0; i < depth; i++) {
			writer.write(",");
		}
		writer.write(this.key);
		writer.write("\n");
		for (ReportItemLocator child : this.childList) {
			child.write(depth + 1, writer);
		}
	}

	public List<ReportItemLocator> getChildList() {
		return this.childList;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int getOrder() {
		return order;
	}

	public void forEach(Visitor<ReportItemLocator> vis, boolean includeThis) {
		if (includeThis) {
			vis.visit(this);
		}
		for (ReportItemLocator cI : this.childList) {
			cI.forEach(vis, true);//
		}
	}

	public void addAllToList(List<ReportItemLocator> list, boolean addThis) {

		if (addThis) {
			list.add(this);
		}
		for (ReportItemLocator cI : this.childList) {
			cI.addAllToList(list, true);//
		}
	}

}
