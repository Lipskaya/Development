package com.epam.esm.response;

import com.epam.esm.entity.UserEntity;
import com.epam.esm.model.User;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response model for paginated list of users
 */
@NoArgsConstructor
@Data
public class UserListResponse {
  private int page;
  private int itemsPerPage;
  private long totalItems;
  private List<UserEntity> userEntities;
}
