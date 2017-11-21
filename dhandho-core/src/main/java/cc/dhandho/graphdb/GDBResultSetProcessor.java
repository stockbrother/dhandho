package cc.dhandho.graphdb;

import com.orientechnologies.orient.core.sql.executor.OResultSet;

public interface GDBResultSetProcessor<T> {
    public T process(OResultSet rst);
}
