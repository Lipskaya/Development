package com.epam.esm.response;

import com.epam.esm.entity.CertificateEntity;
import com.epam.esm.model.Certificate;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response model for paginated list of certificates
 */
@NoArgsConstructor
@Data
public class CertificateListResponse {
  private int page;
  private int itemsPerPage;
  private long totalItems;
  private List<CertificateEntity> certificateEntities;
}
