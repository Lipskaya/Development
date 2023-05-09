package com.epam.esm.controller;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.CertificateListResponse;
import com.epam.esm.model.Sort;
import com.epam.esm.service.CertificateService;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Rest Controller to process Certificate - related HTTP requests
 */
@RestController
@Validated
public class CertificateController {



  private static final Logger LOGGER = Logger
      .getLogger(CertificateController.class.getCanonicalName());
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

  private final CertificateService certificateService;

  @Autowired
  public CertificateController(CertificateService certificateService) {
    this.certificateService = certificateService;
  }
  /**
   * Search all certificates.
   *
   * @return list of certificate
   */
  @GetMapping(value = "/certificates", produces = {MediaType.APPLICATION_JSON_VALUE})
  public CertificateListResponse getCertificates(
      @RequestParam(required = false) String search,
      @RequestParam(required = false) String tag,
      @RequestParam(required = false, defaultValue = "name") @Pattern(regexp = "^(name|description|price|create_day|last_update_date)?", message = "Only name, description, price, create_day,last_update_date values for 'sort' are allowed") String sort,
      @RequestParam(required = false, defaultValue = "ASC") @Pattern(regexp = "^(ASC|DSC)?", message = "Only ASC,DSC values for 'direction' are allowed") String direction,
      @RequestParam(required = false, defaultValue = "5") @Min(value = 1, message = "itemsPerPage Min value should be 1") @Max(value = 20, message = "itemsPerPage should Not be more than 20") int itemsPerPage,
      @RequestParam(required = false, defaultValue = "1") @Min(value = 1, message = "page Min value should be 1") int page) {
    LOGGER.debug(LOG_GET_CERTIFICATES_START);

    Sort sortRequest = new Sort();
    sortRequest.setSortField(sort);
    sortRequest.setDirection(direction);
    sortRequest.setPaginationLimit(itemsPerPage);
    sortRequest.setPaginationOffset(page);
    sortRequest.setSearch(search);
    sortRequest.setTag(tag);

    List<Certificate> certificates = certificateService.getAll(sortRequest);
    LOGGER.debug(LOG_GET_CERTIFICATES_END + certificates);
    CertificateListResponse certificateListResponse = new CertificateListResponse();
    certificateListResponse.setCertificates(certificates);
    certificateListResponse.setPage(page);
    certificateListResponse.setItemsPerPage(itemsPerPage);
    certificateListResponse.setTotalItems(certificateService.getTotalItems());
    LOGGER.debug(LOG_GET_CERTIFICATES_END + certificateListResponse);
    return certificateListResponse;
  }

  /**
   * Search one certificate by id
   *
   * @param certificateId
   * @return the certificate
   */
  @GetMapping(value = "/certificates/{certificateId}", produces = {
      MediaType.APPLICATION_JSON_VALUE})
  public Certificate getCertificate(@PathVariable("certificateId") @Min(value = 1, message = "ID should be positive") int certificateId) {
    LOGGER.debug(LOG_GET_CERTIFICATE_START + certificateId);
    Certificate certificate = certificateService.getById(certificateId);
    LOGGER.debug(String.format(LOG_GET_CERTIFICATE_END + certificate));
    return certificate;
  }

  /**
   * Saves new certificate
   *
   * @param certificate
   * @return certificate
   */
  @PostMapping(value = "/certificates", produces = {MediaType.APPLICATION_JSON_VALUE})
  public Certificate addCertificate(@Valid @RequestBody Certificate certificate) {
    LOGGER.debug(LOG_ADD_CERTIFICATE_START + certificate);
    Certificate newCertificate = certificateService.insert(certificate);
    LOGGER.debug(LOG_ADD_CERTIFICATE_END + newCertificate);
    return newCertificate;
  }

  /**
   * Updates a certificate
   *
   * @param certificate
   * @return certificate
   */
  @PutMapping(value = "/certificates", produces = {MediaType.APPLICATION_JSON_VALUE})
  public Certificate updateCertificate(@Valid @RequestBody Certificate certificate) {
    LOGGER.debug(LOG_UPDATE_START);
    Certificate updateCertificate = certificateService.update(certificate);
    LOGGER.debug(LOG_UPDATE_END + updateCertificate);
    return updateCertificate;
  }

  /**
   * Deletes certificate
   *
   * @param certificateId
   */
  @DeleteMapping(value = "/certificates/{certificateId}", produces = {
      MediaType.APPLICATION_JSON_VALUE})
  public void deleteCertificate(@PathVariable("certificateId") @Min(value = 1, message = "Min value 1") int certificateId) {
    LOGGER.debug(LOG_DELETE_START + certificateId);
    certificateService.delete(certificateId);
    LOGGER.debug(LOG_DELETE_END + certificateId);
  }
}
