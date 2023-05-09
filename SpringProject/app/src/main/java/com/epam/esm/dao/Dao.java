package com.epam.esm.dao;

import com.epam.esm.model.Sort;
import java.util.List;

public interface Dao<T, I> {

  T getById(I id);

  List<T> getAll(Sort sortRequest);

  T insert(T t);

  T update(T t);

  void delete(T t);
}

