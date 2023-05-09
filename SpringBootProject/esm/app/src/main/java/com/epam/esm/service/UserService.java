package com.epam.esm.service;

import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.UserEntity;
import com.epam.esm.model.Sort;
import com.epam.esm.model.Tag;
import com.epam.esm.model.User;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.mapper.UserEntityMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * UserEntity Service
 */
@Service
@Slf4j

public class UserService {

  private static final String LOG_GET_BY_ID_START = "getById() called";
  private static final String LOG_GET_BY_ID_END = "getById() return: ";
  private static final String LOG_GET_ALL_START = "getAll() called";
  private static final String LOG_GET_ALL_END = "getAll() return: ";
  private static final String LOG_GET_TOTAL_ITEMS_START = "getTotalItems() called";
  private static final String LOG_GET_TOTAL_ITEMS_END = "getTotalItems() return: ";

  @Autowired
  private UserDao userDao;
  @Autowired
  private UserEntityMapper userEntityMapper;
  @Autowired
  private UserRepository userRepository;

//  @Autowired
//  public UserService(UserDao userDao, UserEntityMapper userEntityMapper) {
//    this.userDao = userDao;
//    this.userEntityMapper = userEntityMapper;
//  }

  /**
   * Finds all Users
   *
   * @return list of users
   * @param sortRequest
   */
  public Page<User> getAll(Sort sortRequest) {
    log.debug(LOG_GET_ALL_START);
    Pageable pageable = PageRequest.of(sortRequest.getPaginationOffset() - 1, sortRequest.getPaginationLimit());
    Page<User> users = userRepository.findAll(pageable);

//    List<User> users = userDao.getAll(sortRequest);
//    List<UserEntity> userEntities = new ArrayList<>();
//    for (User u: users) {
//      userEntities.add(userEntityMapper.mapToEntity(u));
//    }
    log.debug(LOG_GET_ALL_END + users);
    return users;
  }


  /**
   * Retrieves the UserEntity by id
   *
   * @param userId
   * @return user
   */
  public User getById(int userId) {
    log.debug(LOG_GET_BY_ID_START + userId);
    User user = userRepository.getById(userId);
//    User user = userDao.getById(userId);
//    UserEntity userEntity = userEntityMapper.mapToEntity(user);
    log.debug(LOG_GET_BY_ID_END + user);
    return user;
  }

//  /**
//   * Get total number of users
//   *
//   * @return int number of users
//   */
//  public int getTotalItems() {
//    log.debug(LOG_GET_TOTAL_ITEMS_START);
//    int total = userDao.getTotalItems();
//    log.debug(LOG_GET_TOTAL_ITEMS_END + total);
//    return total;
//  }

}
