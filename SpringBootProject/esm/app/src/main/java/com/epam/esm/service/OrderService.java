package com.epam.esm.service;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.CertificateEntity;
import com.epam.esm.entity.OrderEntity;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.Sort;
import com.epam.esm.model.Tag;
import com.epam.esm.model.User;
import com.epam.esm.model.request.CreateOrderRequest;
import com.epam.esm.service.mapper.CertificateEntityMapper;
import com.epam.esm.service.mapper.OrderEntityMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * OrderEntity Service
 */
@Slf4j
@Service

public class OrderService {

  private static final String LOG_GET_ALL_START = "getAll() called";
  private static final String LOG_GET_ALL_END = "getAll() return: ";
  private static final String LOG_GET_BY_ID_START = "getById() called";
  private static final String LOG_GET_BY_ID_END = "getById() return: ";
  private static final String LOG_DELETE_START = "delete() called id:";
  private static final String LOG_DELETE_END = "delete() end id: ";
  private static final String LOG_UPDATE_START = "update() called";
  private static final String LOG_UPDATE_END = "update() return: ";
  private static final String LOG_GET_TOTAL_ITEMS_START = "getTotalItems() called";
  private static final String LOG_GET_TOTAL_ITEMS_END = "getTotalItems() return: ";



  private CertificateService certificateService; //service populates certificate tags
  private OrderDao orderDao;
  private UserDao userDao;
  private OrderEntityMapper orderEntityMapper;
  @Autowired
  private CertificateEntityMapper certificateEntityMapper;

  @Autowired
  public OrderService(CertificateService certificateService, OrderDao orderDao,
      UserDao userDao, OrderEntityMapper orderEntityMapper) {
    this.certificateService = certificateService;
    this.orderDao = orderDao;
    this.userDao = userDao;
    this.orderEntityMapper = orderEntityMapper;
  }

  public OrderEntity insert(CreateOrderRequest orderRequest) {

    log.debug(LOG_GET_ALL_START);

    LocalDateTime now = LocalDateTime.now();
    Order newOrder = new Order();
    newOrder.setDate(now);

    User user = userDao.getById(orderRequest.getUserId());
    newOrder.setUser(user);

    Certificate certificate = certificateService.getById(orderRequest.getCertificateId());
//    Certificate certificate = certificateEntityMapper.mapToModel(certificateEntity);

    newOrder.setCertificate(certificate);
    newOrder.setCost(certificate.getPrice());

    Order savedOrder = orderDao.insert(newOrder);// user null, certificate null
    savedOrder.setCertificate(certificate);
    savedOrder.setUser(user);

    OrderEntity orderEntity = orderEntityMapper.mapToEntity(savedOrder);
    log.debug(LOG_GET_ALL_END + orderEntity);
    return orderEntity;
  }
  /**
   * Retrieves the OrderEntity by id
   *
   * @param orderId
   * @return order
   */
  public OrderEntity getById(int orderId) {
    log.debug(LOG_GET_BY_ID_START + orderId);
    Order order = orderDao.getById(orderId);// certificate, UserEntity have only correct ids

    User user = userDao.getById(order.getUser().getId());
    Certificate certificate = certificateService.getById(order.getCertificate().getId());
//    Certificate certificate = certificateEntityMapper.mapToModel(certificateEntity);

    order.setUser(user);
    order.setCertificate(certificate);

    OrderEntity orderEntity = orderEntityMapper.mapToEntity(order);
    log.debug(LOG_GET_BY_ID_END + orderEntity);
    return orderEntity;
  }
  /**
   * Deletes a order
   *
   * @param orderId
   */
  public void delete(int orderId) {
    log.debug(LOG_DELETE_START + orderId);
    Order t = new Order();
    t.setId(orderId);
    orderDao.delete(t);
    log.debug(LOG_DELETE_END + t);
  }

  /**
   * Updates the order
   *
   * @param order
   * @return order
   */
  public OrderEntity update(Order order) {
    log.debug(LOG_UPDATE_START + order);
    orderDao.update(order);
    OrderEntity updateOrder = getById(order.getId());
    log.debug(LOG_UPDATE_END + updateOrder);
    return updateOrder;
  }

  /**
   * Finds all Orders
   *
   * @return list of orders
   * @param sortRequest
   */
  public List<OrderEntity> getAll(Sort sortRequest) {
    log.debug(LOG_GET_ALL_START);
    List<Order> orders = orderDao.getAll(sortRequest);
    for (Order order: orders ) {
      User user = userDao.getById(order.getUser().getId());
      Certificate certificate = certificateService.getById(order.getCertificate().getId());
 //     Certificate certificate = certificateEntityMapper.mapToModel(certificateEntity);
      order.setUser(user);
      order.setCertificate(certificate);
    }

    List<OrderEntity> orderEntities = new ArrayList<>();
    for (Order order: orders) {
      orderEntities.add(orderEntityMapper.mapToEntity(order));
    }

    log.debug(LOG_GET_ALL_END + orderEntities);
    return orderEntities;
  }

  /**
   * Returns total items of orders in data base
   * @return
   */
  public int getTotalItems(Sort sortRequest) {
    log.debug(LOG_GET_TOTAL_ITEMS_START);
    int total = orderDao.getTotalItems(sortRequest);
    log.debug(LOG_GET_TOTAL_ITEMS_END + total);
    return total;
  }
}
