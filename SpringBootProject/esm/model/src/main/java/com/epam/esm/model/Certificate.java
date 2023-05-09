package com.epam.esm.model;

import com.epam.esm.model.converter.LocalDateTimeConverter;
import com.epam.esm.model.validation.CertificatePostValidation;
import com.epam.esm.model.validation.CertificatePutValidation;
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
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Certificate model
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name="gift_certificate")
public class Certificate {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Null(message = "Please remove id from certificate body", groups = CertificatePostValidation.class)
  private Integer id;
  @NotEmpty(message = "Please provide a name", groups = CertificatePostValidation.class)
  private String name;
  private String description;
  @Digits(message = "Please provide valid price value", integer = 10, fraction = 2,
      groups = {CertificatePostValidation.class, CertificatePutValidation.class})
  @NotNull(message = "Price should not be empty", groups = CertificatePostValidation.class)
  private Double price;
  @DateTimeFormat
  @Column(name="create_day")
  @Convert(converter = LocalDateTimeConverter.class)
  private LocalDateTime createDay;
  @DateTimeFormat
  @Column(name="last_update_date")
  @Convert(converter = LocalDateTimeConverter.class)
  private LocalDateTime lastUpdateDate;
  @NotNull(message = "Duration should not be empty", groups = CertificatePostValidation.class)
  @Min(message = "Duration should be > 0",
       groups ={CertificatePutValidation.class, CertificatePostValidation.class} , value = 1)
  private Integer duration;
   @ManyToMany
   @JoinTable(
       name = "gift_tag",
       joinColumns = @JoinColumn(name = "gift_certificate_id"),
       inverseJoinColumns = @JoinColumn(name = "tag_id")
   )
  private Set<Tag> tags = new HashSet<>();
}
