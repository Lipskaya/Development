package com.epam.esm.response;

import com.epam.esm.entity.TagEntity;
import com.epam.esm.model.Tag;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response model for paginated list of tags
 */
@NoArgsConstructor
@Data
public class TagListResponse {
  private int page;
  private int itemsPerPage;
  private long totalItems;
  private List<TagEntity> tagEntities;
}
