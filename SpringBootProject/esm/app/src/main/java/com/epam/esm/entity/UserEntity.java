package com.epam.esm.entity;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

/**
 * UserEntity model
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserEntity extends RepresentationModel<UserEntity> {
  private Integer id;
  @NotEmpty(message = "Please provide a name")
  private String name;
}
