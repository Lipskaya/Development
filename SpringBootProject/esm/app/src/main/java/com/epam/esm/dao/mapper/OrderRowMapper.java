package com.epam.esm.dao.mapper;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

/**
 * Database RowMapper for TagEntity model
 */
@Slf4j
@Component
public class OrderRowMapper implements RowMapper<Order> {

  private static final String ORDER_COST_PARAMETER = "cost";
  private static final String ORDER_ID_PARAMETER = "id";
  private static final String ORDER_CREATE_DAY_PARAMETER = "date";
  private static final String LOG_MAP_ROW_START = "mapRow() called";
  private static final String LOG_MAP_ROW_END = "mapRow() return: ";

  /**
   * Converts provided resultSet and returns OrderEntity object.
   *
   * @param rs
   * @param rowNum
   * @return
   * @throws SQLException
   */
  @Override
  public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
    log.debug(LOG_MAP_ROW_START);
    Order order = new Order();
    order.setId(rs.getInt(ORDER_ID_PARAMETER));
    order.setCost(rs.getDouble(ORDER_COST_PARAMETER));

    String createDayString = rs.getString(ORDER_CREATE_DAY_PARAMETER);
    order.setDate(LocalDateTime.parse(createDayString));

    int userId = rs.getInt("user_id");
    User user = new User();
    user.setId(userId);
    order.setUser(user);

    int certificateId = rs.getInt("gift_certificate_id");
    Certificate certificate = new Certificate();
    certificate.setId(certificateId);
    order.setCertificate(certificate);

    log.debug(LOG_MAP_ROW_END + order);
    return order;
  }
}
