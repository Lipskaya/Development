package com.epam.esm.response;

import com.epam.esm.model.dto.OrderDTO;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

/**
 * Response model for paginated list of orders
 */
@Data
@Builder
public class OrderListResponse extends RepresentationModel<OrderListResponse> {
  private int page;
  private int itemsPerPage;
  private long totalItems;
  List<OrderDTO> orders;
}
