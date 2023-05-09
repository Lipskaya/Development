package com.epam.esm.service.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.epam.esm.model.dto.UserDTO;
import com.epam.esm.repository.entity.UserEntity;
import com.epam.esm.service.mapper.UserMapper;
import org.junit.jupiter.api.Test;

public class UserMapperTest {
  @Test
  void mapToEntityTest(){
    UserDTO userDTO = new UserDTO();
    userDTO.setId(1);
    userDTO.setName("tagName");
    UserMapper userMapper = new UserMapper();
    UserEntity userEntity = userMapper.mapToEntity(userDTO);
    assertThat(userEntity.getId(), is(userDTO.getId()));
    assertThat(userEntity.getName(), is(userDTO.getName()));
  }

  @Test
  void mapToDTOTest(){
    UserEntity userEntity = new UserEntity();
    userEntity.setId(1);
    userEntity.setName("tagName");
    UserMapper userMapper = new UserMapper();
    UserDTO userDTO = userMapper.mapToDTO(userEntity);
    assertThat(userDTO.getId(), is(userEntity.getId()));
    assertThat(userDTO.getName(), is(userEntity.getName()));
  }
}
