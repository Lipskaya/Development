package com.epam.esm.dao;

import com.epam.esm.model.Certificate;

/**
 *
 */
public interface CertificateDao extends Dao<Certificate, Integer> {

  public int getTotalItems();

}
