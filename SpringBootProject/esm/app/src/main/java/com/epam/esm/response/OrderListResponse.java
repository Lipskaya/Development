package com.epam.esm.response;

import com.epam.esm.entity.OrderEntity;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Order;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response model for paginated list of orders
 */
@NoArgsConstructor
@Data
public class OrderListResponse {
  private int page;
  private int itemsPerPage;
  private int totalItems;
  List<OrderEntity> orderEntities;
}
