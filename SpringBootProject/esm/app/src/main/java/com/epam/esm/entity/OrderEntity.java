package com.epam.esm.entity;

import com.epam.esm.model.validation.OrderPostValidation;
import java.time.LocalDateTime;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.RepresentationModel;

/**
 * OrderEntity model
 */
@NoArgsConstructor
@Data
public class OrderEntity extends RepresentationModel<OrderEntity> {
  private Integer id;
  @DateTimeFormat
  private LocalDateTime date;
  @Digits(message = "Please provide valid cost value", integer = 10, fraction = 2,
      groups = OrderPostValidation.class)
  @NotNull(message = "Cost should not be empty", groups = OrderPostValidation.class)
  private Double cost;
  private UserEntity userEntity;
  private CertificateEntity certificateEntity;
}
