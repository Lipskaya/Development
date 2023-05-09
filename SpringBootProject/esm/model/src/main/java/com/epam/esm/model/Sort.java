package com.epam.esm.model;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model for performing sorting and pagination
 */
@NoArgsConstructor
@Data

public class Sort {

  private String sortField;
  private String direction;
  private int paginationLimit;
  private int paginationOffset;
  private String search;
  private List<String> tags;
  private Integer userId;

}
