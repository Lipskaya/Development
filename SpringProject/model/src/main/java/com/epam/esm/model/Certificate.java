package com.epam.esm.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Certificate model
 */

public class Certificate {

  private Integer id;
  @NotEmpty(message = "Please provide a name")
  private String name;
  private String description;
  @Digits(message = "Please provide valid price value", integer = 10, fraction = 2)
  @NotNull(message = "Price should not be empty")
  private Double price;
  @DateTimeFormat
  private LocalDateTime createDay;
  @DateTimeFormat
  private LocalDateTime lastUpdateDate;
  private List<Tag> tags = new ArrayList<>();

  public Certificate() {
  }

  public Certificate(Integer id, String name, String description, Double price,
      LocalDateTime createDay, LocalDateTime lastUpdateDate, List<Tag> tags) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.price = price;
    this.createDay = createDay;
    this.lastUpdateDate = lastUpdateDate;
    this.tags = tags;
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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public LocalDateTime getCreateDay() {
    return createDay;
  }

  public void setCreateDay(LocalDateTime createDay) {
    this.createDay = createDay;
  }

  public LocalDateTime getLastUpdateDate() {
    return lastUpdateDate;
  }

  public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
    this.lastUpdateDate = lastUpdateDate;
  }

  public List<Tag> getTags() {
    return tags;
  }

  public void setTags(List<Tag> tags) {
    this.tags = tags;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Certificate)) {
      return false;
    }
    Certificate that = (Certificate) o;
    return Double.compare(that.getPrice(), getPrice()) == 0 && getId().equals(that.getId())
        && getName().equals(that.getName()) && getDescription().equals(that.getDescription())
        && getCreateDay().equals(that.getCreateDay()) && getLastUpdateDate()
        .equals(that.getLastUpdateDate()) && getTags().equals(that.getTags());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getName(), getDescription(), getPrice(), getCreateDay(),
        getLastUpdateDate(), getTags());
  }

  @Override
  public String toString() {
    return "Certificate{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", description='" + description + '\'' +
        ", price=" + price +
        ", createDay=" + createDay +
        ", lastUpdateDate=" + lastUpdateDate +
        ", tagList=" + tags +
        '}';
  }
}
