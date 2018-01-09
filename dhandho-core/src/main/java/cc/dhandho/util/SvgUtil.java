package cc.dhandho.util;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import com.age5k.jcps.JcpsException;
import com.age5k.jcps.framework.provider.Provider;

public class SvgUtil {
	
	public static StringBuilder writeSvg2Html(int width, int height, Provider<String> svg, StringBuilder sb) {
		return SvgUtil.writeSvg2Html(width, height, svg.get(), sb);
	}
	
	public static StringBuilder writeSvg2Html(int width, int height, String svg, StringBuilder sb) {
		StringWriter sWriter = new StringWriter();
		writeSvg2Html(width, height, svg, sWriter);
		sb.append(sWriter.getBuffer());
		return sb;
	}

	public static void writeSvg2Html(int width, int height, String svg, Writer out) {
		try {
//			out.write("<html>\n");
//			out.write("  <head>\n");
//			out.write("    <meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\n");
//			out.write("  </head>\n");
//			out.write("  <body>\n");
			out.write("    <div style=\"width:" + width + "; height:" + height + ";\">\n");
			out.write("    " + svg + "\n");
			out.write("    </div>\n");
//			out.write("  </body>\n");
//			out.write("</html>\n");
			out.flush();
		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		}
	}
}
