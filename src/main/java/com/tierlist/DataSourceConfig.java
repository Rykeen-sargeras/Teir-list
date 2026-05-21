package com.tierlist;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.net.URI;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() throws Exception {
        String dbUrl = System.getenv("DATABASE_URL");

        if (dbUrl != null && dbUrl.startsWith("postgres")) {
            // Railway gives: postgres://user:pass@host:port/db
            // Convert to JDBC format
            URI uri = new URI(dbUrl.replace("postgres://", "postgresql://"));
            String host = uri.getHost();
            int port = uri.getPort();
            String path = uri.getPath();
            String[] userInfo = uri.getUserInfo().split(":");
            String username = userInfo[0];
            String password = userInfo.length > 1 ? userInfo[1] : "";
            String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + path + "?sslmode=require";

            return DataSourceBuilder.create()
                    .url(jdbcUrl)
                    .username(username)
                    .password(password)
                    .driverClassName("org.postgresql.Driver")
                    .build();
        }

        // Fallback to individual env vars (also set by Railway)
        String host     = System.getenv().getOrDefault("PGHOST", "localhost");
        String port     = System.getenv().getOrDefault("PGPORT", "5432");
        String db       = System.getenv().getOrDefault("PGDATABASE", "tierlist");
        String user     = System.getenv().getOrDefault("PGUSER", "postgres");
        String password = System.getenv().getOrDefault("PGPASSWORD", "");
        String jdbcUrl  = "jdbc:postgresql://" + host + ":" + port + "/" + db + "?sslmode=require";

        return DataSourceBuilder.create()
                .url(jdbcUrl)
                .username(user)
                .password(password)
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}
