package com.epam.com.dao;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
@ComponentScan({"com.epam.esm.dao", "com.epam.esm.mapper"})
class DaoTestConfig {

  private static final String INIT_DATABASE_SQL = "classpath:init-ddl.sql";
  private static final String FILL_DATABASE_SQL = "classpath:init-dml.sql";
  private static final String TAG_TABLE_NAME = "tag";
  private static final String TAG_GENERATED_KEY_COLUMNS = "id";
  private static final String CERTIFICATE_TABLE_NAME = "gift_certificate";
  private static final String CERTIFICATE_GENERATED_KEY_COLUMNS = "id";

  @Bean
  public DataSource mysqlDataSource() {
    EmbeddedDatabaseBuilder dataSource = new EmbeddedDatabaseBuilder()
        .setType(EmbeddedDatabaseType.H2)
        .addScript(INIT_DATABASE_SQL)
        .addScript(FILL_DATABASE_SQL);
    return dataSource.build();
  }

  @Bean
  public JdbcTemplate jdbcTemplate(DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }

  @Bean
  public SimpleJdbcInsert simpleJdbcTagInsert(DataSource dataSource) {
    return new SimpleJdbcInsert(dataSource)
        .withTableName(TAG_TABLE_NAME).usingGeneratedKeyColumns(TAG_GENERATED_KEY_COLUMNS);
  }

  @Bean
  public SimpleJdbcInsert simpleJdbcCertificateInsert(DataSource dataSource) {
    return new SimpleJdbcInsert(dataSource)
        .withTableName(CERTIFICATE_TABLE_NAME)
        .usingGeneratedKeyColumns(CERTIFICATE_GENERATED_KEY_COLUMNS);
  }
}