package cc.dhandho.graphdb;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

public class GDBTemplate {


    public <T> T executeQuery(ODatabaseSession session, String sql, Object[] args, GDBResultSetProcessor<T> resultProcessor){
        OResultSet rst = session.query(sql,args);
        return resultProcessor.process(rst);
    }

}
