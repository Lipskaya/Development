package com.epam.esm.dao;

import com.epam.esm.model.Sort;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Class which, create required sql queries
 */
@Component
public class QueryBuilder {

  private static final Logger LOGGER = Logger
      .getLogger(QueryBuilder.class.getCanonicalName());
  private static final String SQL_GET_CERTIFICATES_PART_ONE = "SELECT id,name,description,price,create_day,last_update_date FROM gift_certificate";
  private static final String SQL_DESCRIPTION_SEARCH = "  WHERE description LIKE '%";
  private static final String SQL_NAME_SEARCH = "%' OR name LIKE '%";
  private static final String SQL_SEARCH_PARAMETER = "%'";
  private static final String SQL_ORDER_BY = " ORDER BY ";
  private static final String SQL_SPACE = " ";
  private static final String SQL_TAG_NAME_START =
      "SELECT id,name,description,price,create_day,last_update_date "
          + "FROM (SELECT gift_certificate.id, gift_certificate.name,"
          + "gift_certificate.description,gift_certificate.create_day,\n"
          + "gift_certificate.last_update_date,gift_certificate.price, tag.name AS tag\n"
          + "FROM gift_certificate \n"
          + "JOIN\n"
          + "gift_tag ON gift_certificate.id=gift_tag.gift_certificate_id\n"
          + "JOIN\n"
          + "tag ON tag.id = gift_tag.tag_id  \n"
          + "WHERE tag.name = '";
  private static final String SQL_TAG_NAME_END = "') AS K";
  private static final String SQL_PAGINATION = " LIMIT ? OFFSET ?";
  private static final String LOG_SQL_MESSAGE = "executing SQL: ";

  /**
   * Returns sql query to retrieve all certificate
   * @param sortRequest - query parameters for pagination, search and sorting
   * @return String sql
   */
  public String buildGetAllCertificatesQuery(Sort sortRequest){
    String sql;
    if (sortRequest.getTag() != null && !sortRequest.getTag().trim().isEmpty()) {
      sql = SQL_TAG_NAME_START + sortRequest.getTag() + SQL_TAG_NAME_END;
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
    LOGGER.debug(LOG_SQL_MESSAGE + sql);
    return sql;
  }
}
