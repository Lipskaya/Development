package com.epam.esm.model;

/**
 * Model for performing sorting and pagination
 */

public class Sort {

  private String sortField;
  private String direction;
  private int paginationLimit;
  private int paginationOffset;
  private String search;
  private String tag;

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public String getSearch() {
    return search;
  }

  public void setSearch(String search) {
    this.search = search;
  }



  public String getSortField() {
    return sortField;
  }

  public void setSortField(String sortField) {
    this.sortField = sortField;
  }

  public String getDirection() {
    return direction;
  }

  public void setDirection(String direction) {
    this.direction = direction;
  }

  public int getPaginationLimit() {
    return paginationLimit;
  }

  public void setPaginationLimit(int paginationLimit) {
    this.paginationLimit = paginationLimit;
  }

  public int getPaginationOffset() {
    return paginationOffset;
  }

  public void setPaginationOffset(int paginationOffset) {
    this.paginationOffset = paginationOffset;
  }

  @Override
  public String toString() {
    return "Sort{" +
        "sortField='" + sortField + '\'' +
        ", direction='" + direction + '\'' +
        ", paginationLimit=" + paginationLimit +
        ", paginationOffset=" + paginationOffset +
        ", search='" + search + '\'' +
        ", tag='" + tag + '\'' +
        '}';
  }
}
