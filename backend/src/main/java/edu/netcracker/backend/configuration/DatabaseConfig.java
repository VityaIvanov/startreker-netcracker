package edu.netcracker.backend.configuration;

import lombok.Setter;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;

@Configuration
@ConfigurationProperties(prefix = "database")
@Setter
public class DatabaseConfig {

    @NotNull
    private String serverName;
    @NotNull
    private Integer port;
    @NotNull
    private String database;
    @NotNull
    private String username;
    @NotNull
    private String password;

    @Bean
    @Profile("prod")
    DataSource dataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setServerName(serverName);
        dataSource.setUser(username);
        dataSource.setDatabaseName(database);
        dataSource.setPassword(password);
        dataSource.setPortNumber(port);
        return dataSource;
    }

    @Bean(name = "dataSourceTestH2")
    @Profile("test")
    public DataSource dataSourceTest() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("sa");
        dataSource.setPassword("sa");

        return dataSource;
    }
}