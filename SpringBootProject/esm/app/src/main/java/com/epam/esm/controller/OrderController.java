package com.epam.esm.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.epam.esm.entity.CertificateEntity;
import com.epam.esm.entity.OrderEntity;
import com.epam.esm.entity.TagEntity;
import com.epam.esm.entity.UserEntity;
import com.epam.esm.model.Order;
import com.epam.esm.model.Sort;
import com.epam.esm.model.request.CreateOrderRequest;
import com.epam.esm.response.OrderListResponse;
import com.epam.esm.model.validation.OrderPostValidation;
import com.epam.esm.model.validation.OrderPutValidation;
import com.epam.esm.service.OrderService;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest Controller to process OrderEntity - related HTTP requests
 */
@Validated
@RestController
@Slf4j
public class OrderController {
  private static final String LOG_DELETE_START = "deleteOrder() called for id";
  private static final String LOG_DELETE_END = "deleteOrder() end for id: ";
  private static final String LOG_UPDATE_START = "update() called";
  private static final String LOG_UPDATE_END = "update() return: ";
  private static final String LOG_ADD_ORDER_START = "addOrder() called";
  private static final String LOG_ADD_ORDER_END = "addOrder() return: ";
  private static final String LOG_GET_ORDER_START = "getOrder() called";
  private static final String LOG_GET_ORDER_END = "getOrder() return: ";
  private static final String LOG_GET_ORDERS_START = "getOrders() called";
  private static final String LOG_GET_ORDERS_END = "getOrders() return: ";

  private OrderService orderService;

  @Autowired
  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  /**
   * Retrieves all orders
   *
   * @return list of orders
   */
  @GetMapping(value = "/v1/orders", produces = {MediaType.APPLICATION_JSON_VALUE})
  public OrderListResponse getOrders( @RequestParam(required = false, defaultValue = "5") @Min(value = 1, message = "itemsPerPage Min value should be 1")
                                    @Max(value = 20, message = "itemsPerPage should Not be more than 20") int itemsPerPage,
                                    @RequestParam(required = false, defaultValue = "1") @Min(value = 1, message = "page Min value should be 1") int page,
                                    @RequestParam(required = false) @Min(value = 1, message = "userId Min value should be 1") Integer userId) {
    log.debug(LOG_GET_ORDERS_START);
    Sort sortRequest = new Sort();
    sortRequest.setPaginationLimit(itemsPerPage);
    sortRequest.setPaginationOffset(page);
    sortRequest.setUserId(userId);
    List<OrderEntity> orders = orderService.getAll(sortRequest);
    for (OrderEntity orderEntity: orders ) {
      populateLinks(orderEntity);
    }
    log.debug(LOG_GET_ORDERS_END + orders);

    OrderListResponse orderListResponse = new OrderListResponse();
    orderListResponse.setOrderEntities(orders);
    orderListResponse.setPage(page);
    orderListResponse.setItemsPerPage(itemsPerPage);
    orderListResponse.setTotalItems(orderService.getTotalItems(sortRequest));
    log.debug(LOG_GET_ORDERS_END + orderListResponse);
    return orderListResponse;
  }

  private void populateLinks(OrderEntity orderEntity) {
    orderEntity.add(linkTo(methodOn(OrderController.class).getOrder(orderEntity.getId())).withSelfRel());
    UserEntity userEntity = orderEntity.getUserEntity();
    userEntity.add(linkTo(methodOn(UserController.class).getUser(userEntity.getId())).withSelfRel());
    CertificateEntity certificateEntity = orderEntity.getCertificateEntity();
    certificateEntity.add(linkTo(methodOn(CertificateController.class).getCertificate(certificateEntity.getId())).withSelfRel());

    for (TagEntity tagEntity: certificateEntity.getTagEntities()) {
      tagEntity.add(linkTo(methodOn(TagController.class).getTag(tagEntity.getId())).withSelfRel());
    }
  }

  /**
   * Retrieves a order by id
   *
   * @param orderId
   * @return order
   */
  @GetMapping(value = "/v1/orders/{orderId}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public OrderEntity getOrder(@PathVariable("orderId") @Min(value = 1, message = "orderId Min value should be 1") int orderId) {
    log.debug(LOG_GET_ORDER_START);
    OrderEntity order = orderService.getById(orderId);
    populateLinks(order);
    log.debug(LOG_GET_ORDER_END + order);
    return order;
  }

  /**
   * Saves a new order
   *
   * @param orderRequest
   * @return order
   */
  @PostMapping(value = "/v1/orders", produces = {MediaType.APPLICATION_JSON_VALUE})
  public OrderEntity addOrder(@Validated(OrderPostValidation.class) @RequestBody CreateOrderRequest orderRequest) {
    log.debug(LOG_ADD_ORDER_START + orderRequest);
    OrderEntity newOrder = orderService.insert(orderRequest);
    populateLinks(newOrder);
    log.debug(LOG_ADD_ORDER_END + orderRequest);
    return newOrder;
  }

  /**
   * Updates a order
   *
   * @param order
   * @return order
   */
  @PutMapping(value = "/v1/orders/{orderId}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public OrderEntity updateOrder(
      @PathVariable("orderId") @Min(value = 1, message = "orderId Min value should be 1") int orderId,
      @Validated(OrderPutValidation.class) @RequestBody Order order) {
    log.debug(LOG_UPDATE_START + order);
    order.setId(orderId);
    OrderEntity updateOrder = orderService.update(order);
    populateLinks(updateOrder);
    log.debug(LOG_UPDATE_END + updateOrder);
    return updateOrder;

  }

  /**
   * Deletes a order by id
   *
   * @param orderId
   */
  @DeleteMapping(value = "/v1/orders/{orderId}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public void deleteOrder(@PathVariable("orderId") @Min(value = 1, message = "orderId Min value should be 1") int orderId) {
    log.debug(LOG_DELETE_START + orderId);
    orderService.delete(orderId);
    log.debug(LOG_DELETE_END + orderId);
  }
}
