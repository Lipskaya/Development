package com.epam.esm.service;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Sort;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.mapper.CertificateEntityMapper;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CertificateEntity Service
 */
@Service
@Transactional
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
  private static final String LOG_GET_TOTAL_ITEMS_START = "getTotalItems() called";
  private static final String LOG_GET_TOTAL_ITEMS_END = "getTotalItems() return: ";


  @Autowired
  private CertificateDao certificateDao;
  @Autowired
  private TagDao tagDao;
  @Autowired
  private CertificateEntityMapper certificateEntityMapper;
  @Autowired
  private CertificateRepository certificateRepository;
  @Autowired
  private TagRepository tagRepository;

//  @Autowired
//  public CertificateService(CertificateDao certificateDao, TagDao tagDao, CertificateEntityMapper certificateEntityMapper) {
//    this.certificateDao = certificateDao;
//    this.tagDao = tagDao;
//    this.certificateEntityMapper = certificateEntityMapper;
//  }

  /**
   * Finds all Certificates
   *
   * @param sortRequest
   * @return list of certificate
   */
  public Page<Certificate> getAll(Sort sortRequest) {
    log.debug(LOG_GET_ALL_START);
    Pageable pageable = PageRequest.of(sortRequest.getPaginationOffset() - 1, sortRequest.getPaginationLimit(), getSort(sortRequest));

    if (sortRequest.getSearch() != null){
      Page<Certificate> searchPage = certificateRepository.search(sortRequest.getSearch(),pageable);
      return searchPage;
    }
    if (!sortRequest.getTags().isEmpty()){
      Page<Certificate> certificateTagsPage = certificateRepository.searchByTags(sortRequest.getTags(), sortRequest.getTags().size(), pageable);
      return certificateTagsPage;
    }
    else {
      //List<Certificate> certificates = certificateDao.getAll(sortRequest);//empty tags

      Page<Certificate> certificatesPage = certificateRepository.findAll(pageable);
//    for (Certificate certificate : certificatesPage) {
//      List<Tag> tags = tagDao.getTagsByCertificate(certificate);
//      certificate.setTags(tags);
//    }

//    List<CertificateEntity> certificateEntities = new ArrayList<>();
//    for (Certificate certificate: certificatesPage) {
//      CertificateEntity certificateEntity = certificateEntityMapper.mapToEntity(certificate);
//      certificateEntities.add(certificateEntity);
//    }

      log.debug(LOG_GET_ALL_END + certificatesPage);
      return certificatesPage;
    }

  }

  private org.springframework.data.domain.Sort getSort(Sort sortRequest){
    org.springframework.data.domain.Sort sort;
    if ("DSC".equals(sortRequest.getDirection())){
      sort = org.springframework.data.domain.Sort.by(sortRequest.getSortField()).descending();
    }
    else {
      sort = org.springframework.data.domain.Sort.by(sortRequest.getSortField()).ascending();
    }
    return sort;
  }

  /**
   * Retrieves the CertificateEntity by id
   *
   * @param certificateId
   * @return certificate
   */
  public Certificate getById(int certificateId) {
    log.debug(LOG_GET_BY_ID_START + certificateId);
    //Certificate certificate = certificateDao.getById(certificateId);//empty tags
    Certificate certificate = certificateRepository.getById(certificateId);
//    List<Tag> tags = tagDao.getTagsByCertificate(certificate);
//    certificate.setTags(tags);
//    CertificateEntity certificateEntity = certificateEntityMapper.mapToEntity(certificate);
    log.debug(LOG_GET_BY_ID_END + certificate);
    return certificate;
  }

  /**
   * Saves a new certificate
   *
   * @param certificate
   * @return certificate
   */
  public Certificate insert(Certificate certificate) {
    log.debug(LOG_INSERT_START + certificate);

    //get all tags by id from DB
    // if tag has no ID - create tew tag

    Set<Tag> correctTags = new  HashSet<>();
    for (Tag tag: certificate.getTags()) {
      if (tag.getId() == null){
        Tag savedTag = tagRepository.save(tag);
        correctTags.add(savedTag);
      }
      else {
        Tag correctTag = tagRepository.getById(tag.getId());
        correctTags.add(correctTag);
      }
    }
    //replace certificate tags with correct values
    certificate.setTags(correctTags);

    LocalDateTime now = LocalDateTime.now();
    certificate.setCreateDay(now);
    certificate.setLastUpdateDate(now);

//    certificate.setId(null);
    //Certificate newCertificate = certificateDao.insert(certificate);
    Certificate newCertificate = certificateRepository.saveAndFlush(certificate);
 //TODO: uncomment it to check transaction
//    boolean isThrow = true;
//    if (isThrow){
//      throw  new RuntimeException("transaction check");
//    }

    newCertificate = getById(newCertificate.getId());//to get correct tag names
    //tagDao.updateCertificateTags(newCertificate);
    //CertificateEntity certificateEntity = certificateEntityMapper.mapToEntity(newCertificate);
    log.debug(LOG_INSERT_END + newCertificate);
    return newCertificate;
  }

  /**
   * Updates the certificate
   *
   * @param certificate
   * @param deleteTags
   * @return certificate
   */

  public Certificate update(Certificate certificate, boolean deleteTags) {
    log.debug(LOG_UPDATE_START + certificate);
    LocalDateTime updateDate = LocalDateTime.now();
    certificate.setLastUpdateDate(updateDate);
    //certificateDao.update(certificate);

    Certificate oldCertificate = getById(certificate.getId());//

//    String name = oldCertificate.getName();
//    String description = oldCertificate.getDescription();
//    double price = ;
//    Integer duration = ;

    if (certificate.getName() == null) {
      certificate.setName(oldCertificate.getName());
    }
    if (certificate.getDescription() == null) {
      certificate.setDescription(oldCertificate.getDescription());
    }
    if (certificate.getPrice() == null) {
      certificate.setPrice(oldCertificate.getPrice());
    }
    if (certificate.getDuration() == null) {
      certificate.setDuration(oldCertificate.getDuration());
    }
    if (certificate.getTags().isEmpty()){
      certificate.setTags(oldCertificate.getTags());
    }
    if (deleteTags){
      certificate.setTags(new HashSet<>());
    }
    certificate.setCreateDay(oldCertificate.getCreateDay());

//    Certificate updateCertificate = certificateDao.getById(certificate.getId());
//    tagDao.updateCertificateTags(updateCertificate);
    Certificate updateCertificate = certificateRepository.saveAndFlush(certificate);
    //tagRepository.updateCertificateTags(updateCertificate);

    log.debug(LOG_UPDATE_END + updateCertificate);
    return updateCertificate;
  }

  /**
   * Deletes a certificate
   *
   * @param certificateId
   */
  public void delete(int certificateId) {
    log.debug(LOG_DELETE_START + certificateId);
    Certificate cert = new Certificate();
    cert.setId(certificateId);
    //certificateDao.delete(cert);
    certificateRepository.delete(cert);
    log.debug(LOG_DELETE_END + certificateId);
  }

  /**
   * Get total number of certificates
   *
   * @return int number of certificates
   */
  public int getTotalItems() {
    log.debug(LOG_GET_TOTAL_ITEMS_START);
    int total = certificateDao.getTotalItems();
    log.debug(LOG_GET_TOTAL_ITEMS_END + total);
    return total;
  }
}
