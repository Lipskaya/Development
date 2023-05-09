package com.epam.esm.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.UserEntity;
import com.epam.esm.model.Sort;
import com.epam.esm.model.Tag;
import com.epam.esm.model.User;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.mapper.UserEntityMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  private static final String USER_ONE_NAME_PARAMETER = "FirstTestUsr1";
  private static final String USER_SECOND_NAME_PARAMETER = "FirstTestUser2";
  private static final String USER_THIRD_NAME_PARAMETER = "FirstTestUser3";

  @Mock
  private UserDao userDao;
  @Mock
  private UserRepository userRepository;
  @Spy
  UserEntityMapper userEntityMapper;
  @InjectMocks
  UserService userService;

  User expectedUser1;
  User expectedUser2;
  User expectedUser3;
  List<User> expectedUsers;

  @BeforeEach
  void setup() {
    expectedUsers = new ArrayList<>();
    expectedUser1 = new User();
    expectedUser1.setId(1);
    expectedUser1.setName(USER_ONE_NAME_PARAMETER);
    expectedUser2 = new User();
    expectedUser2.setId(2);
    expectedUser2.setName(USER_SECOND_NAME_PARAMETER);
    expectedUser3 = new User();
    expectedUser3.setId(3);
    expectedUser3.setName(USER_THIRD_NAME_PARAMETER);
    expectedUsers.add(expectedUser1);
    expectedUsers.add(expectedUser2);
    expectedUsers.add(expectedUser3);
  }

  @Test
  void getByIdTest() {
   // when(userDao.getById(anyInt())).thenReturn(expectedUser1);

    when(userRepository.getById(anyInt())).thenReturn(expectedUser1);
    User realUser = userService.getById(1);
    assertThat(realUser, is(expectedUser1));
  }

  @Test
  void getAllTest() {
    //when(userDao.getAll(any())).thenReturn(expectedUsers);

    Page<User> usersPage = new PageImpl<User>(expectedUsers);
    when(userRepository.findAll(any(Pageable.class))).thenReturn(usersPage);

    Sort sort = new Sort();
    sort.setPaginationOffset(1);
    sort.setPaginationLimit(5);
    Page<User> realUsers = userService.getAll(sort);
    assertThat(realUsers.getSize(), is(3));
    assertThat(realUsers.getContent().contains(expectedUser1), is(true));
    assertThat(realUsers.getContent().contains(expectedUser2), is(true));
    assertThat(realUsers.getContent().contains(expectedUser3), is(true));
  } 
}
