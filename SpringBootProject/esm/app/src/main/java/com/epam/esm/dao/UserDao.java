package com.epam.esm.dao;

import com.epam.esm.model.User;

public interface UserDao extends Dao<User,Integer>{


  int getTotalItems();
}
