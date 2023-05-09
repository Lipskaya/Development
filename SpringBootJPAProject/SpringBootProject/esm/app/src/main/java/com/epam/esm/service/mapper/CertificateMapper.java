package com.epam.esm.service.mapper;

import com.epam.esm.model.dto.CertificateDTO;
import com.epam.esm.model.dto.TagDTO;
import com.epam.esm.repository.entity.CertificateEntity;
import com.epam.esm.repository.entity.TagEntity;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CertificateMapper {
  private final TagMapper tagMapper;

  /**
   * Convert CertificateEntity to CertificateDTO
   * @param certificateEntity
   * @return certificateDTO
   */
  public CertificateDTO mapToDTO(CertificateEntity certificateEntity) {
    CertificateDTO certificateDTO = new CertificateDTO();
    certificateDTO.setId(certificateEntity.getId());
    certificateDTO.setCreateDay(certificateEntity.getCreateDay());
    certificateDTO.setLastUpdateDate(certificateEntity.getLastUpdateDate());
    certificateDTO.setDescription(certificateEntity.getDescription());
    certificateDTO.setDuration(certificateEntity.getDuration());
    certificateDTO.setName(certificateEntity.getName());
    certificateDTO.setPrice(certificateEntity.getPrice());
    Set<TagDTO> tagEntities = certificateEntity.getTagEntities().stream()
        .map(tagMapper::mapToDTO)
        .collect(Collectors.toSet());
    certificateDTO.setTags(tagEntities);
    return certificateDTO;
  }

  /**
   * Convert CertificateDTO to CertificateEntity
   * @param certificateDTO
   * @return certificateEntity
   */
  public CertificateEntity mapToEntity(CertificateDTO certificateDTO) {
    CertificateEntity certificateEntity = new CertificateEntity();
    certificateEntity.setId(certificateDTO.getId());
    certificateEntity.setCreateDay(certificateDTO.getCreateDay());
    certificateEntity.setLastUpdateDate(certificateDTO.getLastUpdateDate());
    certificateEntity.setDescription(certificateDTO.getDescription());
    certificateEntity.setDuration(certificateDTO.getDuration());
    certificateEntity.setName(certificateDTO.getName());
    certificateEntity.setPrice(certificateDTO.getPrice());
    Set<TagEntity> tagEntities = certificateDTO.getTags().stream()
        .map(tagMapper::mapToEntity)
        .collect(Collectors.toSet());
    certificateEntity.setTagEntities(tagEntities);
    return certificateEntity;
  }
}
