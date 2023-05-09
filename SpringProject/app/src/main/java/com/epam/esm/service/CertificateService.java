package com.epam.esm.service;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Sort;
import com.epam.esm.model.Tag;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Certificate Service
 */
@Service
@Transactional
public class CertificateService {

  private static final Logger LOGGER = Logger
      .getLogger(CertificateService.class.getCanonicalName());
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
  private static final String LOG_GET_TOTAL_ITEMS_START = "getTotalItems() called";
  private static final String LOG_GET_TOTAL_ITEMS_END = "getTotalItems() return: ";

  private final CertificateDao certificateDao;
  private final TagDao tagDao;

  @Autowired
  public CertificateService(CertificateDao certificateDao, TagDao tagDao) {
    this.certificateDao = certificateDao;
    this.tagDao = tagDao;
  }

  /**
   * Finds all Certificates
   *
   * @param sortRequest
   * @return list of certificate
   */
  public List<Certificate> getAll(Sort sortRequest) {
    LOGGER.debug(LOG_GET_ALL_START);
    List<Certificate> certificates = certificateDao.getAll(sortRequest);//empty tags
    for (Certificate certificate : certificates) {
      List<Tag> tags = tagDao.getTagsByCertificate(certificate);
      certificate.setTags(tags);
    }
    LOGGER.debug(LOG_GET_ALL_END + certificates);
    return certificates;
  }

  /**
   * Retrieves the Certificate by id
   *
   * @param certificateId
   * @return certificate
   */
  public Certificate getById(int certificateId) {
    LOGGER.debug(LOG_GET_BY_ID_START + certificateId);
    Certificate certificate = certificateDao.getById(certificateId);//empty tags
    List<Tag> tags = tagDao.getTagsByCertificate(certificate);
    certificate.setTags(tags);
    LOGGER.debug(LOG_GET_BY_ID_END + certificate);
    return certificate;
  }

  /**
   * Saves a new certificate
   *
   * @param certificate
   * @return certificate
   */
  public Certificate insert(Certificate certificate) {
    LOGGER.debug(LOG_INSERT_START + certificate);
    LocalDateTime now = LocalDateTime.now();
    certificate.setCreateDay(now);
    certificate.setLastUpdateDate(now);
    Certificate newCertificate = certificateDao.insert(certificate);
 //TODO: uncomment it to check transaction
//    boolean isThrow = true;
//    if (isThrow){
//      throw  new RuntimeException("transaction check");
//    }

    tagDao.updateCertificateTags(newCertificate);
    LOGGER.debug(LOG_INSERT_END + newCertificate);
    return newCertificate;
  }

  /**
   * Updates the certificate
   *
   * @param certificate
   * @return certificate
   */

  public Certificate update(Certificate certificate) {
    LOGGER.debug(LOG_UPDATE_START + certificate);
    LocalDateTime updateDate = LocalDateTime.now();
    certificate.setLastUpdateDate(updateDate);
    certificateDao.update(certificate);
    Certificate updateCertificate = certificateDao.getById(certificate.getId());
    tagDao.updateCertificateTags(updateCertificate);
    LOGGER.debug(LOG_UPDATE_END + updateCertificate);
    return updateCertificate;
  }

  /**
   * Deletes a certificate
   *
   * @param certificateId
   */
  public void delete(int certificateId) {
    LOGGER.debug(LOG_DELETE_START + certificateId);
    Certificate cert = new Certificate();
    cert.setId(certificateId);
    certificateDao.delete(cert);
    LOGGER.debug(LOG_DELETE_END + certificateId);
  }

  /**
   * Get total number of certificates
   *
   * @return int number of certificates
   */
  public int getTotalItems() {
    LOGGER.debug(LOG_GET_TOTAL_ITEMS_START);
    int total = certificateDao.getTotalItems();
    LOGGER.debug(LOG_GET_TOTAL_ITEMS_END + total);
    return total;
  }
}
