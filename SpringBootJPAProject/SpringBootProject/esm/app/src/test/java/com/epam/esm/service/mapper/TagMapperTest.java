package com.epam.esm.service.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.epam.esm.model.dto.TagDTO;
import com.epam.esm.repository.entity.TagEntity;
import com.epam.esm.service.mapper.TagMapper;
import org.junit.jupiter.api.Test;

public class TagMapperTest {

  @Test
  void mapToEntityTest(){
    TagDTO tagDTO = new TagDTO();
    tagDTO.setId(1);
    tagDTO.setName("tagName");
    TagMapper tagMapper = new TagMapper();
    TagEntity tagEntity = tagMapper.mapToEntity(tagDTO);
    assertThat(tagEntity.getId(), is(tagDTO.getId()));
    assertThat(tagEntity.getName(), is(tagDTO.getName()));
  }

  @Test
  void mapToDTOTest(){
    TagEntity tagEntity = new TagEntity();
    tagEntity.setId(1);
    tagEntity.setName("tagName");
    TagMapper tagMapper = new TagMapper();
    TagDTO tagDTO = tagMapper.mapToDTO(tagEntity);
    assertThat(tagDTO.getId(), is(tagEntity.getId()));
    assertThat(tagDTO.getName(), is(tagEntity.getName()));
  }


}
