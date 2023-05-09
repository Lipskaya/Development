package com.epam.esm.model.request;

import com.epam.esm.model.validation.OrderPostValidation;
import javax.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request model for OrderEntity request
 */
@NoArgsConstructor
@Data
public class CreateOrderRequest {

  @Min(message = "User ID should be >= 1", groups = OrderPostValidation.class, value = 1)
  private Integer userId;
  @Min(message = "Certificate ID should be >= 1", groups = OrderPostValidation.class, value = 1)
  private Integer certificateId;
}
