package com.epam.esm.response;

import com.epam.esm.model.dto.UserDTO;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

/**
 * Response model for paginated list of users
 */
@NoArgsConstructor
@Data
public class UserListResponse extends RepresentationModel<UserListResponse> {
  private int page;
  private int itemsPerPage;
  private long totalItems;
  private List<UserDTO> users;
}
