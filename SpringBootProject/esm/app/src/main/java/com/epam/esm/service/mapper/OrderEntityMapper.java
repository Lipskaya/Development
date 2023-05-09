package com.epam.esm.service.mapper;

import com.epam.esm.entity.OrderEntity;
import com.epam.esm.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderEntityMapper {

  @Autowired
  private UserEntityMapper userEntityMapper;
  @Autowired
  private CertificateEntityMapper certificateEntityMapper;

  public OrderEntity mapToEntity(Order order){
    OrderEntity orderEntity = new OrderEntity();
    orderEntity.setId(order.getId());
    orderEntity.setDate(order.getDate());
    orderEntity.setCost(order.getCost());
    orderEntity.setUserEntity(userEntityMapper.mapToEntity(order.getUser()));
    orderEntity.setCertificateEntity(certificateEntityMapper.mapToEntity(order.getCertificate()));
    return orderEntity;
  }
}
