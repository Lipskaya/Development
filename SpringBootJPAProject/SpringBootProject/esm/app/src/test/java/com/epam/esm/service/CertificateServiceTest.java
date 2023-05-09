package com.epam.esm.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.epam.esm.model.dto.CertificateDTO;
import com.epam.esm.repository.SearchCriteria;
import com.epam.esm.repository.entity.CertificateEntity;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.response.CertificateListResponse;
import com.epam.esm.service.mapper.CertificateMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CertificateServiceTest {

  private static final String CERTIFICATE_ONE_NAME_PARAMETER = "FirstTestTag1";
  private static final String CERTIFICATE_ONE_DESCRIPTION_PARAMETER = "description_1";
  private static final String CERTIFICATE_SECOND_NAME_PARAMETER = "FirstTestTag2";
  private static final String CERTIFICATE_SECOND_DESCRIPTION_PARAMETER = "description2";
  private static final String CERTIFICATE_THIRD_NAME_PARAMETER = "FirstTestTag3";
  private static final String CERTIFICATE_THIRD_DESCRIPTION_PARAMETER = "description3";


  @Mock
  private CertificateRepository certificateRepository;
  @Mock
  private CertificateMapper certificateMapper;

  @InjectMocks
  CertificateService certificateService;

  CertificateEntity expectedCertificateEntity1;
  CertificateEntity expectedCertificateEntity2;
  CertificateEntity expectedCertificateEntity3;
  List<CertificateEntity> expectedCertificateEntities;

  @BeforeEach
  void setup() {
    expectedCertificateEntities = new ArrayList<>();
    expectedCertificateEntity1 = new CertificateEntity();
    expectedCertificateEntity1.setId(1);
    expectedCertificateEntity1.setName(CERTIFICATE_ONE_NAME_PARAMETER);
    expectedCertificateEntity1.setDescription(CERTIFICATE_ONE_DESCRIPTION_PARAMETER);
    expectedCertificateEntity1.setPrice(7.5);
    LocalDateTime localDateTime = LocalDateTime.now();
    expectedCertificateEntity1.setCreateDay(localDateTime);
    expectedCertificateEntity1.setLastUpdateDate(localDateTime);
    expectedCertificateEntity1.setTagEntities(new HashSet<>());
    expectedCertificateEntity2 = new CertificateEntity();
    expectedCertificateEntity2.setId(2);
    expectedCertificateEntity2.setName(CERTIFICATE_SECOND_NAME_PARAMETER);
    expectedCertificateEntity2.setDescription(CERTIFICATE_SECOND_DESCRIPTION_PARAMETER);
    expectedCertificateEntity2.setPrice(7.0);
    expectedCertificateEntity2.setCreateDay(localDateTime);
    expectedCertificateEntity2.setLastUpdateDate(localDateTime);
    expectedCertificateEntity2.setTagEntities(new HashSet<>());
    expectedCertificateEntity3 = new CertificateEntity();
    expectedCertificateEntity3.setId(3);
    expectedCertificateEntity3.setName(CERTIFICATE_THIRD_NAME_PARAMETER);
    expectedCertificateEntity3.setDescription(CERTIFICATE_THIRD_DESCRIPTION_PARAMETER);
    expectedCertificateEntity3.setPrice(8.5);
    expectedCertificateEntity3.setCreateDay(localDateTime);
    expectedCertificateEntity3.setLastUpdateDate(localDateTime);
    expectedCertificateEntity3.setTagEntities(new HashSet<>());
    expectedCertificateEntities.add(expectedCertificateEntity1);
    expectedCertificateEntities.add(expectedCertificateEntity2);
    expectedCertificateEntities.add(expectedCertificateEntity3);
  }

  @Test
  void getByIdTest() {
    when(certificateRepository.getById(anyInt())).thenReturn(expectedCertificateEntity1);
    CertificateDTO realCertificateDTO = certificateService.getById(1);

    assertThat(realCertificateDTO,is(certificateMapper.mapToDTO(expectedCertificateEntity1)));
    verify(certificateMapper,times(2)).mapToDTO(expectedCertificateEntity1);
    verify(certificateRepository).getById(1);
    verifyNoMoreInteractions(certificateRepository);
  }

  @Test
  void getAllTest() {
    Page<CertificateEntity> certificatePage = new PageImpl<CertificateEntity>(
        expectedCertificateEntities);
    when(certificateRepository.findAll(any(Pageable.class))).thenReturn(certificatePage);
    SearchCriteria searchCriteria = new SearchCriteria();
    searchCriteria.setPage(1);
    searchCriteria.setItemsPerPage(5);
    searchCriteria.setSortField("name");
    searchCriteria.setTags(new ArrayList<>());
    CertificateListResponse realCertificates = certificateService.getAll(searchCriteria);

    assertThat(realCertificates.getCertificates().size(), is(3));
    assertThat(realCertificates.getCertificates()
        .contains(certificateMapper.mapToDTO(expectedCertificateEntity1)), is(true));
    assertThat(realCertificates.getCertificates()
        .contains(certificateMapper.mapToDTO(expectedCertificateEntity2)), is(true));
    assertThat(realCertificates.getCertificates()
        .contains(certificateMapper.mapToDTO(expectedCertificateEntity3)), is(true));

    verify(certificateMapper,times(2)).mapToDTO(expectedCertificateEntity1);
    verify(certificateRepository,times(1)).findAll(any(Pageable.class));
    verifyNoMoreInteractions(certificateRepository);
  }

  @Test
  void insertTest() {
    CertificateDTO certificateDTO = new CertificateDTO();

    certificateDTO.setId(1);
    certificateDTO.setName(CERTIFICATE_ONE_NAME_PARAMETER);
    certificateDTO.setDescription(CERTIFICATE_ONE_DESCRIPTION_PARAMETER);
    certificateDTO.setPrice(7.5);
    certificateDTO.setCreateDay(expectedCertificateEntity1.getCreateDay());
    certificateDTO.setLastUpdateDate(expectedCertificateEntity1.getLastUpdateDate());
    certificateDTO.setTags(new HashSet<>());

    when(certificateMapper.mapToDTO(expectedCertificateEntity1)).thenReturn(certificateDTO);
    when(certificateMapper.mapToEntity(certificateDTO)).thenReturn(expectedCertificateEntity1);
    when(certificateRepository.saveAndFlush(any())).thenReturn(expectedCertificateEntity1);
    when(certificateRepository.getById(anyInt())).thenReturn(expectedCertificateEntity1);

    CertificateDTO realCertificateDTO = certificateService
        .insert(certificateMapper.mapToDTO(expectedCertificateEntity1));

    verify(certificateMapper).mapToEntity(certificateDTO);
    verify(certificateRepository).saveAndFlush(expectedCertificateEntity1);
    verify(certificateRepository).getById(1);
    verifyNoMoreInteractions(certificateRepository);

    assertThat(realCertificateDTO, is(certificateDTO));
  }

  @Test
  void updateTest() {
    CertificateDTO certificateDTO = new CertificateDTO();

    certificateDTO.setId(1);
    certificateDTO.setName(CERTIFICATE_ONE_NAME_PARAMETER);
    certificateDTO.setDescription(CERTIFICATE_ONE_DESCRIPTION_PARAMETER);
    certificateDTO.setPrice(7.5);
    certificateDTO.setCreateDay(expectedCertificateEntity1.getCreateDay());
    certificateDTO.setLastUpdateDate(expectedCertificateEntity1.getLastUpdateDate());
    certificateDTO.setTags(new HashSet<>());

    when(certificateMapper.mapToDTO(expectedCertificateEntity1)).thenReturn(certificateDTO);
    when(certificateMapper.mapToEntity(certificateDTO)).thenReturn(expectedCertificateEntity1);
    when(certificateRepository.saveAndFlush(any())).thenReturn(expectedCertificateEntity1);
    when(certificateRepository.getById(expectedCertificateEntity1.getId())).thenReturn(
        expectedCertificateEntity1);

    CertificateDTO realCertificateDTO = certificateService.update(certificateDTO, false);


    verify(certificateRepository).saveAndFlush(expectedCertificateEntity1);
    verify(certificateRepository).getById(1);
    verifyNoMoreInteractions(certificateRepository,certificateMapper);

    assertThat(realCertificateDTO, is(certificateDTO));
  }

  @Test
  void deleteTest() {
    doNothing().when(certificateRepository).delete(any());
    certificateService.delete(1);
    verify(certificateRepository).delete(any());
    verifyNoMoreInteractions(certificateRepository);
  }
}
