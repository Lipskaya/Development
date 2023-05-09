package com.epam.esm.service.mapper;

import com.epam.esm.entity.UserEntity;
import com.epam.esm.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserEntityMapper {

  public UserEntity mapToEntity(User user){
    UserEntity userEntity = new UserEntity();
    userEntity.setId(user.getId());
    userEntity.setName(user.getName());
    return userEntity;
  }

}
