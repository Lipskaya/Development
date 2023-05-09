package com.epam.esm.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.epam.esm.model.dto.UserDTO;
import com.epam.esm.repository.SearchCriteria;
import com.epam.esm.response.UserListResponse;
import com.epam.esm.service.UserService;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest Controller to process UserDTO - related HTTP requests
 */
@Validated
@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {
  private static final String LOG_GET_USER_START = "getUserEntity() called";
  private static final String LOG_GET_USER_END = "getUserEntity() return: ";
  private static final String LOG_GET_USERS_START = "getUsers() called";
  private static final String LOG_GET_USERS_END = "getUsers() return: ";

  private final UserService userService;


  /**
   * Retrieves all users
   *
   * @return list of users
   */
  @GetMapping(value = "/v1/users", produces = {MediaType.APPLICATION_JSON_VALUE})
  public UserListResponse getUsers(
      @RequestParam(required = false, defaultValue = "5") @Min(value = 1, message = "itemsPerPage Min value should be 1") @Max(value = 20, message = "itemsPerPage should Not be more than 20") int itemsPerPage,
      @RequestParam(required = false, defaultValue = "1") @Min(value = 1, message = "page Min value should be 1") int page) {
    log.debug(LOG_GET_USERS_START);
    SearchCriteria searchCriteriaRequest = new SearchCriteria();
    searchCriteriaRequest.setItemsPerPage(itemsPerPage);
    searchCriteriaRequest.setPage(page);
    UserListResponse userListResponse = userService.getAll(searchCriteriaRequest);
    userListResponse.getUsers().forEach(userEntity -> userEntity
        .add(linkTo(methodOn(UserController.class).getUser(userEntity.getId())).withSelfRel()));
    userListResponse.add(
        linkTo(methodOn(UserController.class).getUsers(itemsPerPage, page +1))
            .withSelfRel());
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
  public UserDTO getUser(
      @PathVariable("userId") @Min(value = 1, message = "userId Min value should be 1") int userId) {
    log.debug(LOG_GET_USER_START);
    UserDTO userDTO = userService.getById(userId);
    Link link = linkTo(methodOn(UserController.class).getUser(userDTO.getId())).withSelfRel();
    userDTO.add(link);
    log.debug(LOG_GET_USER_END + userDTO);
    return userDTO;
  }
}
