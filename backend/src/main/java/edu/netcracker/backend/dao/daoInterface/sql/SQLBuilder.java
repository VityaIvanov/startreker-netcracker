package edu.netcracker.backend.dao.daoInterface.sql;

public interface SQLBuilder {

    String assembleInsertSql();
    String assembleUpdateSql();
    String assembleDeleteSql();
    String assembleExistsSql();
    String assembleSelectSql();
}
