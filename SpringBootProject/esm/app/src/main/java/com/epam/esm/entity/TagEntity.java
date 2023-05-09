package com.epam.esm.entity;

import javax.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

/**
 * TagEntity model
 */
@NoArgsConstructor
@Data
public class TagEntity extends RepresentationModel<TagEntity> {

  private Integer id;
  @NotEmpty(message = "Please provide a name")
  private String name;

}
