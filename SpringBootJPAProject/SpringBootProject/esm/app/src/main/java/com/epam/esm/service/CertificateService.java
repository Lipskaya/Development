package com.epam.esm.service;

import com.epam.esm.model.dto.CertificateDTO;
import com.epam.esm.exception.ErrorCode;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.repository.SearchCriteria;
import com.epam.esm.repository.entity.CertificateEntity;
import com.epam.esm.repository.entity.TagEntity;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.response.CertificateListResponse;
import com.epam.esm.service.mapper.CertificateMapper;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CertificateDTO Service
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class CertificateService {

  private static final String LOG_DELETE_START = "delete() called for certificate";
  private static final String LOG_DELETE_END = "delete() end for certificate: ";
  private static final String LOG_UPDATE_START = "update() called";
  private static final String LOG_UPDATE_END = "update() return: ";
  private static final String LOG_INSERT_START = "insert() called";
  private static final String LOG_INSERT_END = "insert() return: ";
  private static final String LOG_GET_BY_ID_START = "getById() called";
  private static final String LOG_GET_BY_ID_END = "getById() return: ";
  private static final String LOG_GET_ALL_START = "getAll() called";
  private static final String LOG_GET_ALL_END = "getAll() return: ";
  private static final String CERTIFICATE_NOT_FOUND_START = "Requested CertificateEntity not found (id = ";
  private static final String END = ")";

  private final CertificateMapper certificateMapper;
  private final CertificateRepository certificateRepository;
  private final TagRepository tagRepository;


  /**
   * Finds all Certificates
   *
   * @param searchCriteriaRequest
   * @return list of certificate
   */
  public CertificateListResponse getAll(SearchCriteria searchCriteriaRequest) {
    log.debug(LOG_GET_ALL_START);
    Page<CertificateEntity> certificatesPage = getCertificatePage(searchCriteriaRequest);
    List<CertificateDTO> certificateDTOS = certificatesPage.stream()
        .map(certificateMapper::mapToDTO)
        .collect(Collectors.toList());
    CertificateListResponse certificateListResponse = new CertificateListResponse();
    certificateListResponse.setCertificates(certificateDTOS);
    certificateListResponse.setPage(searchCriteriaRequest.getPage());
    certificateListResponse.setItemsPerPage(searchCriteriaRequest.getItemsPerPage());
    certificateListResponse.setTotalItems(certificatesPage.getTotalElements());
    log.debug(LOG_GET_ALL_END + certificatesPage);
    return certificateListResponse;
  }

  private Page<CertificateEntity> getCertificatePage(SearchCriteria searchCriteriaRequest){
    Pageable pageable = PageRequest
        .of(searchCriteriaRequest.getPage() - 1, searchCriteriaRequest.getItemsPerPage(), getSort(
            searchCriteriaRequest));
    if (searchCriteriaRequest.getSearch() != null) {
      return certificateRepository.search(searchCriteriaRequest.getSearch(), pageable);
    }
    if (searchCriteriaRequest.getTags() != null && !searchCriteriaRequest.getTags().isEmpty()) {
      return certificateRepository
          .searchByTags(searchCriteriaRequest.getTags(), searchCriteriaRequest.getTags().size(), pageable);
    } else {
      return certificateRepository.findAll(pageable);
    }
  }

  private Sort getSort(SearchCriteria searchCriteriaRequest) {
    Sort sort;
    if ("DSC".equals(searchCriteriaRequest.getDirection())) {
      sort = Sort.by(searchCriteriaRequest.getSortField()).descending();
    } else {
      sort = Sort.by(searchCriteriaRequest.getSortField()).ascending();
    }
    return sort;
  }

  /**
   * Retrieves the CertificateDTO by id
   *
   * @param certificateId
   * @return certificate
   */
  public CertificateDTO getById(int certificateId) {
    log.debug(LOG_GET_BY_ID_START + certificateId);
    try {
      CertificateEntity certificateEntity = certificateRepository.getById(certificateId);
      CertificateDTO certificateDTO = certificateMapper.mapToDTO(certificateEntity);
      log.debug(LOG_GET_BY_ID_END + certificateDTO);
      return certificateDTO;
    } catch (EntityNotFoundException exception) {
      throw new NotFoundException(CERTIFICATE_NOT_FOUND_START + certificateId + END,
          ErrorCode.ERROR_CERTIFICATE);
    }
  }

  /**
   * Saves a new certificate
   *
   * @param certificateDto
   * @return certificate
   */
  @Transactional
  public CertificateDTO insert(CertificateDTO certificateDto) {
    log.debug(LOG_INSERT_START + certificateDto);
    CertificateEntity certificateEntity = certificateMapper.mapToEntity(certificateDto);
    Set<TagEntity> correctTagEntities = new HashSet<>();
    for (TagEntity tagEntity : certificateEntity.getTagEntities()) {
      if (tagEntity.getId() == null) {
        TagEntity savedTagEntity = tagRepository.save(tagEntity);
        correctTagEntities.add(savedTagEntity);
      } else {
        TagEntity correctTagEntity = tagRepository.getById(tagEntity.getId());
        correctTagEntities.add(correctTagEntity);
      }
    }
    certificateEntity.setTagEntities(correctTagEntities);
    LocalDateTime now = LocalDateTime.now();
    certificateEntity.setCreateDay(now);
    certificateEntity.setLastUpdateDate(now);
    CertificateEntity newCertificateEntity = certificateRepository.saveAndFlush(certificateEntity);
    CertificateDTO certificateDTO = getById(newCertificateEntity.getId());//to get correct tag names
    log.debug(LOG_INSERT_END + certificateDTO);
    return certificateDTO;
  }

  /**
   * Updates the certificate
   *
   * @param certificateDto
   * @param deleteTags
   * @return certificate
   */
  @Transactional
  public CertificateDTO update(CertificateDTO certificateDto, boolean deleteTags) {
    log.debug(LOG_UPDATE_START + certificateDto);
    CertificateDTO oldCertificateDto = getById(certificateDto.getId());//
    CertificateEntity oldCertificateEntity = certificateMapper.mapToEntity(oldCertificateDto);
    CertificateEntity certificateEntity = certificateMapper.mapToEntity(certificateDto);
    LocalDateTime updateDate = LocalDateTime.now();
    certificateEntity.setLastUpdateDate(updateDate);
    if (certificateEntity.getName() == null) {
      certificateEntity.setName(oldCertificateEntity.getName());
    }
    if (certificateEntity.getDescription() == null) {
      certificateEntity.setDescription(oldCertificateEntity.getDescription());
    }
    if (certificateEntity.getPrice() == null) {
      certificateEntity.setPrice(oldCertificateEntity.getPrice());
    }
    if (certificateEntity.getDuration() == null) {
      certificateEntity.setDuration(oldCertificateEntity.getDuration());
    }
    if (certificateEntity.getTagEntities().isEmpty()) {
      certificateEntity.setTagEntities(oldCertificateEntity.getTagEntities());
    }
    if (deleteTags) {
      certificateEntity.setTagEntities(new HashSet<>());
    }
    certificateEntity.setCreateDay(oldCertificateDto.getCreateDay());
    CertificateEntity updateCertificateEntity = certificateRepository.saveAndFlush(
        certificateEntity);
    CertificateDTO certificateDTO = certificateMapper.mapToDTO(updateCertificateEntity);
    log.debug(LOG_UPDATE_END + certificateDTO);
    return certificateDTO;
  }

  /**
   * Deletes a certificate
   *
   * @param certificateId
   */
  public void delete(int certificateId) {
    log.debug(LOG_DELETE_START + certificateId);
    CertificateEntity cert = new CertificateEntity();
    cert.setId(certificateId);
    certificateRepository.delete(cert);
    log.debug(LOG_DELETE_END + certificateId);
  }
}
