package com.epam.esm.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.epam.esm.entity.TagEntity;
import com.epam.esm.entity.UserEntity;
import com.epam.esm.model.Sort;
import com.epam.esm.model.User;
import com.epam.esm.response.UserListResponse;
import com.epam.esm.service.UserService;
import com.epam.esm.service.mapper.UserEntityMapper;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest Controller to process UserEntity - related HTTP requests
 */
@Validated
@RestController
@Slf4j
public class UserController {
  private static final String LOG_GET_USER_START = "getUserEntity() called";
  private static final String LOG_GET_USER_END = "getUserEntity() return: ";
  private static final String LOG_GET_USERS_START = "getUsers() called";
  private static final String LOG_GET_USERS_END = "getUsers() return: ";

  private UserService userService;
  @Autowired
  private UserEntityMapper userEntityMapper;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  /**
   * Retrieves all users
   *
   * @return list of users
   */
  @GetMapping(value = "/v1/users", produces = {MediaType.APPLICATION_JSON_VALUE})
  public UserListResponse getUsers( @RequestParam(required = false, defaultValue = "5") @Min(value = 1, message = "itemsPerPage Min value should be 1") @Max(value = 20, message = "itemsPerPage should Not be more than 20") int itemsPerPage,
                              @RequestParam(required = false, defaultValue = "1") @Min(value = 1, message = "page Min value should be 1") int page) {
    log.debug(LOG_GET_USERS_START);

    Sort sortRequest = new Sort();
    sortRequest.setPaginationLimit(itemsPerPage);
    sortRequest.setPaginationOffset(page);

    Page<User> userPage = userService.getAll(sortRequest);

    List<UserEntity> userEntities = new ArrayList<>();

    for (User user : userPage) {
      UserEntity userEntity = userEntityMapper.mapToEntity(user);
      Link link = linkTo(methodOn(UserController.class).getUser(userEntity.getId())).withSelfRel();
      userEntity.add(link);
      userEntities.add(userEntity);
    }
    log.debug(LOG_GET_USERS_END + userEntities);

    UserListResponse userListResponse = new UserListResponse();

    userListResponse.setUserEntities(userEntities);
    userListResponse.setPage(page);
    userListResponse.setItemsPerPage(itemsPerPage);
    userListResponse.setTotalItems(userPage.getTotalElements());

    log.debug(LOG_GET_USERS_END + userListResponse);
    return userListResponse;
  }

  /**
   * Retrieves a user by id
   *
   * @param userId
   * @return tag
   */
  @GetMapping(value = "/v1/users/{userId}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public UserEntity getUser(@PathVariable("userId") @Min(value = 1, message = "userId Min value should be 1") int userId) {
    log.debug(LOG_GET_USER_START);
    User user = userService.getById(userId);
    UserEntity userEntity = userEntityMapper.mapToEntity(user);

    Link link = linkTo(methodOn(UserController.class).getUser(userEntity.getId())).withSelfRel();
    userEntity.add(link);

    log.debug(LOG_GET_USER_END + userEntity);
    return userEntity;
  }
}
