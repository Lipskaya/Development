package com.epam.esm.response;

import com.epam.esm.model.dto.CertificateDTO;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

/**
 * Response model for paginated list of certificates
 */
@NoArgsConstructor
@Data
public class CertificateListResponse extends RepresentationModel<CertificateListResponse> {
  private int page;
  private int itemsPerPage;
  private long totalItems;
  private List<CertificateDTO> certificates;
}
