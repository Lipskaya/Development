package com.epam.esm.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Sort;
import com.epam.esm.model.Tag;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DaoTestConfig.class})
class CertificateEntityIntegrationTest {

  private static final int CERTIFICATE_ID_PARAMETER = 1;
  private static final String CERTIFICATE_NAME_PARAMETER = "name_1";
  private static final String CERTIFICATE_DESCRIPTION_PARAMETER = "description_1";
  private static final double CERTIFICATE_PRICE_PARAMETER = 4;
  private static final String DATE_IN_STRING = "2022-01-22T16:56:01.027";
  private static final String TAG_NAME_PARAMETER = "nameTag_3";
  private static final String CERTIFICATE_UPDATE_NAME_PARAMETER = "name_update";
  private static final String CERTIFICATE_DESCRIPTION_UPDATE_PARAMETER = "description_update";
  private static final String CERTIFICATE_INSERTED_NAME_PARAMETER = "name_inserted";
  private static final int CERTIFICATE_DURATION_PARAMETER = 20;

  @Autowired
  CertificateDao certificateDao;

  @Test
  void certificateDaoGetByIdTest() {
    Certificate expectedCertificate = new Certificate();
    expectedCertificate.setId(CERTIFICATE_ID_PARAMETER);
    expectedCertificate.setName(CERTIFICATE_NAME_PARAMETER);
    expectedCertificate.setDescription(CERTIFICATE_DESCRIPTION_PARAMETER);
    expectedCertificate.setPrice(CERTIFICATE_PRICE_PARAMETER);
    expectedCertificate.setDuration(CERTIFICATE_DURATION_PARAMETER);
    String date = DATE_IN_STRING;
    expectedCertificate.setCreateDay(LocalDateTime.parse(date));
    expectedCertificate.setLastUpdateDate(LocalDateTime.parse(date));
    Certificate realCertificate = certificateDao.getById(1);
    assertThat(realCertificate, is(expectedCertificate));
  }

  @Test
  void CertificateDaoGetByAllTest() {
    Sort sort = new Sort();
    sort.setSortField("name");
    sort.setDirection("ASC");
    sort.setPaginationOffset(1);
    sort.setPaginationLimit(10);
    List<Certificate> realCertificateList = certificateDao.getAll(sort);
    assertThat(realCertificateList.size(), is(6));
  }

  @Test
  void certificateDaoDeleteTest() {
    Certificate expectedCertificate = certificateDao.getById(1);
    assertNotNull(expectedCertificate);
    certificateDao.delete(expectedCertificate);
    Sort sort = new Sort();
    sort.setSortField("name");
    sort.setDirection("ASC");
    sort.setPaginationOffset(1);
    sort.setPaginationLimit(10);
    List<Certificate> realCertificateList = certificateDao.getAll(sort);
    assertThat(realCertificateList.size(), is(5));
    assertThat(realCertificateList.contains(expectedCertificate), is(false));
  }

  @Test
  void certificateDaoUpdateTest() {
    Certificate expectedCertificate = certificateDao.getById(2);
    expectedCertificate.setName(CERTIFICATE_UPDATE_NAME_PARAMETER);
    expectedCertificate.setDescription(CERTIFICATE_DESCRIPTION_UPDATE_PARAMETER);
    expectedCertificate.setPrice(4.0);
    String date = DATE_IN_STRING;
    expectedCertificate.setCreateDay(LocalDateTime.parse(date));
    expectedCertificate.setLastUpdateDate(LocalDateTime.parse(date));
    Set<Tag> tags = new HashSet<>();
    Tag tag = new Tag();
    tag.setId(3);
    tag.setName(TAG_NAME_PARAMETER);
    tags.add(tag);
    expectedCertificate.setTags(tags);
    Certificate realCertificate = certificateDao.update(expectedCertificate);
    assertEquals(realCertificate, expectedCertificate);

  }

  @Test
  void certificateDaoInsertTest() {
    Certificate expectedCertificate = new Certificate();
    expectedCertificate.setName(CERTIFICATE_INSERTED_NAME_PARAMETER);
    expectedCertificate.setDescription(CERTIFICATE_DESCRIPTION_PARAMETER);
    expectedCertificate.setPrice(4.0);
    expectedCertificate.setDuration(19);
    String date = DATE_IN_STRING;
    expectedCertificate.setCreateDay(LocalDateTime.parse(date));
    expectedCertificate.setLastUpdateDate(LocalDateTime.parse(date));
    Set<Tag> tags = new HashSet<>();
    Tag tag = new Tag();
    tag.setId(3);
    tag.setName(TAG_NAME_PARAMETER);
    tags.add(tag);
    expectedCertificate.setTags(tags);
    Certificate realCertificate = certificateDao.insert(expectedCertificate);
    assertThat(realCertificate, is(expectedCertificate));
    certificateDao.getById(realCertificate.getId());
  }
}
