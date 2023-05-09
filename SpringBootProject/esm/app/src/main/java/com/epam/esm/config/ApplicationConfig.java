package com.epam.esm.config;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@EnableWebMvc
@EnableOpenApi
@Configuration
@PropertySource("classpath:${ENV_KEY}.properties")
@ComponentScan("com.epam.esm")
@EnableTransactionManagement

public class ApplicationConfig {

  @Value("${db.driver}")
  private String driverClassName;
  @Value("${db.url}")
  private String jdbcUrl;
  @Value("${db.user}")
  private String userName;
  @Value("${db.password}")
  private String password;
  private static final String TAG_TABLE_NAME = "tag";
  private static final String ID_GENERATED_KEY_COLUMNS = "id";
  private static final String CERTIFICATE_TABLE_NAME = "gift_certificate";
  private static final String ORDER_TABLE_NAME = "`order`"; ///??? TODO: consult with mentor. OrderEntity is a Keyword

  /**
   * Create HikariDataSource connection pool
   *
   * @return DataSource
   */
  @Bean
  public DataSource mysqlDataSource() {

    HikariDataSource ds = new HikariDataSource();
    ds.setMaximumPoolSize(10);
    ds.setDriverClassName(driverClassName);
    ds.setJdbcUrl(jdbcUrl);
    ds.setUsername(userName);
    ds.setPassword(password);
    return ds;
  }

  /**
   * Create jdbcTemplate. Example of dependency injection through constructor.
   *
   * @param dataSource
   * @return jdbcTemplate
   */
  @Bean
  public JdbcTemplate jdbcTemplate(DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }

  /**
   * Create SimpleJdbcInsert for TagEntity
   *
   * @param dataSource
   * @return new SimpleJdbcInsert
   */
  @Bean
  public SimpleJdbcInsert simpleJdbcTagInsert(DataSource dataSource) {
    return new SimpleJdbcInsert(dataSource)
        .withTableName(TAG_TABLE_NAME).usingGeneratedKeyColumns(ID_GENERATED_KEY_COLUMNS);
  }

   /**
   * Create SimpleJdbcInsert for CertificateEntity
   *
   * @param dataSource
   * @return new SimpleJdbcInsert
   */
  @Bean
  public SimpleJdbcInsert simpleJdbcCertificateInsert(DataSource dataSource) {
    return new SimpleJdbcInsert(dataSource)
        .withTableName(CERTIFICATE_TABLE_NAME)
        .usingGeneratedKeyColumns(ID_GENERATED_KEY_COLUMNS);
  }

  /**
   * Create SimpleJdbcInsert for OrderEntity
   *
   * @param dataSource
   * @return new SimpleJdbcInsert
   */
  @Bean
  public SimpleJdbcInsert simpleJdbcOrderInsert(DataSource dataSource) {
    return new SimpleJdbcInsert(dataSource)
        .withTableName(ORDER_TABLE_NAME)
        .usingGeneratedKeyColumns(ID_GENERATED_KEY_COLUMNS);
  }


  /**
   * Creates PlatformTransactionManager bean
   *
   * @param dataSource
   * @return DataSourceTransactionManager
   */
  @Bean
  public PlatformTransactionManager transactionManager(DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
  }

  @Bean
  public Docket api(){
    return new Docket(DocumentationType.OAS_30)
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.any())
        .build()
        .apiInfo(apiInfo());
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("TITLE")
        .description("DESCRIPTION")
        .version("VERSION")
        .termsOfServiceUrl("http://terms-of-services.url")
        .license("LICENSE")
        .licenseUrl("http://url-to-license.com")
        .build();
  }
}
