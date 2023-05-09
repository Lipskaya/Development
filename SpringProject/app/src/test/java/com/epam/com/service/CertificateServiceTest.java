package com.epam.com.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Sort;
import com.epam.esm.service.CertificateService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


class CertificateServiceTest {

  private static final String CERTIFICATE_ONE_NAME_PARAMETER = "FirstTestTag1";
  private static final String CERTIFICATE_ONE_DESCRIPTION_PARAMETER = "description_1";
  private static final String CERTIFICATE_SECOND_NAME_PARAMETER = "FirstTestTag2";
  private static final String CERTIFICATE_SECOND_DESCRIPTION_PARAMETER = "description2";
  private static final String CERTIFICATE_THIRD_NAME_PARAMETER = "FirstTestTag3";
  private static final String CERTIFICATE_THIRD_DESCRIPTION_PARAMETER = "description3";

  @Mock
  private CertificateDao certificateDao;
  @Mock
  private TagDao tagDao;
  @InjectMocks
  CertificateService certificateService;

  Certificate expectedCertificate1;
  Certificate expectedCertificate2;
  Certificate expectedCertificate3;
  List<Certificate> expectedCertificates;

  @BeforeEach
  void setup() {
    MockitoAnnotations.initMocks(this);
    expectedCertificates = new ArrayList<>();
    expectedCertificate1 = new Certificate();
    expectedCertificate1.setId(1);
    expectedCertificate1.setName(CERTIFICATE_ONE_NAME_PARAMETER);
    expectedCertificate1.setDescription(CERTIFICATE_ONE_DESCRIPTION_PARAMETER);
    expectedCertificate1.setPrice(7.5);
    LocalDateTime localDateTime = LocalDateTime.now();
    expectedCertificate1.setCreateDay(localDateTime);
    expectedCertificate1.setLastUpdateDate(localDateTime);
    expectedCertificate1.setTags(new ArrayList<>());
    expectedCertificate2 = new Certificate();
    expectedCertificate2.setId(2);
    expectedCertificate2.setName(CERTIFICATE_SECOND_NAME_PARAMETER);
    expectedCertificate2.setDescription(CERTIFICATE_SECOND_DESCRIPTION_PARAMETER);
    expectedCertificate2.setPrice(7.0);
    expectedCertificate2.setCreateDay(localDateTime);
    expectedCertificate2.setLastUpdateDate(localDateTime);
    expectedCertificate2.setTags(new ArrayList<>());
    expectedCertificate3 = new Certificate();
    expectedCertificate3.setId(3);
    expectedCertificate3.setName(CERTIFICATE_THIRD_NAME_PARAMETER);
    expectedCertificate3.setDescription(CERTIFICATE_THIRD_DESCRIPTION_PARAMETER);
    expectedCertificate3.setPrice(8.5);
    expectedCertificate3.setCreateDay(localDateTime);
    expectedCertificate3.setLastUpdateDate(localDateTime);
    expectedCertificate3.setTags(new ArrayList<>());
    expectedCertificates.add(expectedCertificate1);
    expectedCertificates.add(expectedCertificate2);
    expectedCertificates.add(expectedCertificate3);
  }

  @Test
  void getByIdTest() {
    when(certificateDao.getById(anyInt())).thenReturn(expectedCertificate1);
    when(tagDao.getTagsByCertificate(any())).thenReturn(expectedCertificate1.getTags());
    Certificate realCertificate = certificateService.getById(1);
    assertThat(realCertificate, is(expectedCertificate1));
  }

  @Test
  void getAllTest() {
    when(certificateDao.getAll(any())).thenReturn(expectedCertificates);
    List<Certificate> realCertificates = certificateService.getAll(new Sort());
    assertThat(realCertificates.size(), is(3));
    assertThat(realCertificates.contains(expectedCertificate1), is(true));
    assertThat(realCertificates.contains(expectedCertificate2), is(true));
    assertThat(realCertificates.contains(expectedCertificate3), is(true));
  }

  @Test
  void insertTest() {
    when(certificateDao.insert(any())).thenReturn(expectedCertificate1);
    Certificate realCertificate = certificateService.insert(expectedCertificate1);
    assertThat(realCertificate, is(expectedCertificate1));
  }

  @Test
  void updateTest() {
    when(certificateDao.update(any())).thenReturn(expectedCertificate1);
    when(certificateDao.getById(expectedCertificate1.getId())).thenReturn(expectedCertificate1);
    Certificate realCertificate = certificateService.update(expectedCertificate1);
    assertThat(realCertificate, is(expectedCertificate1));
  }

  @Test
  void deleteTest() {
    doNothing().when(certificateDao).delete(any());
    certificateService.delete(1);
  }
}
