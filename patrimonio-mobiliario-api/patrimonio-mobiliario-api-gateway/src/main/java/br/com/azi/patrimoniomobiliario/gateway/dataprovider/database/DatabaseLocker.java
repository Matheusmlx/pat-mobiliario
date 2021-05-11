package br.com.azi.patrimoniomobiliario.gateway.dataprovider.database;

import br.com.azi.patrimoniomobiliario.gateway.dataprovider.database.exception.DatabaseConnectionNotFoundException;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.database.exception.DatabaseVendorNotSupported;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Table;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class DatabaseLocker {

    private static final String MSSQL_VENDOR = "Microsoft SQL Server";
    private static final String POSTGRES_VENDOR = "PostgreSQL";

    private final DataSource dataSource;

    private final JdbcTemplateFactory jdbcTemplateFactory;

    @Transactional(propagation = Propagation.MANDATORY, transactionManager = "transactionManager")
    public void lockTableByClass(Class entityClass) {
        JdbcTemplate jdbcTemplate = jdbcTemplateFactory.getJdbcTemplate(dataSource);
        jdbcTemplate.execute(getStatement(entityClass));
    }

    private String getStatement(Class entityClass) {
        final String databaseProductName = getDatabaseProductName();
        final String tableName = getFullTableNameFromClass(entityClass);

        if (isSqlServerDatabase(databaseProductName)) {
            return "SELECT 1 FROM " + tableName + " WITH (TABLOCKX, HOLDLOCK)";
        } else if (isPostgresDatabase(databaseProductName)) {
            return "LOCK TABLE " + tableName + " IN ACCESS EXCLUSIVE MODE";
        }

        throw new DatabaseVendorNotSupported();
    }

    private String getDatabaseProductName() {
        try (Connection connection = dataSource.getConnection()) {
            return connection.getMetaData().getDatabaseProductName();
        } catch (SQLException e) {
            throw new DatabaseConnectionNotFoundException(e);
        }
    }

    private String getFullTableNameFromClass(Class entityClass) {
        Table table = (Table) entityClass.getAnnotation(Table.class);//NOSONAR
        return table.schema() + "." + table.name();
    }

    private boolean isSqlServerDatabase(String databaseProductName) {
        return MSSQL_VENDOR.equals(databaseProductName);
    }

    private boolean isPostgresDatabase(String databaseProductName) {
        return POSTGRES_VENDOR.equals(databaseProductName);
    }
}
