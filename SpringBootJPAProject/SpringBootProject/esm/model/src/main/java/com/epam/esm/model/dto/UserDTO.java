package com.epam.esm.model.dto;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

/**
 * UserDTO model
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO extends RepresentationModel<UserDTO> {
  private Integer id;
  @NotEmpty(message = "Please provide a name")
  private String name;
}
