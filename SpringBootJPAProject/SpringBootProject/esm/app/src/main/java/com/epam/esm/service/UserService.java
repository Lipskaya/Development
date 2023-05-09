package com.epam.esm.service;

import com.epam.esm.model.dto.UserDTO;
import com.epam.esm.repository.SearchCriteria;
import com.epam.esm.repository.UserDAORepository;
import com.epam.esm.repository.entity.UserEntity;
import com.epam.esm.response.UserListResponse;
import com.epam.esm.service.mapper.UserMapper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * UserDTO Service
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

  private static final String LOG_GET_BY_ID_START = "getById() called";
  private static final String LOG_GET_BY_ID_END = "getById() return: ";
  private static final String LOG_GET_ALL_START = "getAll() called";
  private static final String LOG_GET_ALL_END = "getAll() return: ";
  private static final String USER_NOT_FOUND_START = "Requested UserEntity not found (id = ";
  private static final String END = ")";

  private final UserMapper userMapper;
  private final UserDAORepository userDAORepository;

  /**
   * Finds all Users
   *
   * @param searchCriteriaRequest
   * @return list of users
   */
  public UserListResponse getAll(SearchCriteria searchCriteriaRequest) {
    log.debug(LOG_GET_ALL_START);

    List<UserEntity> fooList = userDAORepository.getAll(searchCriteriaRequest);
    List<UserDTO> userDTOS = fooList.stream()
        .map(user -> userMapper.mapToDTO(user))
        .collect(Collectors.toList());

    long countResult = userDAORepository.getTotalUsers();

    UserListResponse userListResponse = new UserListResponse();
    userListResponse.setUsers(userDTOS);
    userListResponse.setPage(searchCriteriaRequest.getPage());
    userListResponse.setItemsPerPage(searchCriteriaRequest.getItemsPerPage());
    userListResponse.setTotalItems(countResult);
    log.debug(LOG_GET_ALL_END + userListResponse);
    return userListResponse;
  }

  /**
   * Retrieves the UserDTO by id
   *
   * @param userId
   * @return user
   */
  public UserDTO getById(int userId) {
    log.debug(LOG_GET_BY_ID_START + userId);
    UserEntity userEntity = userDAORepository.getById(userId);
    UserDTO userDTO = userMapper.mapToDTO(userEntity);
    log.debug(LOG_GET_BY_ID_END + userDTO);
    return userDTO;
  }
}
