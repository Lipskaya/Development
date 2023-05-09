package com.epam.esm.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.epam.esm.model.dto.UserDTO;
import com.epam.esm.repository.SearchCriteria;
import com.epam.esm.repository.UserDAORepository;
import com.epam.esm.repository.entity.UserEntity;
import com.epam.esm.response.UserListResponse;
import com.epam.esm.service.mapper.UserMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  private static final String USER_ONE_NAME_PARAMETER = "FirstTestUsr1";
  private static final String USER_SECOND_NAME_PARAMETER = "FirstTestUser2";
  private static final String USER_THIRD_NAME_PARAMETER = "FirstTestUser3";

  @Mock
  private UserDAORepository userRepository;
  @Mock
  private UserMapper userMapper;

  @InjectMocks
  UserService userService;
  UserEntity expectedUserEntity1;
  UserEntity expectedUserEntity2;
  UserEntity expectedUserEntity3;
  List<UserEntity> expectedUserEntities;

  @BeforeEach
  void setup() {
    expectedUserEntities = new ArrayList<>();
    expectedUserEntity1 = new UserEntity();
    expectedUserEntity1.setId(1);
    expectedUserEntity1.setName(USER_ONE_NAME_PARAMETER);
    expectedUserEntity2 = new UserEntity();
    expectedUserEntity2.setId(2);
    expectedUserEntity2.setName(USER_SECOND_NAME_PARAMETER);
    expectedUserEntity3 = new UserEntity();
    expectedUserEntity3.setId(3);
    expectedUserEntity3.setName(USER_THIRD_NAME_PARAMETER);
    expectedUserEntities.add(expectedUserEntity1);
    expectedUserEntities.add(expectedUserEntity2);
    expectedUserEntities.add(expectedUserEntity3);
  }

  @Test
  void getByIdTest() {
    UserDTO expectedDTO  = new UserDTO();

    when(userRepository.getById(anyInt())).thenReturn(expectedUserEntity1);
    when(userMapper.mapToDTO(expectedUserEntity1)).thenReturn(expectedDTO);

    UserDTO userDTO = userService.getById(1);
    assertThat(userDTO, is(userMapper.mapToDTO(expectedUserEntity1)));

    verify(userRepository,times(1)).getById(1);
    verify(userMapper,times(2)).mapToDTO(expectedUserEntity1);
    verifyNoMoreInteractions(userMapper,userRepository);
  }

  @Test
  void getAllTest() {

    when(userRepository.getAll(any())).thenReturn(expectedUserEntities);
    when(userRepository.getTotalUsers()).thenReturn(10L);

    SearchCriteria searchCriteria = new SearchCriteria();
    searchCriteria.setPage(1);
    searchCriteria.setItemsPerPage(5);

    UserListResponse realUsers = userService.getAll(searchCriteria);
    assertThat(realUsers.getUsers().size(), is(3));
    assertThat(realUsers.getUsers().contains(userMapper.mapToDTO(expectedUserEntity1)),
        is(true));
    assertThat(realUsers.getUsers().contains(userMapper.mapToDTO(expectedUserEntity2)),
        is(true));
    assertThat(realUsers.getUsers().contains(userMapper.mapToDTO(expectedUserEntity3)),
        is(true));

    verify(userRepository,times(1)).getAll(any());
    verify(userRepository,times(1)).getTotalUsers();
    verify(userMapper,times(2)).mapToDTO(expectedUserEntity1);
    verifyNoMoreInteractions(userRepository);
  }
}
