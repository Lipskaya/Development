package com.epam.esm.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.epam.esm.entity.CertificateEntity;
import com.epam.esm.entity.TagEntity;
import com.epam.esm.model.Certificate;
import com.epam.esm.response.CertificateListResponse;
import com.epam.esm.model.Sort;
import com.epam.esm.model.validation.CertificatePostValidation;
import com.epam.esm.model.validation.CertificatePutValidation;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.mapper.CertificateEntityMapper;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiOperation;

/**
 * Rest Controller to process CertificateEntity - related HTTP requests
 */
@RestController
@Validated
@Slf4j
public class CertificateController {

  private static final String LOG_DELETE_START = "deleteCertificate() called for certificate id";
  private static final String LOG_DELETE_END = "deleteCertificate() end for certificate id: ";
  private static final String LOG_UPDATE_START = "updateCertificate() called";
  private static final String LOG_UPDATE_END = "updateCertificate() return: ";
  private static final String LOG_ADD_CERTIFICATE_START = "addCertificate() called";
  private static final String LOG_ADD_CERTIFICATE_END = "addCertificate() return: ";
  private static final String LOG_GET_CERTIFICATE_START = "getCertificate() called";
  private static final String LOG_GET_CERTIFICATE_END = "getCertificate() return: ";
  private static final String LOG_GET_CERTIFICATES_START = "getCertificates() called";
  private static final String LOG_GET_CERTIFICATES_END = "getCertificates() return: ";

  @Autowired
  private CertificateService certificateService;
  @Autowired
  private CertificateEntityMapper certificateEntityMapper;

//  @Autowired
//  public CertificateController(CertificateService certificateService) {
//    this.certificateService = certificateService;
//  }
  /**
   * Search all certificates.
   *
   * @return list of certificate
   */
  @ApiOperation("Get all Certificates")
  @GetMapping(value = "/v1/certificates", produces = {MediaType.APPLICATION_JSON_VALUE})
  public CertificateListResponse getCertificates(
      @RequestParam(required = false) String search,
      @RequestParam(required = false) List<String> tags,
      @RequestParam(required = false, defaultValue = "name") @Pattern(regexp = "^(name|description|price|createDay|lastUpdateDate)?", message = "Only name, description, price, create_day,last_update_date values for 'sort' are allowed") String sort,
      @RequestParam(required = false, defaultValue = "ASC") @Pattern(regexp = "^(ASC|DSC)?", message = "Only ASC,DSC values for 'direction' are allowed") String direction,
      @RequestParam(required = false, defaultValue = "5") @Min(value = 1, message = "itemsPerPage Min value should be 1") @Max(value = 20, message = "itemsPerPage should Not be more than 20") int itemsPerPage,
      @RequestParam(required = false, defaultValue = "1") @Min(value = 1, message = "page Min value should be 1") int page) {
    log.debug(LOG_GET_CERTIFICATES_START);

    Sort sortRequest = new Sort();
    sortRequest.setSortField(sort);
    sortRequest.setDirection(direction);
    sortRequest.setPaginationLimit(itemsPerPage);
    sortRequest.setPaginationOffset(page);
    sortRequest.setSearch(search);
    sortRequest.setTags(tags);

    Page<Certificate> certificatesPage = certificateService.getAll(sortRequest);

    List<CertificateEntity> certificateEntities = new ArrayList<>();
    for (Certificate certificate: certificatesPage) {
      CertificateEntity certificateEntity = certificateEntityMapper.mapToEntity(certificate);
      populateLinks(certificateEntity);
      certificateEntities.add(certificateEntity);
    }

    CertificateListResponse certificateListResponse = new CertificateListResponse();
    certificateListResponse.setCertificateEntities(certificateEntities);
    certificateListResponse.setPage(page);
    certificateListResponse.setItemsPerPage(itemsPerPage);
    certificateListResponse.setTotalItems(certificatesPage.getTotalElements());
    log.debug(LOG_GET_CERTIFICATES_END + certificateListResponse);
    return certificateListResponse;
  }

  private void populateLinks(CertificateEntity certificateEntity) {
    certificateEntity.add(linkTo(methodOn(CertificateController.class).getCertificate(certificateEntity.getId())).withSelfRel());
    for (TagEntity tagEntity : certificateEntity.getTagEntities()) {
      tagEntity.add(linkTo(methodOn(TagController.class).getTag(tagEntity.getId())).withSelfRel());
    }
  }

  /**
   * Search one certificate by id
   *
   * @param certificateId
   * @return the certificate
   */
  @ApiOperation("Get CertificateEntity by id")
  @GetMapping(value = "/v1/certificates/{certificateId}", produces = {
      MediaType.APPLICATION_JSON_VALUE})
  public CertificateEntity getCertificate(@PathVariable("certificateId") @Min(value = 1, message = "ID should be positive") int certificateId) {
    log.debug(LOG_GET_CERTIFICATE_START + certificateId);
    Certificate certificate = certificateService.getById(certificateId);
    CertificateEntity certificateEntity = certificateEntityMapper.mapToEntity(certificate);
    populateLinks(certificateEntity);
    log.debug(LOG_GET_CERTIFICATE_END + certificateEntity);
    return certificateEntity;
  }

  /**
   * Saves new certificate
   *
   * @param certificate
   * @return certificate
   */
  @ApiOperation("Create new CertificateEntity")
  @PostMapping(value = "/v1/certificates", produces = {MediaType.APPLICATION_JSON_VALUE})
  public CertificateEntity addCertificate(
      @Validated(CertificatePostValidation.class) @RequestBody Certificate certificate) {

    log.debug(LOG_ADD_CERTIFICATE_START + certificate);

    Certificate newCertificate = certificateService.insert(certificate);
    CertificateEntity certificateEntity = certificateEntityMapper.mapToEntity(newCertificate);
    populateLinks(certificateEntity);
    log.debug(LOG_ADD_CERTIFICATE_END + certificateEntity);
    return certificateEntity;
  }

  /**
   * Updates a certificate
   *
   * @param certificate
   * @return certificate
   */
  @ApiOperation("Update CertificateEntity")
  @PutMapping(value = "/v1/certificates/{certificateId}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public CertificateEntity updateCertificate(
      @RequestParam(required = false, defaultValue = "false") boolean deleteTags,
      @PathVariable("certificateId") @Min(value = 1, message = "Min value 1") int certificateId,
      @Validated(CertificatePutValidation.class) @RequestBody Certificate certificate) {

    log.debug(LOG_UPDATE_START + certificateId);
    certificate.setId(certificateId);
    Certificate updateCertificate = certificateService.update(certificate, deleteTags);
    CertificateEntity certificateEntity = certificateEntityMapper.mapToEntity(updateCertificate);
    populateLinks(certificateEntity);

    log.debug(LOG_UPDATE_END + certificateEntity);
    return certificateEntity;
  }

  /**
   * Deletes certificate
   *
   * @param certificateId
   */
  @ApiOperation("Delete CertificateEntity by id")
  @DeleteMapping(value = "/v1/certificates/{certificateId}", produces = {
      MediaType.APPLICATION_JSON_VALUE})
  public void deleteCertificate(@PathVariable("certificateId") @Min(value = 1, message = "Min value 1") int certificateId) {
    log.debug(LOG_DELETE_START + certificateId);
    certificateService.delete(certificateId);
    log.debug(LOG_DELETE_END + certificateId);
  }
}
