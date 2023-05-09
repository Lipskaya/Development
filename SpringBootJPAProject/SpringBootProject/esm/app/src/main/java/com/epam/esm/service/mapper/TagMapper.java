package com.epam.esm.service.mapper;

import com.epam.esm.model.dto.TagDTO;
import com.epam.esm.repository.entity.TagEntity;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {

  /**
   * Convert TagEntity to TagDTO
   * @param tagEntity
   * @return tagDTO
   */
  public TagDTO mapToDTO(TagEntity tagEntity) {
    TagDTO tagDTO = new TagDTO();
    tagDTO.setId(tagEntity.getId());
    tagDTO.setName(tagEntity.getName());
    return tagDTO;
  }

  /**
   * Convert TagDTO to TagEntity
   * @param tagDTO
   * @return tagEntity
   */
  public TagEntity mapToEntity(TagDTO tagDTO) {
    TagEntity tagEntity = new TagEntity();
    tagEntity.setId(tagDTO.getId());
    tagEntity.setName(tagDTO.getName());
    return tagEntity;
  }
}
