package com.epam.esm.response;

import com.epam.esm.model.dto.TagDTO;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

/**
 * Response model for paginated list of tags
 */
@NoArgsConstructor
@Data
public class TagListResponse extends RepresentationModel<TagListResponse> {
  private int page;
  private int itemsPerPage;
  private long totalItems;
  private List<TagDTO> tags;
}
