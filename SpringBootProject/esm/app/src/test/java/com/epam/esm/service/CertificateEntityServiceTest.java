package com.epam.esm.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Sort;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.service.mapper.CertificateEntityMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CertificateEntityServiceTest {

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
  @Mock
  private CertificateRepository certificateRepository;
  @Spy
  private CertificateEntityMapper certificateEntityMapper = new CertificateEntityMapper();
  @InjectMocks
  CertificateService certificateService;

  Certificate expectedCertificate1;
  Certificate expectedCertificate2;
  Certificate expectedCertificate3;
  List<Certificate> expectedCertificates;



  @BeforeEach
  void setup() {
    expectedCertificates = new ArrayList<>();
    expectedCertificate1 = new Certificate();
    expectedCertificate1.setId(1);
    expectedCertificate1.setName(CERTIFICATE_ONE_NAME_PARAMETER);
    expectedCertificate1.setDescription(CERTIFICATE_ONE_DESCRIPTION_PARAMETER);
    expectedCertificate1.setPrice(7.5);
    LocalDateTime localDateTime = LocalDateTime.now();
    expectedCertificate1.setCreateDay(localDateTime);
    expectedCertificate1.setLastUpdateDate(localDateTime);
    expectedCertificate1.setTags(new HashSet<>());
    expectedCertificate2 = new Certificate();
    expectedCertificate2.setId(2);
    expectedCertificate2.setName(CERTIFICATE_SECOND_NAME_PARAMETER);
    expectedCertificate2.setDescription(CERTIFICATE_SECOND_DESCRIPTION_PARAMETER);
    expectedCertificate2.setPrice(7.0);
    expectedCertificate2.setCreateDay(localDateTime);
    expectedCertificate2.setLastUpdateDate(localDateTime);
    expectedCertificate2.setTags(new HashSet<>());
    expectedCertificate3 = new Certificate();
    expectedCertificate3.setId(3);
    expectedCertificate3.setName(CERTIFICATE_THIRD_NAME_PARAMETER);
    expectedCertificate3.setDescription(CERTIFICATE_THIRD_DESCRIPTION_PARAMETER);
    expectedCertificate3.setPrice(8.5);
    expectedCertificate3.setCreateDay(localDateTime);
    expectedCertificate3.setLastUpdateDate(localDateTime);
    expectedCertificate3.setTags(new HashSet<>());
    expectedCertificates.add(expectedCertificate1);
    expectedCertificates.add(expectedCertificate2);
    expectedCertificates.add(expectedCertificate3);
  }

  @Test
  void getByIdTest() {
//    when(certificateDao.getById(anyInt())).thenReturn(expectedCertificate1);
//    when(tagDao.getTagsByCertificate(any())).thenReturn(expectedCertificate1.getTags());
    when(certificateRepository.getById(anyInt())).thenReturn(expectedCertificate1);
    Certificate realCertificate = certificateService.getById(1);
    assertThat(realCertificate, is(expectedCertificate1));
  }

  @Test
  void getAllTest() {
    //when(certificateDao.getAll(any())).thenReturn(expectedCertificates);
    Page<Certificate> certificatePage = new PageImpl<Certificate>(expectedCertificates);

    when(certificateRepository.findAll(any(Pageable.class))).thenReturn(certificatePage);
    Sort sort = new Sort();
    sort.setPaginationOffset(1);
    sort.setPaginationLimit(5);
    sort.setSortField("name");
    sort.setTags(new ArrayList<>());
    Page<Certificate> realCertificates = certificateService.getAll(sort);
    assertThat(realCertificates.getSize(), is(3));
    assertThat(realCertificates.getContent().contains(expectedCertificate1), is(true));
    assertThat(realCertificates.getContent().contains(expectedCertificate2), is(true));
    assertThat(realCertificates.getContent().contains(expectedCertificate3), is(true));
  }

  @Test
  void insertTest() {
   //when(certificateDao.insert(any())).thenReturn(expectedCertificate1);
    when(certificateRepository.saveAndFlush(any())).thenReturn(expectedCertificate1);
    when(certificateRepository.getById(anyInt())).thenReturn(expectedCertificate1);
    Certificate realCertificate = certificateService.insert(expectedCertificate1);
    assertThat(realCertificate, is(expectedCertificate1));
  }

  @Test
  void updateTest() {
//    when(certificateDao.update(any())).thenReturn(expectedCertificate1);
//    when(certificateDao.getById(expectedCertificate1.getId())).thenReturn(expectedCertificate1);
    when(certificateRepository.saveAndFlush(any())).thenReturn(expectedCertificate1);
    when(certificateRepository.getById(expectedCertificate1.getId())).thenReturn(expectedCertificate1);

    Certificate realCertificate = certificateService.update(expectedCertificate1, false);
    assertThat(realCertificate, is(expectedCertificate1));
  }

  @Test
  void deleteTest() {
   // doNothing().when(certificateDao).delete(any());
    doNothing().when(certificateRepository).delete(any());
    certificateService.delete(1);
  }
}
