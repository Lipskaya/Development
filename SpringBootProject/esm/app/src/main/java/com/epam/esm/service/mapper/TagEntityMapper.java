package com.epam.esm.service.mapper;

import com.epam.esm.entity.TagEntity;
import com.epam.esm.model.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagEntityMapper {

  public TagEntity mapToEntity(Tag tag){
    TagEntity tagEntity = new TagEntity();
    tagEntity.setId(tag.getId());
    tagEntity.setName(tag.getName());
    return tagEntity;
  }

  public Tag mapToModel(TagEntity tagEntity) {
    Tag tag = new Tag();
    tag.setId(tagEntity.getId());
    tag.setName(tagEntity.getName());
    return tag;
  }
}
