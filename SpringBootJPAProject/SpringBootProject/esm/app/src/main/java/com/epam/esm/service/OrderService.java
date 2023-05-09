package com.epam.esm.service;

import com.epam.esm.model.dto.OrderDTO;
import com.epam.esm.exception.ErrorCode;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.repository.SearchCriteria;
import com.epam.esm.repository.UserDAORepository;
import com.epam.esm.repository.entity.CertificateEntity;
import com.epam.esm.repository.entity.OrderEntity;
import com.epam.esm.repository.entity.UserEntity;
import com.epam.esm.model.request.CreateOrderRequest;
import com.epam.esm.model.request.UpdateOrderRequest;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.response.OrderListResponse;
import com.epam.esm.service.mapper.OrderMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * OrderDTO Service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

  private static final String LOG_GET_ALL_START = "getAll() called";
  private static final String LOG_GET_ALL_END = "getAll() return: ";
  private static final String LOG_GET_BY_ID_START = "getById() called";
  private static final String LOG_GET_BY_ID_END = "getById() return: ";
  private static final String LOG_DELETE_START = "delete() called id:";
  private static final String LOG_DELETE_END = "delete() end id: ";
  private static final String LOG_UPDATE_START = "update() called";
  private static final String LOG_UPDATE_END = "update() return: ";
  private static final String ORDER_NOT_FOUND_START = "Requested OrderEntity not found (id = ";
  private static final String END = ")";

  private final OrderMapper orderMapper;
  private final CertificateRepository certificateRepository;
  private final OrderRepository orderRepository;
  private final UserDAORepository userDAORepository;

  @Transactional
  public OrderDTO insert(CreateOrderRequest orderRequest) {
    log.debug(LOG_GET_ALL_START);
    LocalDateTime now = LocalDateTime.now();
    OrderEntity newOrderEntity = new OrderEntity();
    newOrderEntity.setDate(now);
    UserEntity userEntity = userDAORepository.getById(orderRequest.getUserId());
    newOrderEntity.setUserEntity(userEntity);
    CertificateEntity certificateEntity = certificateRepository
        .getById(orderRequest.getCertificateId());
    newOrderEntity.setCertificateEntity(certificateEntity);
    newOrderEntity.setCost(certificateEntity.getPrice());
    OrderEntity savedOrderEntity = orderRepository
        .saveAndFlush(newOrderEntity);// userEntity null, certificateEntity null
    OrderDTO orderDTO = orderMapper.mapToDTO(savedOrderEntity);
    log.debug(LOG_GET_ALL_END + orderDTO);
    return orderDTO;
  }

  /**
   * Retrieves the OrderDTO by id
   *
   * @param orderId
   * @return order
   */
  public OrderDTO getById(int orderId) {
    log.debug(LOG_GET_BY_ID_START + orderId);
    try {
      OrderEntity orderEntity = orderRepository.getById(orderId);// certificate, UserDTO have only correct ids
      OrderDTO orderDTO = orderMapper.mapToDTO(orderEntity);
      log.debug(LOG_GET_BY_ID_END + orderDTO);
      return orderDTO;
    } catch (EntityNotFoundException exception) {
      throw new NotFoundException(ORDER_NOT_FOUND_START + orderId + END, ErrorCode.ERROR_ORDER);
    }
  }

  /**
   * Deletes a order
   *
   * @param orderId
   */
  public void delete(int orderId) {
    log.debug(LOG_DELETE_START + orderId);
    OrderEntity orderEntity = new OrderEntity();
    orderEntity.setId(orderId);
    orderRepository.delete(orderEntity);
    log.debug(LOG_DELETE_END + orderEntity);
  }

  /**
   * Updates the order
   *
   * @param orderRequest
   * @return order
   */
  @Transactional
  public OrderDTO update(UpdateOrderRequest orderRequest) {
    log.debug(LOG_UPDATE_START + orderRequest);
    OrderDTO oldOrderDTO = getById(orderRequest.getId());
    OrderEntity oldOrderEntity = orderMapper.mapToEntity(oldOrderDTO);
    if (orderRequest.getUserId() != null) {
      UserEntity newUserEntity = userDAORepository.getById(orderRequest.getUserId());
      oldOrderEntity.setUserEntity(newUserEntity);
    }
    if (orderRequest.getCertificateId() != null) {
      CertificateEntity newCertificateEntity = certificateRepository
          .getById(orderRequest.getCertificateId());
      oldOrderEntity.setCertificateEntity(newCertificateEntity);
    }
    if (orderRequest.getCost() != null) {
      oldOrderEntity.setCost(orderRequest.getCost());
    }
    OrderEntity updateOrderEntity = orderRepository.save(oldOrderEntity);
    OrderDTO orderDTO = orderMapper.mapToDTO(updateOrderEntity);
    log.debug(LOG_UPDATE_END + orderDTO);
    return orderDTO;
  }

  /**
   * Finds all Orders
   *
   * @param searchCriteriaRequest
   * @return list of orders
   */
  public OrderListResponse getAll(SearchCriteria searchCriteriaRequest) {
    log.debug(LOG_GET_ALL_START);
    Pageable pageable = PageRequest.of(searchCriteriaRequest.getPage() - 1, searchCriteriaRequest.getItemsPerPage());
    Page<OrderEntity> ordersPage = null;
    if (searchCriteriaRequest.getUserId() == null) {
      ordersPage = orderRepository.findAll(pageable);
    } else {
      ordersPage = orderRepository.findUserOrders(searchCriteriaRequest.getUserId(), pageable);
    }
    List<OrderDTO> orderDTOS = ordersPage.stream()
        .map(orderMapper::mapToDTO)
        .collect(Collectors.toList());
    OrderListResponse orderListResponse = OrderListResponse.builder()
        .orders(orderDTOS)
        .page(searchCriteriaRequest.getPage())
        .itemsPerPage(searchCriteriaRequest.getItemsPerPage())
        .totalItems(ordersPage.getTotalElements())
        .build();
    log.debug(LOG_GET_ALL_END + orderListResponse);
    return orderListResponse;
  }
}
