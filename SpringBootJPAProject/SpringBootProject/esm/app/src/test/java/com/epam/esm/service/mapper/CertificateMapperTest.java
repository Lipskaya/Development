package com.epam.esm.service.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.epam.esm.model.dto.CertificateDTO;
import com.epam.esm.model.dto.TagDTO;
import com.epam.esm.repository.entity.CertificateEntity;
import com.epam.esm.repository.entity.TagEntity;
import com.epam.esm.service.mapper.CertificateMapper;
import com.epam.esm.service.mapper.TagMapper;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class CertificateMapperTest {

  @Test
  void mapToEntityTest(){
    CertificateDTO certificateDTO = new CertificateDTO();
    certificateDTO.setId(1);
    certificateDTO.setCreateDay(LocalDateTime.now());
    certificateDTO.setLastUpdateDate(LocalDateTime.now());
    certificateDTO.setDescription("description");
    certificateDTO.setDuration(12);
    certificateDTO.setPrice(56.0);
    certificateDTO.setName("name");
    Set<TagDTO> tags = new HashSet<>();
    TagDTO dto = new TagDTO();
    dto.setId(1);
    dto.setName("name_1");
    TagDTO dto1 = new TagDTO();
    dto1.setId(2);
    dto1.setName("name_2");
    TagDTO dto2 = new TagDTO();
    dto2.setId(3);
    dto2.setName("name_3");
    tags.add(dto);
    tags.add(dto1);
    tags.add(dto2);
    certificateDTO.setTags(tags);
    CertificateMapper certificateMapper = new CertificateMapper(new TagMapper());
    CertificateEntity certificateEntity = certificateMapper.mapToEntity(certificateDTO);
    assertThat(certificateEntity.getId(), is(certificateDTO.getId()));
    assertThat(certificateEntity.getDescription(), is(certificateDTO.getDescription()));
    assertThat(certificateEntity.getCreateDay(), is(certificateDTO.getCreateDay()));
    assertThat(certificateEntity.getPrice(), is(certificateDTO.getPrice()));
    assertThat(certificateEntity.getLastUpdateDate(), is(certificateDTO.getLastUpdateDate()));
    assertThat(certificateEntity.getDuration(), is(certificateDTO.getDuration()));
    assertThat(certificateEntity.getName(), is(certificateDTO.getName()));
    assertThat(certificateEntity.getTagEntities().size(), is(certificateDTO.getTags().size()));
  }

  @Test
  void mapToDTOTest(){
    CertificateEntity certificateEntity = new CertificateEntity();
    certificateEntity.setId(1);
    certificateEntity.setCreateDay(LocalDateTime.now());
    certificateEntity.setLastUpdateDate(LocalDateTime.now());
    certificateEntity.setDescription("description");
    certificateEntity.setDuration(12);
    certificateEntity.setPrice(56.0);
    certificateEntity.setName("name");
    Set<TagEntity> tags = new HashSet<>();
    TagEntity tagEntity = new TagEntity();
    tagEntity.setId(1);
    tagEntity.setName("name_1");
    TagEntity tagEntity1 = new TagEntity();
    tagEntity1.setId(2);
    tagEntity1.setName("name_2");
    TagEntity tagEntity2 = new TagEntity();
    tagEntity2.setId(3);
    tagEntity2.setName("name_3");
    tags.add(tagEntity);
    tags.add(tagEntity1);
    tags.add(tagEntity2);
    certificateEntity.setTagEntities(tags);
    CertificateMapper certificateMapper = new CertificateMapper(new TagMapper());
    CertificateDTO certificateDTO = certificateMapper.mapToDTO(certificateEntity);
    assertThat(certificateEntity.getId(), is(certificateDTO.getId()));
    assertThat(certificateEntity.getDescription(), is(certificateDTO.getDescription()));
    assertThat(certificateEntity.getCreateDay(), is(certificateDTO.getCreateDay()));
    assertThat(certificateEntity.getPrice(), is(certificateDTO.getPrice()));
    assertThat(certificateEntity.getLastUpdateDate(), is(certificateDTO.getLastUpdateDate()));
    assertThat(certificateEntity.getDuration(), is(certificateDTO.getDuration()));
    assertThat(certificateEntity.getName(), is(certificateDTO.getName()));
    assertThat(certificateEntity.getTagEntities().size(), is(certificateDTO.getTags().size()));
  }
}
