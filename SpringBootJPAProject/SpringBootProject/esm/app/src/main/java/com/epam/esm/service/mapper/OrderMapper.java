package com.epam.esm.service.mapper;

import com.epam.esm.model.dto.OrderDTO;
import com.epam.esm.repository.entity.OrderEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrderMapper {
  private final UserMapper userMapper;
  private final CertificateMapper certificateMapper;

  /**
   * Convert OrderEntity to OrderDTO
   * @param orderEntity
   * @return orderDTO
   */
  public OrderDTO mapToDTO(OrderEntity orderEntity) {
    OrderDTO orderDTO = new OrderDTO();
    orderDTO.setId(orderEntity.getId());
    orderDTO.setDate(orderEntity.getDate());
    orderDTO.setCost(orderEntity.getCost());
    orderDTO.setUser(userMapper.mapToDTO(orderEntity.getUserEntity()));
    orderDTO
        .setCertificate(certificateMapper.mapToDTO(orderEntity.getCertificateEntity()));
    return orderDTO;
  }

  /**
   * Convert OrderDTO to OrderEntity
   * @param orderDTO
   * @return orderEntity
   */
  public OrderEntity mapToEntity(OrderDTO orderDTO) {
    OrderEntity orderEntity = new OrderEntity();
    orderEntity.setId(orderDTO.getId());
    orderEntity.setDate(orderDTO.getDate());
    orderEntity.setCost(orderDTO.getCost());
    orderEntity.setUserEntity(userMapper.mapToEntity(orderDTO.getUser()));
    orderEntity
        .setCertificateEntity(certificateMapper.mapToEntity(orderDTO.getCertificate()));
    return orderEntity;
  }
}
