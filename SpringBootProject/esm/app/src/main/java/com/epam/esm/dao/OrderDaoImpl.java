package com.epam.esm.dao;

import com.epam.esm.exception.ErrorCode;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.dao.mapper.OrderRowMapper;
import com.epam.esm.model.Order;
import com.epam.esm.model.Sort;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 * Database implementation of OrderDao interface
 */
@Repository
@Slf4j
public class OrderDaoImpl implements OrderDao {


  private static final String LOG_INSERT_START = "insert() called";
  private static final String LOG_INSERT_END = "insert() return: ";
  private static final String ORDER_DATE_PARAMETER = "date";
  private static final String ORDER_COST_PARAMETER = "cost";
  private static final String ORDER_USER_ID_PARAMETER = "user_id";
  private static final String ORDER_GIFT_CERTIFICATE_ID_PARAMETER = "gift_certificate_id";
  private static final String LOG_GET_BY_ID_START = "getById() called";
  private static final String LOG_GET_BY_ID_END = "getById() return: ";
  private static final String ORDER_NOT_FOUND_START_MESSAGE = "OrderEntity with ID=";
  private static final String ORDER_NOT_FOUND_END_MESSAGE = " not found";
  private static final String LOG_DELETE_START = "delete() called:";
  private static final String LOG_DELETE_END = "delete() end order: ";
  private static final String LOG_UPDATE_START = "update() called";
  private static final String LOG_UPDATE_END = "update() return: ";
  private static final String LOG_GET_ALL_START = "getAll() called";
  private static final String LOG_GET_ALL_END = "getAll() return: ";
  private static final String LOG_GET_TOTAL_ITEMS_START = "getTotalItems() called";
  private static final String LOG_GET_TOTAL_ITEMS_END = "getTotalItems() return: ";




  private static final String SQL_GET_ORDER_BY_ID = "SELECT id, date, cost, user_id, gift_certificate_id "
      + "FROM spring_boot_certificates.`order` where id = ?";
  private static final String SQL_DELETE_ORDER = "DELETE FROM spring_boot_certificates.`order` WHERE id = ?";
  private static final String SQL_UPDATE_ORDER = "UPDATE spring_boot_certificates.`order` "
      + "SET date = ?, cost = ?, user_id = ?, gift_certificate_id = ? WHERE id = ?";
  private static final String SQL_TOTAL_ORDERS = "SELECT count(*) FROM spring_boot_certificates.`order`";
  private static final String SQL_TOTAL_USER_ORDERS = "SELECT count(*) FROM spring_boot_certificates.`order` WHERE user_id = ?";


  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert simpleJdbcInsert;
  private final OrderRowMapper orderRowMapper;
  private final QueryBuilder queryBuilder;

  @Autowired
  public OrderDaoImpl(JdbcTemplate jdbcTemplate,
      @Qualifier("simpleJdbcOrderInsert")
      SimpleJdbcInsert simpleJdbcInsert, OrderRowMapper orderRowMapper, QueryBuilder queryBuilder) {
    this.jdbcTemplate = jdbcTemplate;
    this.simpleJdbcInsert = simpleJdbcInsert;
    this.orderRowMapper = orderRowMapper;
    this.queryBuilder = queryBuilder;
  }
  /**
   * Retrieves OrderEntity from database by provided id
   *
   * @param id
   * @return OrderEntity
   */
  @Override
  public Order getById(Integer id) {
    log.debug(LOG_GET_BY_ID_START);
    try {
      Order order = jdbcTemplate.queryForObject(SQL_GET_ORDER_BY_ID, orderRowMapper, id);
      log.debug(LOG_GET_BY_ID_END + order);
      return order;
    } catch (EmptyResultDataAccessException e) {
      log.error(ORDER_NOT_FOUND_START_MESSAGE, e);
      throw new NotFoundException(ORDER_NOT_FOUND_START_MESSAGE + id + ORDER_NOT_FOUND_END_MESSAGE,
          ErrorCode.ERROR_ORDER);
    }
  }

  /**
   * Retrieves list of all Orders from database.
   *
   * @param sortRequest
   * @return list of Orders
   */
  @Override
  public List<Order> getAll(Sort sortRequest) {
    log.debug(LOG_GET_ALL_START);

    String sql = queryBuilder.buildGetAllOrderQuery(sortRequest);
    log.debug(LOG_GET_ALL_END + sql);

    List<Order> orders = null;

    if (sortRequest.getUserId() != null){
      orders = jdbcTemplate.query(sql, orderRowMapper,sortRequest.getUserId(),
          sortRequest.getPaginationLimit(),
          (sortRequest.getPaginationOffset() - 1) * sortRequest.getPaginationLimit());
    }
    else {
      orders = jdbcTemplate.query(sql, orderRowMapper,
          sortRequest.getPaginationLimit(),
          (sortRequest.getPaginationOffset() - 1) * sortRequest.getPaginationLimit());
    }


    log.debug(LOG_GET_ALL_END + orders);
    return orders;
  }

  /**
   * Saves a new OrderEntity in database
   * @param order
   * @return
   */
  @Override
  public Order insert(Order order) {
    log.debug(LOG_INSERT_START);
    Map<String, Object> parameters = new HashMap<>(1);
    parameters.put(ORDER_DATE_PARAMETER, order.getDate().toString());
    parameters.put(ORDER_COST_PARAMETER, order.getCost());
    parameters.put(ORDER_USER_ID_PARAMETER, order.getUser().getId());
    parameters.put(ORDER_GIFT_CERTIFICATE_ID_PARAMETER, order.getCertificate().getId());

    Number newId = simpleJdbcInsert.executeAndReturnKey(parameters);
    order.setId(newId.intValue());
    log.debug(LOG_INSERT_END + order);
    return order;
  }

  /**
   * Updates a order in database
   *
   * @param order
   * @return
   */
  @Override
  public Order update(Order order) {
    log.debug(LOG_UPDATE_START);
    Order oldOrder = getById(order.getId());

    LocalDateTime date = oldOrder.getDate();
    Double cost = oldOrder.getCost();
    Integer userId = oldOrder.getUser().getId();
    Integer certificateId = oldOrder.getCertificate().getId();

    if (order.getDate() != null) {
      date = order.getDate();
    }
    if (order.getCost() != null) {
      cost = order.getCost();
    }
    if (order.getUser() != null) {
      userId = order.getUser().getId();
    }
    if (order.getCertificate() != null) {
      certificateId = order.getCertificate().getId();
    }

    int updatedCertificateRows = jdbcTemplate
        .update(SQL_UPDATE_ORDER, date.toString(), cost,
            userId, certificateId, order.getId());

    if (updatedCertificateRows == 0) {
      log.error(ORDER_NOT_FOUND_START_MESSAGE);
      throw new NotFoundException(
          ORDER_NOT_FOUND_START_MESSAGE + order.getId() + ORDER_NOT_FOUND_END_MESSAGE,
          ErrorCode.ERROR_ORDER);
    }
    log.debug(LOG_UPDATE_END + order);
    return order;

  }

  /**
   * Deletes a order in database
   *
   * @param order
   */
  @Override
  public void delete(Order order) {
    log.debug(LOG_DELETE_START);
    jdbcTemplate.update(SQL_DELETE_ORDER, order.getId());
    log.debug(LOG_DELETE_END + order);
  }
  /**
   * Returns total items from data base 
   * @return
   */
  @Override
  public int getTotalItems(Sort sortRequest) {
    log.debug(LOG_GET_TOTAL_ITEMS_START);
    int total = 0;
    if (sortRequest.getUserId() != null){
     total = jdbcTemplate.queryForObject(SQL_TOTAL_USER_ORDERS, Integer.class, sortRequest.getUserId());
    }
    else{
     total = jdbcTemplate.queryForObject(SQL_TOTAL_ORDERS, Integer.class);
    }

    log.debug(LOG_GET_TOTAL_ITEMS_END + total);
    return total;
  }
}
