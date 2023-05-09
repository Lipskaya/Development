package com.epam.esm.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.epam.esm.model.dto.CertificateDTO;
import com.epam.esm.model.dto.OrderDTO;
import com.epam.esm.model.dto.UserDTO;
import com.epam.esm.repository.SearchCriteria;
import com.epam.esm.model.request.CreateOrderRequest;
import com.epam.esm.model.request.UpdateOrderRequest;
import com.epam.esm.model.validation.OrderPostValidation;
import com.epam.esm.model.validation.OrderPutValidation;
import com.epam.esm.response.OrderListResponse;
import com.epam.esm.service.OrderService;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
 * Rest Controller to process OrderDTO - related HTTP requests
 */
@Validated
@RestController
@Slf4j
@RequiredArgsConstructor
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

  private final OrderService orderService;

  /**
   * Retrieves all orders
   *
   * @return list of orders
   */
  @GetMapping(value = "/v1/orders", produces = {MediaType.APPLICATION_JSON_VALUE})
  public OrderListResponse getOrders(
      @RequestParam(required = false, defaultValue = "5") @Min(value = 1, message = "itemsPerPage Min value should be 1")
      @Max(value = 20, message = "itemsPerPage should Not be more than 20") int itemsPerPage,
      @RequestParam(required = false, defaultValue = "1") @Min(value = 1, message = "page Min value should be 1") int page,
      @RequestParam(required = false) @Min(value = 1, message = "userId Min value should be 1") Integer userId) {
    log.debug(LOG_GET_ORDERS_START);
    SearchCriteria searchCriteriaRequest = new SearchCriteria();
    searchCriteriaRequest.setItemsPerPage(itemsPerPage);
    searchCriteriaRequest.setPage(page);
    searchCriteriaRequest.setUserId(userId);
    OrderListResponse listResponse = orderService.getAll(searchCriteriaRequest);
    listResponse.getOrders().forEach(this::populateLinks);
    listResponse.add(
        linkTo(methodOn(OrderController.class).getOrders(itemsPerPage, page +1, userId))
            .withSelfRel());
    log.debug(LOG_GET_ORDERS_END + listResponse);
    return listResponse;
  }

  private OrderDTO populateLinks(OrderDTO orderDTO) {
    orderDTO.add(linkTo(methodOn(OrderController.class).getOrder(orderDTO.getId())).withSelfRel());
    UserDTO userEntity = orderDTO.getUser();
    userEntity
        .add(linkTo(methodOn(UserController.class).getUser(userEntity.getId())).withSelfRel());
    CertificateDTO certificateDTO = orderDTO.getCertificate();
    certificateDTO.add(
        linkTo(methodOn(CertificateController.class).getCertificate(certificateDTO.getId()))
            .withSelfRel());
    certificateDTO.getTags().
        forEach(tagEntity -> tagEntity
            .add(linkTo(methodOn(TagController.class).getTag(tagEntity.getId())).withSelfRel()));
    return orderDTO;
  }

  /**
   * Retrieves a order by id
   *
   * @param orderId
   * @return order
   */
  @GetMapping(value = "/v1/orders/{orderId}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public OrderDTO getOrder(
      @PathVariable("orderId") @Min(value = 1, message = "orderId Min value should be 1") int orderId) {
    log.debug(LOG_GET_ORDER_START);
    OrderDTO orderDTO = orderService.getById(orderId);
    populateLinks(orderDTO);
    log.debug(LOG_GET_ORDER_END + orderDTO);
    return orderDTO;
  }

  /**
   * Saves a new order
   *
   * @param orderRequest
   * @return order
   */
  @PostMapping(value = "/v1/orders", produces = {MediaType.APPLICATION_JSON_VALUE})
  public OrderDTO addOrder(
      @Validated(OrderPostValidation.class) @RequestBody CreateOrderRequest orderRequest) {
    log.debug(LOG_ADD_ORDER_START + orderRequest);
    OrderDTO orderDTO = orderService.insert(orderRequest);
    populateLinks(orderDTO);
    log.debug(LOG_ADD_ORDER_END + orderDTO);
    return orderDTO;
  }

  /**
   * Updates a order
   *
   * @param updateOrderRequest
   * @return order
   */
  @PutMapping(value = "/v1/orders/{orderId}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public OrderDTO updateOrder(
      @PathVariable("orderId") @Min(value = 1, message = "orderId Min value should be 1") int orderId,
      @Validated(OrderPutValidation.class) @RequestBody UpdateOrderRequest updateOrderRequest) {
    log.debug(LOG_UPDATE_START + updateOrderRequest);
    updateOrderRequest.setId(orderId);
    OrderDTO orderDTO = orderService.update(updateOrderRequest);
    populateLinks(orderDTO);
    log.debug(LOG_UPDATE_END + orderDTO);
    return orderDTO;
  }

  /**
   * Deletes a order by id
   *
   * @param orderId
   */
  @DeleteMapping(value = "/v1/orders/{orderId}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public void deleteOrder(
      @PathVariable("orderId") @Min(value = 1, message = "orderId Min value should be 1") int orderId) {
    log.debug(LOG_DELETE_START + orderId);
    orderService.delete(orderId);
    log.debug(LOG_DELETE_END + orderId);
  }
}
