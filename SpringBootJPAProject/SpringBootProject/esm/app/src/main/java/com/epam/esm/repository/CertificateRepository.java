package com.epam.esm.repository;

import com.epam.esm.repository.entity.CertificateEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRepository extends JpaRepository<CertificateEntity, Integer> {

  @Query("SELECT cert FROM gift_certificate cert WHERE cert.description LIKE %:keyword% OR cert.name LIKE %:keyword%")
   Page<CertificateEntity> search(@Param("keyword") String keyword, Pageable pageable);

  @Query("SELECT cert FROM gift_certificate cert JOIN cert.tagEntities t WHERE (t.name in :tagNames) GROUP BY cert.id HAVING COUNT(cert.id) = :tagCount")
   Page<CertificateEntity> searchByTags(@Param("tagNames") List<String> tagNames,
      @Param("tagCount") long tagCount, Pageable pageable);
}
