package com.epam.esm.dao;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import java.util.List;

public interface TagDao extends Dao<Tag, Integer> {

  void updateCertificateTags(Certificate certificate);

  List<Tag> getTagsByCertificate(Certificate certificate);

  int getTotalItems();

  Tag getFavorite();
}
