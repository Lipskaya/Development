package com.epam.esm.dao;

import com.epam.esm.exception.ErrorCode;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.dao.mapper.CertificateRowMapper;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Sort;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 * Database implementation of CertificateDao interface
 */
@Repository
@Slf4j
public class CertificateDaoImpl implements CertificateDao {

  private static final String SQL_TOTAL_CERTIFICATES = "SELECT count(*) FROM gift_certificate";
  private static final String SQL_GET_CERTIFICATE_BY_ID = "SELECT id,name,description,price,create_day,last_update_date,duration FROM gift_certificate WHERE id = ?";
  private static final String SQL_UPDATE_CERTIFICATE = "UPDATE gift_certificate SET name = ?,description = ?, price = ?, last_update_date = ?, duration = ? WHERE ID = ?";
  private static final String SQL_DELETE_CERTIFICATE = "DELETE FROM gift_certificate WHERE id = ?";
  private static final String CERTIFICATE_NOT_FOUND_START_MESSAGE = "CertificateEntity with ID = ";
  private static final String NOT_FOUND_END_MESSAGE = " not found";
  private static final String CERTIFICATE_NAME_PARAMETER = "name";
  private static final String CERTIFICATE_DESCRIPTION_PARAMETER = "description";
  private static final String CERTIFICATE_PRICE_PARAMETER = "price";
  private static final String CERTIFICATE_CREATE_DAY_PARAMETER = "create_day";
  private static final String CERTIFICATE_LAST_UPDATE_DATE_PARAMETER = "last_update_date";
  private static final String CERTIFICATE_DURATION_PARAMETER = "duration";
  private static final String LOG_DELETE_START = "delete() called for certificate";
  private static final String LOG_DELETE_END = "delete() end for certificate: ";
  private static final String LOG_UPDATE_START = "update() called";
  private static final String LOG_UPDATE_END = "update() return: ";
  private static final String LOG_INSERT_START = "insert() called";
  private static final String LOG_INSERT_END = "insert() return: ";
  private static final String LOG_GET_BY_ID_START = "getById() called";
  private static final String LOG_GET_BY_ID_END = "getById() return: ";
  private static final String LOG_GET_ALL_START = "getAll() called";
  private static final String LOG_GET_ALL_END = "getAll() return: ";
  private static final String LOG_GET_TOTAL_ITEMS_START = "getTotalItems() called";
  private static final String LOG_GET_TOTAL_ITEMS_END = "getTotalItems() return: ";

  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert simpleJdbcInsert;
  private final CertificateRowMapper certificateRowMapper;
  private final QueryBuilder queryBuilder;

  @Autowired
  public CertificateDaoImpl(JdbcTemplate jdbcTemplate,
      @Qualifier("simpleJdbcCertificateInsert")
      SimpleJdbcInsert simpleJdbcInsert,
      CertificateRowMapper certificateRowMapper, QueryBuilder queryBuilder) {
    this.jdbcTemplate = jdbcTemplate;
    this.simpleJdbcInsert = simpleJdbcInsert;
    this.certificateRowMapper = certificateRowMapper;
    this.queryBuilder = queryBuilder;
  }

  /**
   * Retrieves the certificate from database by provided id TagEntity list will be empty. Use TagDao to
   * retrieve certificate tag list.
   *
   * @param id
   * @return certificate
   */
  @Override
  public Certificate getById(Integer id) {
    log.debug(LOG_GET_BY_ID_START);
    try {
      Certificate certificate = jdbcTemplate
          .queryForObject(SQL_GET_CERTIFICATE_BY_ID, certificateRowMapper, id);
      log.debug(LOG_GET_BY_ID_END + certificate);
      return certificate;
    } catch (EmptyResultDataAccessException e) {
      log.error(CERTIFICATE_NOT_FOUND_START_MESSAGE, e);
      throw new NotFoundException(CERTIFICATE_NOT_FOUND_START_MESSAGE + id + NOT_FOUND_END_MESSAGE,
          ErrorCode.ERROR_CERTIFICATE);
    }
  }

  /**
   * Retrieves list of all CertificateEntity from database.
   *
   * @param sortRequest - parameters to perform pagination and search
   * @return list of all Certificates
   */
  @Override
  public List<Certificate> getAll(Sort sortRequest) {
    log.debug(LOG_GET_ALL_START + sortRequest);
    String sql = queryBuilder.buildGetAllCertificatesQuery(sortRequest);
    List<Certificate> certificates = jdbcTemplate
        .query(sql, certificateRowMapper, sortRequest.getPaginationLimit(),
            (sortRequest.getPaginationOffset() - 1) * sortRequest.getPaginationLimit());
    log.debug(LOG_GET_ALL_END + certificates);
    return certificates;
  }

  /**
   * Saves a certificate in database
   *
   * @param certificate
   * @return certificate
   */
  @Override
  public Certificate insert(Certificate certificate) {
    log.debug(LOG_INSERT_START);
    Map<String, Object> parameters = new HashMap<>(1);
    parameters.put(CERTIFICATE_NAME_PARAMETER, certificate.getName());
    parameters.put(CERTIFICATE_DESCRIPTION_PARAMETER, certificate.getDescription());
    parameters.put(CERTIFICATE_PRICE_PARAMETER, certificate.getPrice());
    parameters.put(CERTIFICATE_CREATE_DAY_PARAMETER, certificate.getCreateDay().toString());
    parameters
        .put(CERTIFICATE_LAST_UPDATE_DATE_PARAMETER, certificate.getLastUpdateDate().toString());
    parameters.put(CERTIFICATE_DURATION_PARAMETER, certificate.getDuration());
    Number newId = simpleJdbcInsert.executeAndReturnKey(parameters);
    certificate.setId(newId.intValue());
    log.debug(LOG_INSERT_END + certificate);
    return certificate;
  }

  /**
   * Updates a certificate in database
   *
   * @param newCertificate
   * @return
   */
  @Override
  public Certificate update(Certificate newCertificate) {
    log.debug(LOG_UPDATE_START);
    Certificate oldCertificate = getById(newCertificate.getId());

    String name = oldCertificate.getName();
    String description = oldCertificate.getDescription();
    double price = oldCertificate.getPrice();
    Integer duration = oldCertificate.getDuration();

    if (newCertificate.getName() != null) {
      name = newCertificate.getName();
    }
    if (newCertificate.getDescription() != null) {
      description = newCertificate.getDescription();
    }
    if (newCertificate.getPrice() != null) {
      price = newCertificate.getPrice();
    }
    if (newCertificate.getDuration() != null) {
      duration = newCertificate.getDuration();
    }

    int updatedCertificateRows = jdbcTemplate
        .update(SQL_UPDATE_CERTIFICATE, name, description,
            price, newCertificate.getLastUpdateDate().toString(), duration,
            newCertificate.getId());

    if (updatedCertificateRows == 0) {
      log.error(CERTIFICATE_NOT_FOUND_START_MESSAGE);
      throw new NotFoundException(
          CERTIFICATE_NOT_FOUND_START_MESSAGE + newCertificate.getId() + NOT_FOUND_END_MESSAGE,
          ErrorCode.ERROR_CERTIFICATE);
    }
    log.debug(LOG_UPDATE_END + newCertificate);
    return newCertificate;
  }


  /**
   * Deletes a certificate by id
   *
   * @param certificate
   */
  @Override
  public void delete(Certificate certificate) {
    log.debug(LOG_DELETE_START);
    jdbcTemplate.update(SQL_DELETE_CERTIFICATE, certificate.getId());
    log.debug(LOG_DELETE_END + certificate);
  }


  /**
   * Get total number of certificates
   *
   * @return int number of certificates
   */
  @Override
  public int getTotalItems() {
    log.debug(LOG_GET_TOTAL_ITEMS_START);
    int total = jdbcTemplate.queryForObject(SQL_TOTAL_CERTIFICATES, Integer.class);
    log.debug(LOG_GET_TOTAL_ITEMS_END + total);
    return total;
  }
}
