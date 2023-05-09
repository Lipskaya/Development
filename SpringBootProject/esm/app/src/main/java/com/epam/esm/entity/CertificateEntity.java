package com.epam.esm.entity;

import com.epam.esm.model.validation.CertificatePostValidation;
import com.epam.esm.model.validation.CertificatePutValidation;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.RepresentationModel;

/**
 * CertificateEntity model
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CertificateEntity extends RepresentationModel<CertificateEntity> {

  @Min(message = "Id should be > 0", groups = CertificatePutValidation.class, value = 1)
  @NotNull(message = "Id should not be empty", groups = CertificatePutValidation.class)
  private Integer id;
  @NotEmpty(message = "Please provide a name", groups = CertificatePostValidation.class)
  private String name;
  private String description;
  @Digits(message = "Please provide valid price value", integer = 10, fraction = 2,
      groups = {CertificatePostValidation.class, CertificatePutValidation.class})
  @NotNull(message = "Price should not be empty", groups = CertificatePostValidation.class)
  private Double price;
  @DateTimeFormat
  private LocalDateTime createDay;
  @DateTimeFormat
  private LocalDateTime lastUpdateDate;
  @NotNull(message = "Duration should not be empty", groups = CertificatePostValidation.class)
  @Min(message = "Duration should be > 0",
       groups ={CertificatePutValidation.class, CertificatePostValidation.class} , value = 1)
  private Integer duration;
  private Set<TagEntity> tagEntities = new HashSet<>();
}
