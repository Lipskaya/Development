package com.epam.esm.dao;

import com.epam.esm.model.Sort;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Class which, create required sql queries
 */
@Component
@Slf4j
public class QueryBuilder {

  private static final String SQL_GET_CERTIFICATES_PART_ONE = "SELECT id,name,description,price,create_day,last_update_date, duration FROM gift_certificate";
  private static final String SQL_DESCRIPTION_SEARCH = "  WHERE description LIKE '%";
  private static final String SQL_NAME_SEARCH = "%' OR name LIKE '%";
  private static final String SQL_SEARCH_PARAMETER = "%'";
  private static final String SQL_ORDER_BY = " ORDER BY '";
  private static final String SQL_SPACE = "' ";
  private static final String SQL_TAG_NAME_START =
      "SELECT id,name,description,price,create_day,last_update_date, duration "
          + "FROM (SELECT gift_certificate.id, gift_certificate.name,"
          + "gift_certificate.description,gift_certificate.create_day,\n"
          + "gift_certificate.last_update_date,gift_certificate.price,"
          + "gift_certificate.duration, tag.name AS tag\n"
          + "FROM gift_certificate \n"
          + "JOIN\n"
          + "gift_tag ON gift_certificate.id=gift_tag.gift_certificate_id\n"
          + "JOIN\n"
          + "tag ON tag.id = gift_tag.tag_id  \n"
          + "WHERE tag.name IN (";
  private static final String SQL_TAG_NAME_END = ")) AS K GROUP BY K.id HAVING COUNT(DISTINCT K.tag)=";
  private static final String SQL_PAGINATION = " LIMIT ? OFFSET ?";
  private static final String LOG_SQL_MESSAGE = "executing SQL: ";

  private static final String SQL_GET_ALL_ORDERS = "SELECT id, date, cost, user_id, gift_certificate_id FROM spring_boot_certificates.`order` LIMIT ? OFFSET ?";
  private static final String SQL_GET_USER_ORDERS = "SELECT id, date, cost, user_id, gift_certificate_id FROM spring_boot_certificates.`order` where user_id=? LIMIT ? OFFSET ?";

  /**
   * Returns sql query to retrieve all certificate
   *
   * @param sortRequest - query parameters for pagination, search and sorting
   * @return String sql
   */
  public String buildGetAllCertificatesQuery(Sort sortRequest) {
    String sql;
    if (sortRequest.getTags() != null) {
      sql = SQL_TAG_NAME_START;

      String tagsSql = sortRequest.getTags().stream()
          .collect(Collectors.joining("','","'" , "'"));

      sql = sql + tagsSql + SQL_TAG_NAME_END + sortRequest.getTags().size();
    } else {
      sql = SQL_GET_CERTIFICATES_PART_ONE;
    }
    if (sortRequest.getSearch() != null && !sortRequest.getSearch().trim().isEmpty()) {
      sql = sql + SQL_DESCRIPTION_SEARCH;
      sql = sql + sortRequest.getSearch() + SQL_NAME_SEARCH
          + sortRequest.getSearch() + SQL_SEARCH_PARAMETER;
    }
    sql = sql + SQL_ORDER_BY + sortRequest.getSortField() + SQL_SPACE + sortRequest.getDirection()
        + SQL_PAGINATION;
    log.debug(LOG_SQL_MESSAGE + sql);
    return sql;
  }

  public String buildGetAllOrderQuery(Sort sortRequest) {
    String sql;
    if (sortRequest.getUserId() != null) {
      sql = SQL_GET_USER_ORDERS;
    } else {
      sql = SQL_GET_ALL_ORDERS;
    }
    return sql;
  }
}
