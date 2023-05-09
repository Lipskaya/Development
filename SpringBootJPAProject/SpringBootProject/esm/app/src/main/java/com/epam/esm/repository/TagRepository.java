package com.epam.esm.repository;

import com.epam.esm.repository.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Integer> {

  String SQL_GET_FAVORITE_TAG = "select * FROM tag\n"
      + "WHERE tag.id = \n"
      + "(SELECT tag_id \n"
      + "FROM\n"
      + "(SELECT TT.tag_id, count(TT.tag_id) AS occurance FROM\n"
      + "(SELECT tag_id FROM \n"
      + "(SELECT * FROM spring_boot_certificates.gift_certificate\n"
      + "WHERE \n"
      + "spring_boot_certificates.gift_certificate.id IN\n"
      + "(SELECT KK.gift_certificate_id \n"
      + "FROM\n"
      + "(SELECT * FROM spring_boot_certificates.`order`\n"
      + "HAVING \n"
      + "spring_boot_certificates.`order`.user_id = \n"
      + "(SELECT K.max_user_id FROM\n"
      + "(SELECT user_id AS max_user_id, sum(cost) AS total_orders_cost \n"
      + "FROM \n"
      + "spring_boot_certificates.`order`\n"
      + "GROUP BY user_id\n"
      + "ORDER BY total_orders_cost desc\n"
      + "LIMIT 1) AS K)) AS KK)) AS S\n"
      + "JOIN gift_tag ON gift_tag.gift_certificate_id = S.id) AS TT\n"
      + "GROUP BY TT.tag_id\n"
      + "ORDER BY occurance DESC\n"
      + "LIMIT 1) AS MT)";

  @Query(value = SQL_GET_FAVORITE_TAG, nativeQuery = true)
  TagEntity getFavoriteTag();
}
