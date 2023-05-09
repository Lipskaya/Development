package com.epam.esm.dao;

import com.epam.esm.model.Order;
import com.epam.esm.model.Sort;

public interface OrderDao extends Dao<Order, Integer>{
  int getTotalItems(Sort sortRequest);

}
