package com.epam.esm.dao;

import com.epam.esm.exception.ErrorCode;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.dao.mapper.UserRowMapper;
import com.epam.esm.model.Sort;
import com.epam.esm.model.User;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Database implementation of UserDao interface
 */
@Repository
@Slf4j
public class UserDaoImpl implements UserDao{
  private static final String SQL_GET_ALL = "SELECT id,name FROM user LIMIT ? OFFSET ?";
  private static final String SQL_GET_USER_BY_ID = "SELECT id,name FROM user where id = ?";
  private static final String SQL_TOTAL_USERS = "SELECT count(*) FROM user";
  private static final String LOG_GET_ALL_START = "getAll() called";
  private static final String LOG_GET_ALL_END = "getAll() return: ";
  private static final String LOG_GET_BY_ID_START = "getById() called";
  private static final String LOG_GET_BY_ID_END = "getById() return: ";
  private static final String USER_NOT_FOUND_START_MESSAGE = "UserEntity with ID=";
  private static final String USER_NOT_FOUND_END_MESSAGE = " not found";
  private static final String LOG_GET_TOTAL_ITEMS_START = "getTotalItems() called";
  private static final String LOG_GET_TOTAL_ITEMS_END = "getTotalItems() return: ";



  private final JdbcTemplate jdbcTemplate;
  private final UserRowMapper userRowMapper;

  @Autowired
  public UserDaoImpl(JdbcTemplate jdbcTemplate, UserRowMapper userRowMapper) {
    this.jdbcTemplate = jdbcTemplate;
    this.userRowMapper = userRowMapper;
  }

  @Override
  public User getById(Integer id) {
    log.debug(LOG_GET_BY_ID_START);
    try {
      User user = jdbcTemplate.queryForObject(SQL_GET_USER_BY_ID, userRowMapper, id);
      log.debug(LOG_GET_BY_ID_END + user);
      return user;
    } catch (EmptyResultDataAccessException e) {
      log.error(USER_NOT_FOUND_START_MESSAGE, e);
      throw new NotFoundException(USER_NOT_FOUND_START_MESSAGE + id + USER_NOT_FOUND_END_MESSAGE,
          ErrorCode.ERROR_USER);
    }
  }

  @Override
  public List<User> getAll(Sort sortRequest) {
    log.debug(LOG_GET_ALL_START + sortRequest);
    List<User> users = jdbcTemplate.query(SQL_GET_ALL, userRowMapper,
        sortRequest.getPaginationLimit(),
        (sortRequest.getPaginationOffset() - 1) * sortRequest.getPaginationLimit());
    log.debug(LOG_GET_ALL_END + users);
    return users;
  }

  @Override
  public User insert(User user) {
    throw new UnsupportedOperationException("Not implemented");

  }

  @Override
  public User update(User user) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void delete(User user) {
    throw new UnsupportedOperationException("Not implemented");

  }

  @Override
  public int getTotalItems() {
    log.debug(LOG_GET_TOTAL_ITEMS_START);
    int total = jdbcTemplate.queryForObject(SQL_TOTAL_USERS, Integer.class);
    log.debug(LOG_GET_TOTAL_ITEMS_END + total);
    return total;
  }
}
