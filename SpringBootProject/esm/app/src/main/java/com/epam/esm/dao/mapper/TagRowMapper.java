package com.epam.esm.dao.mapper;

import com.epam.esm.model.Tag;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

/**
 * Database RowMapper for TagEntity model
 */
@Component
@Slf4j
public class TagRowMapper implements RowMapper<Tag> {

  private static final String TAG_NAME_PARAMETER = "name";
  private static final String TAG_ID_PARAMETER = "id";
  private static final String LOG_MAP_ROW_START = "mapRow() called";
  private static final String LOG_MAP_ROW_END = "mapRow() return: ";

  /**
   * Converts provided resultSet and returns TagEntity object.
   *
   * @param rs
   * @param rowNum
   * @return
   * @throws SQLException
   */
  @Override
  public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
    log.debug(LOG_MAP_ROW_START);
    Tag tag = new Tag();
    tag.setId(rs.getInt(TAG_ID_PARAMETER));
    tag.setName(rs.getString(TAG_NAME_PARAMETER));
    log.debug(LOG_MAP_ROW_END + tag);
    return tag;
  }
}
