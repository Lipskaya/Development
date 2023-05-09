package com.epam.esm.dao.mapper;

import com.epam.esm.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

/**
 * Database RowMapper for UserEntity model
 */
@Component
@Slf4j
public class UserRowMapper implements RowMapper<User> {

  private static final String USER_NAME_PARAMETER = "name";
  private static final String USER_ID_PARAMETER = "id";
  private static final String LOG_MAP_ROW_START = "mapRow() called";
  private static final String LOG_MAP_ROW_END = "mapRow() return: ";

  /**
   * Converts provided resultSet and returns UserEntity object.
   *
   * @param rs
   * @param rowNum
   * @return
   * @throws SQLException
   */
  @Override
  public User mapRow(ResultSet rs, int rowNum) throws SQLException {
    log.debug(LOG_MAP_ROW_START);
    User user = new User();
    user.setId(rs.getInt(USER_ID_PARAMETER));
    user.setName(rs.getString(USER_NAME_PARAMETER));
    log.debug(LOG_MAP_ROW_END + user);
    return user;
  }
}
