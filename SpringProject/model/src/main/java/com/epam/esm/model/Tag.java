package com.epam.esm.model;

import java.util.Objects;
import javax.validation.constraints.NotEmpty;

/**
 * Tag model
 */
public class Tag {

  private Integer id;
  @NotEmpty(message = "Please provide a name")
  private String name;

  public Tag() {
  }

  public Tag(Integer id, String name) {
    this.id = id;
    this.name = name;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Tag)) {
      return false;
    }
    Tag tag = (Tag) o;
    return getId().equals(tag.getId()) && getName().equals(tag.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getName());
  }

  @Override
  public String toString() {
    return "Tag{" +
        "id=" + id +
        ", name='" + name + '\'' +
        '}';
  }
}
