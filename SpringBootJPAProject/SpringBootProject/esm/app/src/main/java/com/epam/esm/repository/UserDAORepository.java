package com.epam.esm.repository;

import com.epam.esm.exception.ErrorCode;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.repository.entity.UserEntity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@RequiredArgsConstructor
@Repository
public class UserDAORepository {

  private static final String LOG_GET_BY_ID_START = "getById() called";
  private static final String LOG_GET_BY_ID_END = "getById() return: ";
  private static final String LOG_GET_ALL_START = "getAll() called";
  private static final String LOG_GET_ALL_END = "getAll() return: ";
  private static final String USER_NOT_FOUND_START = "Requested UserEntity not found (id = ";
  private static final String END = ")";

  private final EntityManager entityManager;

  /**
   * Finds all Users
   *
   * @param searchCriteriaRequest
   * @return list of users
   */
  public List<UserEntity> getAll(SearchCriteria searchCriteriaRequest) {
    log.debug(LOG_GET_ALL_START);

    Query query = entityManager.createQuery("From user");
    int pageNumber = searchCriteriaRequest.getPage();
    int pageSize = searchCriteriaRequest.getItemsPerPage();
    query.setFirstResult((pageNumber) * pageSize);
    query.setMaxResults(pageSize);
    List<UserEntity> fooList = query.getResultList();
    return fooList;
  }

  public long getTotalUsers() {
    Query queryTotal = entityManager.createQuery
        ("Select count(f.id) From user f");
    long countResult = (long) queryTotal.getSingleResult();
    return countResult;
  }

  /**
   * Retrieves the UserDTO by id
   *
   * @param userId
   * @return user
   */
  public UserEntity getById(int userId) {
    log.debug(LOG_GET_BY_ID_START + userId);
    UserEntity userEntity = entityManager.find(UserEntity.class, userId);
    if (userEntity == null) {
      throw new NotFoundException(USER_NOT_FOUND_START + userId + END, ErrorCode.ERROR_USER);
    }
    log.debug(LOG_GET_BY_ID_END + userEntity);
    return userEntity;
  }
}
