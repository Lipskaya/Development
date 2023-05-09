package com.epam.esm.model.request;

import com.epam.esm.model.validation.OrderPutValidation;
import javax.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request model for OrderEntity request
 */
@NoArgsConstructor
@Data
public class UpdateOrderRequest {

  private Integer id;
  @Min(message = "User ID should be >= 1", groups = OrderPutValidation.class, value = 1)
  private Integer userId;
  @Min(message = "Certificate ID should be >= 1", groups = OrderPutValidation.class, value = 1)
  private Integer certificateId;
  @Min(message = "Cost should be >= 1", groups = OrderPutValidation.class, value = 1)
  private Double cost;

}
