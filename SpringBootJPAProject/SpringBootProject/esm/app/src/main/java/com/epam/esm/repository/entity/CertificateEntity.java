package com.epam.esm.repository.entity;

import com.epam.esm.model.converter.LocalDateTimeConverter;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * CertificateEntity model
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "gift_certificate")
public class CertificateEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String name;
  private String description;
  private Double price;
  @DateTimeFormat
  @Column(name = "create_day")
  @Convert(converter = LocalDateTimeConverter.class)
  private LocalDateTime createDay;
  @DateTimeFormat
  @Column(name = "last_update_date")
  @Convert(converter = LocalDateTimeConverter.class)
  private LocalDateTime lastUpdateDate;
  private Integer duration;
  @ManyToMany
  @JoinTable(
      name = "gift_tag",
      joinColumns = @JoinColumn(name = "gift_certificate_id"),
      inverseJoinColumns = @JoinColumn(name = "tag_id")
  )
  private Set<TagEntity> tagEntities = new HashSet<>();
}
