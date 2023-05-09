package com.epam.esm.dao.mapper;

import com.epam.esm.model.Certificate;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

/**
 * Database RowMapper for CertificateEntity model
 */
@Slf4j
@Component
public class CertificateRowMapper implements RowMapper<Certificate> {

  private static final String CERTIFICATE_ID_PARAMETER = "id";
  private static final String CERTIFICATE_NAME_PARAMETER = "name";
  private static final String CERTIFICATE_DESCRIPTION_PARAMETER = "description";
  private static final String CERTIFICATE_PRICE_PARAMETER = "price";
  private static final String CERTIFICATE_CREATE_DAY_PARAMETER = "create_day";
  private static final String CERTIFICATE_LAST_UPDATE_DATE_PARAMETER = "last_update_date";
  private static final String CERTIFICATE_DURATION_PARAMETER = "duration";
  private static final String LOG_MAP_ROW_START = "mapRow() called";
  private static final String LOG_MAP_ROW_END = "mapRow() return: ";

  /**
   * Converts provided resultSet and returns CertificateEntity object.
   *
   * @param rs
   * @param rowNum
   * @return certificate
   * @throws SQLException
   */
  @Override
  public Certificate mapRow(ResultSet rs, int rowNum) throws SQLException {
    log.debug(LOG_MAP_ROW_START);
    Certificate certificate = new Certificate();
    certificate.setId(rs.getInt(CERTIFICATE_ID_PARAMETER));
    certificate.setName(rs.getString(CERTIFICATE_NAME_PARAMETER));
    certificate.setDescription(rs.getString(CERTIFICATE_DESCRIPTION_PARAMETER));
    certificate.setPrice(rs.getDouble(CERTIFICATE_PRICE_PARAMETER));

    String createDayString = rs.getString(CERTIFICATE_CREATE_DAY_PARAMETER);
    certificate.setCreateDay(LocalDateTime.parse(createDayString));

    String updateDayString = rs.getString(CERTIFICATE_LAST_UPDATE_DATE_PARAMETER);
    certificate.setLastUpdateDate(LocalDateTime.parse(updateDayString));
    certificate.setDuration(rs.getInt(CERTIFICATE_DURATION_PARAMETER));

    certificate.setTags(new HashSet<>());
    log.debug(LOG_MAP_ROW_END + certificate);
    return certificate;
  }
}
