package cc.dhandho;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import cc.dhandho.graphdb.GDBResultSetProcessor;
import cc.dhandho.graphdb.GDBTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DbAliasInfos extends AbstractAliasInfos{

    private static final Logger LOG = LoggerFactory.getLogger(DbAliasInfos.class);

    public void initialize(ODatabaseSession con, GDBTemplate t) {
        this.updateCache(con, t);
    }

    private void updateCache(ODatabaseSession con, GDBTemplate t) {
        LOG.info("load/reload alias information from db.");
        this.reportAliasColumnMap.clear();
        this.reportAliasListMap.clear();
        String sql = "select reportType,aliasName,columnIndex from AliasInfo";
        t.executeQuery(con, sql, new Object[]{}, new GDBResultSetProcessor<Object>() {

            @Override
            public Object process(OResultSet rset) {

                while (rset.hasNext()) {
                    OResult rs = rset.next();
                    String reportType = rs.getProperty("reportType");
                    Map<String, Integer> tc = reportAliasColumnMap.get(reportType);

                    if (tc == null) {
                        tc = new HashMap<>();
                        reportAliasColumnMap.put(reportType, tc);
                    }
                    String aliasName = rs.getProperty("aliasName");
                    Integer columnIndex = rs.getProperty("columnIndex");
                    tc.put(aliasName, columnIndex);
                    //
                    List<String> aliasList = reportAliasListMap.get(reportType);
                    if (aliasList == null) {
                        aliasList = new ArrayList<>();
                        reportAliasListMap.put(reportType, aliasList);
                    }
                    aliasList.add(aliasName);

                }

                return null;
            }
        });
        LOG.info("alias information loaded.");

    }
    protected int getMaxColumIndex(ODatabaseSession con, GDBTemplate t, String reportType) {
        String sql = "select max(columnIndex) from AliasInfo where reportType=?";

        return t.executeQuery(con, sql, new Object[]{reportType}, new GDBResultSetProcessor<Integer>() {

            @Override
            public Integer process(OResultSet rs) {

                while (rs.hasNext()) {
                    OResult rst = rs.next();
                    return rst.getProperty("max(columnIndex)");
                }
                return 0;
            }
        });

    }
    
    public List<Integer> getOrCreateColumnIndexByAliasList(ODatabaseSession con, GDBTemplate t, final String reportType,
                                                           List<String> aliasList) {
        Map<String, Integer> aliasMap = reportAliasColumnMap.get(reportType);

        List<Integer> rt = new ArrayList<>();
        for (final String alias : aliasList) {
            Integer columnIndex = null;
            if (aliasMap != null) {
                columnIndex = aliasMap.get(alias);
            }

            if (columnIndex == null) {
            	this.addColumnIndex(reportType, alias, con, t);
            }

            rt.add(columnIndex);
        }

        return rt;
    }
    
    @Override
    public Integer addColumnIndex(String reportType, String alias) {
    	throw new RtException("not supported.");
    }
    		
    public Integer addColumnIndex(String reportType, String alias, ODatabaseSession con, GDBTemplate t) {
    	int tmpIndex = getMaxColumIndex(con, t, reportType) + 1;
        OClass aliasClazz = con.getClass("AliasInfo");
        OVertex ver = con.newVertex(aliasClazz);
        ver.setProperty("reportType", reportType);
        ver.setProperty("aliasName", alias);
        ver.setProperty("columnIndex", tmpIndex);
        ver.save();
        updateCache(con, t);
        return tmpIndex;
    }

}
