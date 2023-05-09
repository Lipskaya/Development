package com.epam.esm.service.mapper;

import com.epam.esm.model.dto.UserDTO;
import com.epam.esm.repository.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

  /**
   * Convert UserEntity to UserDTO
   * @param userEntity
   * @return userDTO
   */
  public UserDTO mapToDTO(UserEntity userEntity) {
    UserDTO userDTO = new UserDTO();
    userDTO.setId(userEntity.getId());
    userDTO.setName(userEntity.getName());
    return userDTO;
  }

  /**
   * Convert UserDTO to UserEntity
   * @param userDTO
   * @return userEntity
   */
  public UserEntity mapToEntity(UserDTO userDTO) {
    UserEntity userEntity = new UserEntity();
    userEntity.setId(userDTO.getId());
    userEntity.setName(userDTO.getName());
    return userEntity;
  }
}
