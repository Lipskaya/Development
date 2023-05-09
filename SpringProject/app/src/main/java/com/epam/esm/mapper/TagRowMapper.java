package com.epam.esm.mapper;

import com.epam.esm.model.Tag;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

/**
 * Database RowMapper for Tag model
 */
@Component
public class TagRowMapper implements RowMapper<Tag> {

  private static final Logger LOGGER = Logger.getLogger(TagRowMapper.class.getCanonicalName());
  private static final String TAG_NAME_PARAMETER = "name";
  private static final String TAG_ID_PARAMETER = "id";
  private static final String LOG_MAP_ROW_START = "mapRow() called";
  private static final String LOG_MAP_ROW_END = "mapRow() return: ";

  /**
   * Converts provided resultSet and returns Tag object.
   *
   * @param rs
   * @param rowNum
   * @return
   * @throws SQLException
   */
  @Override
  public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
    LOGGER.debug(LOG_MAP_ROW_START);
    Tag tag = new Tag();
    tag.setId(rs.getInt(TAG_ID_PARAMETER));
    tag.setName(rs.getString(TAG_NAME_PARAMETER));
    LOGGER.debug(LOG_MAP_ROW_END + tag);
    return tag;
  }
}
