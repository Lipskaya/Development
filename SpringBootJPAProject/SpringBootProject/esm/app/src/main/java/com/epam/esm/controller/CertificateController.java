package com.epam.esm.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.epam.esm.model.dto.CertificateDTO;
import com.epam.esm.repository.SearchCriteria;
import com.epam.esm.model.validation.CertificatePostValidation;
import com.epam.esm.model.validation.CertificatePutValidation;
import com.epam.esm.response.CertificateListResponse;
import com.epam.esm.service.CertificateService;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

/**
 * Rest Controller to process CertificateDTO - related HTTP requests
 */
@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
public class CertificateController {

  private static final String LOG_DELETE_START = "deleteCertificate() called for certificate id";
  private static final String LOG_DELETE_END = "deleteCertificate() end for certificate id: ";
  private static final String LOG_UPDATE_START = "updateCertificate() called";
  private static final String LOG_UPDATE_END = "updateCertificate() return: ";
  private static final String LOG_ADD_CERTIFICATE_START = "addCertificate() called";
  private static final String LOG_ADD_CERTIFICATE_END = "addCertificate() return: ";
  private static final String LOG_GET_CERTIFICATE_START = "getCertificateEntity() called";
  private static final String LOG_GET_CERTIFICATE_END = "getCertificateEntity() return: ";
  private static final String LOG_GET_CERTIFICATES_START = "getCertificates() called";
  private static final String LOG_GET_CERTIFICATES_END = "getCertificates() return: ";

  private final CertificateService certificateService;

  /**
   * Search all certificates.
   *
   * @return list of certificate
   */
  @ApiOperation("Get all Certificates")
  @GetMapping(value = "/v1/certificates", produces = {MediaType.APPLICATION_JSON_VALUE})
  public CertificateListResponse getCertificates(
      @RequestParam(required = false) String search,
      @RequestParam(required = false) List<String> tagEntities,
      @RequestParam(required = false, defaultValue = "name") @Pattern(regexp = "^(name|description|price|createDay|lastUpdateDate)?", message = "Only name, description, price, create_day,last_update_date values for 'sort' are allowed") String sort,
      @RequestParam(required = false, defaultValue = "ASC") @Pattern(regexp = "^(ASC|DSC)?", message = "Only ASC,DSC values for 'direction' are allowed") String direction,
      @RequestParam(required = false, defaultValue = "5") @Min(value = 1, message = "itemsPerPage Min value should be 1") @Max(value = 20, message = "itemsPerPage should Not be more than 20") int itemsPerPage,
      @RequestParam(required = false, defaultValue = "1") @Min(value = 1, message = "page Min value should be 1") int page) {
    log.debug(LOG_GET_CERTIFICATES_START);
    SearchCriteria searchCriteriaRequest = new SearchCriteria();
    searchCriteriaRequest.setSortField(sort);
    searchCriteriaRequest.setDirection(direction);
    searchCriteriaRequest.setItemsPerPage(itemsPerPage);
    searchCriteriaRequest.setPage(page);
    searchCriteriaRequest.setSearch(search);
    searchCriteriaRequest.setTags(tagEntities);
    CertificateListResponse certificateListResponse = certificateService.getAll(
        searchCriteriaRequest);
    certificateListResponse.getCertificates().forEach(this::populateLinks);//call populateLinks() on each elements of certificates

    certificateListResponse.add(
        linkTo(methodOn(CertificateController.class).getCertificates(search, tagEntities,sort, direction, itemsPerPage, page +1))
            .withSelfRel());

    log.debug(LOG_GET_CERTIFICATES_END + certificateListResponse);
    return certificateListResponse;
  }

  /**
   * Search one certificate by id
   *
   * @param certificateId
   * @return the certificate
   */
  @ApiOperation("Get CertificateDTO by id")
  @GetMapping(value = "/v1/certificates/{certificateId}", produces = {
      MediaType.APPLICATION_JSON_VALUE})
  public CertificateDTO getCertificate(
      @PathVariable("certificateId") @Min(value = 1, message = "ID should be positive") int certificateId) {
    log.debug(LOG_GET_CERTIFICATE_START + certificateId);
    CertificateDTO certificateDTO = certificateService.getById(certificateId);
    populateLinks(certificateDTO);
    log.debug(LOG_GET_CERTIFICATE_END + certificateDTO);
    return certificateDTO;
  }

  /**
   * Saves new certificate
   *
   * @param certificate
   * @return certificate
   */
  @ApiOperation("Create new CertificateDTO")
  @PostMapping(value = "/v1/certificates", produces = {MediaType.APPLICATION_JSON_VALUE})
  public CertificateDTO addCertificate(
      @Validated(CertificatePostValidation.class) @RequestBody CertificateDTO certificate) {
    log.debug(LOG_ADD_CERTIFICATE_START + certificate);
    CertificateDTO certificateDTO = certificateService.insert(certificate);
    populateLinks(certificateDTO);
    log.debug(LOG_ADD_CERTIFICATE_END + certificateDTO);
    return certificateDTO;
  }

  /**
   * Updates a certificate
   *
   * @param certificate
   * @return certificate
   */
  @ApiOperation("Update CertificateDTO")
  @PutMapping(value = "/v1/certificates/{certificateId}", produces = {
      MediaType.APPLICATION_JSON_VALUE})
  public CertificateDTO updateCertificate(
      @RequestParam(required = false, defaultValue = "false") boolean deleteTags,
      @PathVariable("certificateId") @Min(value = 1, message = "Min value 1") int certificateId,
      @Validated(CertificatePutValidation.class) @RequestBody CertificateDTO certificate) {
    log.debug(LOG_UPDATE_START + certificateId);
    certificate.setId(certificateId);
    CertificateDTO certificateDTO = certificateService.update(certificate, deleteTags);
    populateLinks(certificateDTO);
    log.debug(LOG_UPDATE_END + certificateDTO);
    return certificateDTO;
  }

  /**
   * Deletes certificate
   *
   * @param certificateId
   */
  @ApiOperation("Delete CertificateDTO by id")
  @DeleteMapping(value = "/v1/certificates/{certificateId}", produces = {
      MediaType.APPLICATION_JSON_VALUE})
  public void deleteCertificate(
      @PathVariable("certificateId") @Min(value = 1, message = "Min value 1") int certificateId) {
    log.debug(LOG_DELETE_START + certificateId);
    certificateService.delete(certificateId);
    log.debug(LOG_DELETE_END + certificateId);
  }

  private void populateLinks(CertificateDTO certificateDTO) {
    certificateDTO.add(
        linkTo(methodOn(CertificateController.class).getCertificate(certificateDTO.getId()))
            .withSelfRel());
    certificateDTO.getTags().forEach(tagEntity -> tagEntity.add(linkTo(methodOn(
        TagController.class).getTag(tagEntity.getId())).withSelfRel()));
  }
}
