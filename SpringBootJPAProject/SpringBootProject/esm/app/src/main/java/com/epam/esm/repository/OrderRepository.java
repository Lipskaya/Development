package com.epam.esm.repository;

import com.epam.esm.repository.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

  @Query(value = "SELECT * FROM spring_boot_certificates.`order` WHERE user_id = :userId", nativeQuery = true)
  Page<OrderEntity> findUserOrders(@Param("userId") Integer userId, Pageable pageable);
}
