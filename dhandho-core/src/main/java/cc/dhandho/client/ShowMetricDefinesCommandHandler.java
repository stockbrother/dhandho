package cc.dhandho.client;

import java.util.Comparator;
import java.util.function.Consumer;
import java.util.stream.Stream;

import cc.dhandho.client.MetricDefines.MetricDefine;
import cc.dhandho.commons.commandline.CommandLineWriter;
import cc.dhandho.report.JsonMetricSqlLinkQueryBuilder;

/**
 * @see JsonMetricSqlLinkQueryBuilder
 * @author wu
 *
 */
public class ShowMetricDefinesCommandHandler extends DhandhoCommandHandler {

	@Override
	public void execute(CommandContext cc) {
		MetricDefines msd = cc.getConsole().getMetricsDefine();

		Stream<MetricDefines.MetricDefine> mds = msd.getMetricDefineStream();
		CommandLineWriter w = cc.getConsole().peekWriter();

		mds.sorted(new Comparator<MetricDefine>() {

			@Override
			public int compare(MetricDefine o1, MetricDefine o2) {
				return o1.getId() - o2.getId();
			}
		}).forEach(new Consumer<MetricDefines.MetricDefine>() {

			@Override
			public void accept(MetricDefine t) {
				w.write(t.getId());
				w.write("\t");
				w.write(t.getName());
				w.write("\t=\t(");
				w.write(t.getOperator().getSign());
				w.write(",");
				t.getChildMetricList().stream().forEach(new Consumer<String>() {

					@Override
					public void accept(String t) {
						w.write(t);
						w.write(",");
					}
				});
				w.write(") \tin group:\t");
				w.write(t.getGroup().name());
				w.writeLine();

			}
		});

	}

}
