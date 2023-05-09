package com.epam.esm.repository;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model for performing sorting and pagination
 */
@NoArgsConstructor
@Data

public class SearchCriteria {

  private String sortField;
  private String direction;
  private int itemsPerPage;
  private int page;
  private String search;
  private List<String> tags;
  private Integer userId;

}
