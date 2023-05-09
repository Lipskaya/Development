package com.epam.esm.repository.entity;

import com.epam.esm.model.converter.LocalDateTimeConverter;
import com.epam.esm.model.validation.OrderPostValidation;
import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.Digits;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * OrderEntity model
 */
@NoArgsConstructor
@Data
@Entity(name = "spring_boot_certificates.`order`")
public class OrderEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @DateTimeFormat
  @Convert(converter = LocalDateTimeConverter.class)
  private LocalDateTime date;
  @Digits(message = "Please provide valid cost value", integer = 10, fraction = 2,
      groups = OrderPostValidation.class)
  private Double cost;
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private UserEntity userEntity;
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "gift_certificate_id", referencedColumnName = "id")
  private CertificateEntity certificateEntity;
}
