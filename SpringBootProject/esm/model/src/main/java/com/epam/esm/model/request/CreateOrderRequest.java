package com.epam.esm.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request model for Order request
 */
@NoArgsConstructor
@Data
public class CreateOrderRequest {
private Integer userId;
private Integer certificateId;
}
