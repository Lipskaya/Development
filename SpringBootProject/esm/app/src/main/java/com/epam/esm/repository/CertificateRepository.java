package com.epam.esm.repository;


import com.epam.esm.model.Certificate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Integer> {

  @Query("SELECT cert from gift_certificate cert WHERE cert.description LIKE %:keyword% OR cert.name LIKE %:keyword%")
  public Page<Certificate> search (@Param("keyword") String keyword, Pageable pageable);

  @Query("SELECT cert from gift_certificate cert join cert.tags t WHERE (t.name in :tagNames) group by cert.id having count(cert.id) = :tagCount")
  public Page<Certificate> searchByTags(@Param("tagNames") List<String> tagNames, @Param("tagCount") long tagCount, Pageable pageable);
}
