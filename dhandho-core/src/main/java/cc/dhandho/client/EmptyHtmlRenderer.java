package cc.dhandho.client;

public class EmptyHtmlRenderer implements HtmlRenderer {

	@Override
	public void showHtml(String html) {
		System.out.println(html);
	}

	@Override
	public void showHtml(StringBuilder html) {
		showHtml(html.toString());
	}

}
