package com.epam.esm.service.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.epam.esm.model.dto.CertificateDTO;
import com.epam.esm.model.dto.OrderDTO;
import com.epam.esm.model.dto.TagDTO;
import com.epam.esm.model.dto.UserDTO;
import com.epam.esm.repository.entity.CertificateEntity;
import com.epam.esm.repository.entity.OrderEntity;
import com.epam.esm.repository.entity.TagEntity;
import com.epam.esm.repository.entity.UserEntity;
import com.epam.esm.service.mapper.CertificateMapper;
import com.epam.esm.service.mapper.OrderMapper;
import com.epam.esm.service.mapper.TagMapper;
import com.epam.esm.service.mapper.UserMapper;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class OrderMapperTest {

  private UserMapper userMapper = new UserMapper();
  private CertificateMapper certificateMapper = new CertificateMapper(new TagMapper());
  private OrderMapper orderMapper = new OrderMapper(userMapper,certificateMapper);

  @Test
  void mapToEntityTest() {
    OrderDTO orderDTO = new OrderDTO();
    orderDTO.setId(1);
    orderDTO.setCost(34.0);
    orderDTO.setDate(LocalDateTime.now());
    UserDTO userDTO = new UserDTO();
    userDTO.setId(1);
    userDTO.setName("nameUser");
    orderDTO.setUser(userDTO);
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
    orderDTO.setCertificate(certificateDTO);
    OrderEntity orderEntity = orderMapper.mapToEntity(orderDTO);
    assertThat(orderEntity.getId(), is(orderDTO.getId()));
    assertThat(orderEntity.getCost(), is(orderDTO.getCost()));
    assertThat(orderEntity.getDate(), is(orderDTO.getDate()));
    assertThat(orderEntity.getUserEntity(), is(userMapper.mapToEntity(orderDTO.getUser())));
    assertThat(orderEntity.getCertificateEntity(), is(certificateMapper.mapToEntity(orderDTO.getCertificate())));
  }

  @Test
  void mapToDTOTest() {
    OrderEntity orderEntity1 = new OrderEntity();
    orderEntity1.setId(1);
    orderEntity1.setCost(34.0);
    orderEntity1.setDate(LocalDateTime.now());
    UserEntity userEntity = new UserEntity();
    userEntity.setId(1);
    userEntity.setName("nameUser");
    orderEntity1.setUserEntity(userEntity);
    CertificateEntity certificateEntity = new CertificateEntity();
    certificateEntity.setId(1);
    certificateEntity.setCreateDay(LocalDateTime.now());
    certificateEntity.setLastUpdateDate(LocalDateTime.now());
    certificateEntity.setDescription("description");
    certificateEntity.setDuration(12);
    certificateEntity.setPrice(56.0);
    certificateEntity.setName("name");
    Set<TagEntity> tags = new HashSet<>();
    TagEntity dto = new TagEntity();
    dto.setId(1);
    dto.setName("name_1");
    TagEntity dto1 = new TagEntity();
    dto1.setId(2);
    dto1.setName("name_2");
    TagEntity dto2 = new TagEntity();
    dto2.setId(3);
    dto2.setName("name_3");
    tags.add(dto);
    tags.add(dto1);
    tags.add(dto2);
    certificateEntity.setTagEntities(tags);
    orderEntity1.setCertificateEntity(certificateEntity);
    OrderDTO orderDTO = orderMapper.mapToDTO(orderEntity1);
    assertThat(orderDTO.getId(), is(orderEntity1.getId()));
    assertThat(orderDTO.getCost(), is(orderEntity1.getCost()));
    assertThat(orderDTO.getDate(), is(orderEntity1.getDate()));
    assertThat(orderDTO.getUser(), is(userMapper.mapToDTO(orderEntity1.getUserEntity())));
    assertThat(orderDTO.getCertificate(), is(certificateMapper.mapToDTO(orderEntity1.getCertificateEntity())));
  }
}
