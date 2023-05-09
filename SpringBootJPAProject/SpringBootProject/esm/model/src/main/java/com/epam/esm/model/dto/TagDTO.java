package com.epam.esm.model.dto;

import javax.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

/**
 * TagDTO model
 */
@NoArgsConstructor
@Data
public class TagDTO extends RepresentationModel<TagDTO> {
  private Integer id;
  @NotEmpty(message = "Please provide a name")
  private String name;
}
