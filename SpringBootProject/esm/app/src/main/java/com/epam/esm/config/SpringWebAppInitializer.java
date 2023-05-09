package com.epam.esm.config;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

public class SpringWebAppInitializer implements WebApplicationInitializer {

  private static final String SPRING_DISPATCHER = "SpringDispatcher";
  private static final String ROOT_URL_MAPPING = "/";
  private static final String ENCODING_FILTER = "encodingFilter";
  private static final String ENCODING_PARAMETER = "encoding";
  private static final String UTF_VALUE = "UTF-8";
  private static final String FORCE_ENCODING_PARAMETER = "forceEncoding";
  private static final String TRUE_VALUE = "true";
  private static final String ALL_URLS_PATTERN = "/*";

  /**
   * Performs initial configuration
   *
   * @param servletContext
   * @throws ServletException
   */
  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
    AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
    appContext.register(ApplicationConfig.class);

    ServletRegistration.Dynamic dispatcher = servletContext.addServlet(SPRING_DISPATCHER,
        new DispatcherServlet(appContext));
    dispatcher.setLoadOnStartup(1);
    dispatcher.addMapping(ROOT_URL_MAPPING);

    FilterRegistration.Dynamic fr = servletContext
        .addFilter(ENCODING_FILTER, CharacterEncodingFilter.class);

    fr.setInitParameter(ENCODING_PARAMETER, UTF_VALUE);
    fr.setInitParameter(FORCE_ENCODING_PARAMETER, TRUE_VALUE);
    fr.addMappingForUrlPatterns(null, true, ALL_URLS_PATTERN);
  }

}
