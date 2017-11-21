package cc.dhandho.report;

import com.orientechnologies.orient.core.db.ODatabaseSession;

import cc.dhandho.Quarter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReportQuery {

    public static class Metric {
        private int reportType;
        private String metricName;
    }

    private String corpId;

    private Quarter quarter;

    private List<Metric> metricList = new ArrayList<>();


    public List<Double> query(ODatabaseSession session) {
        //TODO
        List<Double> rt = new ArrayList<>();
        String sql = "select ";
        session.query(sql);
        return rt;

    }

}
