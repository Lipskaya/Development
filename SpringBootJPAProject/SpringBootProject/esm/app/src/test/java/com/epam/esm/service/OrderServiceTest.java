package com.epam.esm.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.epam.esm.model.dto.OrderDTO;
import com.epam.esm.model.request.CreateOrderRequest;
import com.epam.esm.model.request.UpdateOrderRequest;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.SearchCriteria;
import com.epam.esm.repository.UserDAORepository;
import com.epam.esm.repository.entity.CertificateEntity;
import com.epam.esm.repository.entity.OrderEntity;
import com.epam.esm.repository.entity.UserEntity;
import com.epam.esm.response.OrderListResponse;
import com.epam.esm.service.mapper.CertificateMapper;
import com.epam.esm.service.mapper.OrderMapper;
import com.epam.esm.service.mapper.TagMapper;
import com.epam.esm.service.mapper.UserMapper;
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
public class OrderServiceTest {

  @Mock
  private OrderRepository orderRepository;
  @Mock
  private CertificateRepository certificateRepository;
  @Mock
  private UserMapper userMapper;
  @Mock
  private CertificateMapper certificateMapper;
  @Mock
  private OrderMapper orderMapper;
  @Mock
  private UserDAORepository userDAORepository;

  @InjectMocks
  private OrderService orderService;

  OrderEntity expectedOrderEntity1;
  OrderEntity expectedOrderEntity2;
  OrderEntity expectedOrderEntity3;
  UserEntity userEntity;
  UserEntity userEntity1;
  UserEntity userEntity2;
  CertificateEntity certificateEntity;
  CertificateEntity certificateEntity1;
  CertificateEntity certificateEntity2;
  List<OrderEntity> expectedOrderEntities;


  @BeforeEach
  void setup() {
    expectedOrderEntities = new ArrayList<>();

    expectedOrderEntity1 = new OrderEntity();
    expectedOrderEntity1.setId(1);
    expectedOrderEntity1.setCost(12.0);
    LocalDateTime localDateTime = LocalDateTime.now();
    expectedOrderEntity1.setDate(localDateTime);
    userEntity = new UserEntity();
    userEntity.setId(1);
    userEntity.setName("userName");
    expectedOrderEntity1.setUserEntity(userEntity);
    certificateEntity = new CertificateEntity();
    certificateEntity.setId(1);
    certificateEntity.setName("certificateName");
    certificateEntity.setDescription("description_1");
    certificateEntity.setPrice(7.5);
    certificateEntity.setCreateDay(localDateTime);
    certificateEntity.setLastUpdateDate(localDateTime);
    certificateEntity.setTagEntities(new HashSet<>());
    expectedOrderEntity1.setCertificateEntity(certificateEntity);

    expectedOrderEntity2 = new OrderEntity();
    expectedOrderEntity2.setId(2);
    expectedOrderEntity2.setCost(14.0);
    expectedOrderEntity2.setDate(localDateTime);
    userEntity1 = new UserEntity();
    userEntity1.setId(2);
    userEntity1.setName("userName_1");
    expectedOrderEntity2.setUserEntity(userEntity1);
    certificateEntity1 = new CertificateEntity();
    certificateEntity1.setId(2);
    certificateEntity1.setName("certificateName_1");
    certificateEntity1.setDescription("description_2");
    certificateEntity1.setPrice(3.5);
    certificateEntity1.setCreateDay(localDateTime);
    certificateEntity1.setLastUpdateDate(localDateTime);
    certificateEntity1.setTagEntities(new HashSet<>());
    expectedOrderEntity2.setCertificateEntity(certificateEntity1);

    expectedOrderEntity3 = new OrderEntity();
    expectedOrderEntity3.setId(3);
    expectedOrderEntity3.setCost(18.0);
    expectedOrderEntity3.setDate(localDateTime);
    userEntity2 = new UserEntity();
    userEntity2.setId(3);
    userEntity2.setName("userName_2");
    expectedOrderEntity3.setUserEntity(userEntity2);
    certificateEntity2 = new CertificateEntity();
    certificateEntity2.setId(5);
    certificateEntity2.setName("certificateName_3");
    certificateEntity2.setDescription("description_3");
    certificateEntity2.setPrice(43.5);
    certificateEntity2.setCreateDay(localDateTime);
    certificateEntity2.setLastUpdateDate(localDateTime);
    certificateEntity2.setTagEntities(new HashSet<>());
    expectedOrderEntity3.setCertificateEntity(certificateEntity2);

    expectedOrderEntities.add(expectedOrderEntity1);
    expectedOrderEntities.add(expectedOrderEntity2);
    expectedOrderEntities.add(expectedOrderEntity3);
  }

  @Test
  void getByIdTest() {
    OrderDTO expectedDTO  = new OrderDTO();
    when(orderRepository.getById(12)).thenReturn(expectedOrderEntity1);
    when(orderMapper.mapToDTO(expectedOrderEntity1)).thenReturn(expectedDTO);

    OrderDTO orderDTO = orderService.getById(12);
    assertThat(orderDTO, is(expectedDTO));

    verify(orderRepository,times(1)).getById(12);
    verify(orderMapper,times(1)).mapToDTO(expectedOrderEntity1);
    verifyNoMoreInteractions(orderMapper,orderRepository);
    verifyNoInteractions(certificateMapper,userMapper);
  }

  @Test
  void getAllTest() {
    Page<OrderEntity> orderPage = new PageImpl<OrderEntity>(
        expectedOrderEntities);

    when(orderRepository.findAll(any(Pageable.class))).thenReturn(orderPage);

    SearchCriteria searchCriteria = new SearchCriteria();
    searchCriteria.setPage(1);
    searchCriteria.setItemsPerPage(5);
    searchCriteria.setSortField("name");
    searchCriteria.setTags(new ArrayList<>());

    OrderListResponse realOrders = orderService.getAll(searchCriteria);
    assertThat(realOrders.getOrders().size(), is(3));
    assertThat(realOrders.getOrders()
        .contains(orderMapper.mapToDTO(expectedOrderEntity1)), is(true));
    assertThat(realOrders.getOrders()
        .contains(orderMapper.mapToDTO(expectedOrderEntity2)), is(true));
    assertThat(realOrders.getOrders()
        .contains(orderMapper.mapToDTO(expectedOrderEntity3)), is(true));

    verify(orderRepository,times(1)).findAll(any(Pageable.class));
    verify(orderMapper,times(2)).mapToDTO(expectedOrderEntity1);
    verifyNoMoreInteractions(orderRepository);
    verifyNoInteractions(certificateMapper,userMapper);
  }

  @Test
  void insertTest() {
    CertificateMapper myCertificateMapper = new CertificateMapper(new TagMapper());
    UserMapper myUserMapper = new UserMapper();

    CreateOrderRequest orderRequest = new CreateOrderRequest();
    orderRequest.setCertificateId(1);
    orderRequest.setUserId(1);

    OrderEntity newOrderEntity = new OrderEntity();
    newOrderEntity.setDate(LocalDateTime.now());
    newOrderEntity.setUserEntity(userEntity);
    newOrderEntity.setCertificateEntity(certificateEntity);
    newOrderEntity.setId(5);
    newOrderEntity.setCost(certificateEntity.getPrice());

    OrderDTO expectedDTO = new OrderDTO();
    expectedDTO.setCost(newOrderEntity.getCost());
    expectedDTO.setId(newOrderEntity.getId());
    expectedDTO.setDate(newOrderEntity.getDate());
    expectedDTO.setCertificate(myCertificateMapper.mapToDTO(newOrderEntity.getCertificateEntity()));
    expectedDTO.setUser(myUserMapper.mapToDTO(newOrderEntity.getUserEntity()));

    when(userDAORepository.getById(1)).thenReturn(userEntity);
    when(certificateRepository.getById(1)).thenReturn(certificateEntity);
    when(orderRepository.saveAndFlush(any())).thenReturn(newOrderEntity);
    when(orderMapper.mapToDTO(any())).thenReturn(expectedDTO);

    OrderDTO orderDTO = orderService.insert(orderRequest);

    assertThat(orderDTO, is(expectedDTO));
    verify(userDAORepository,times(1)).getById(1);
    verify(certificateRepository,times(1)).getById(1);
    verify(orderRepository,times(1)).saveAndFlush(any());
    verifyNoMoreInteractions(orderRepository,userDAORepository,certificateRepository);
    verifyNoInteractions(certificateMapper,userMapper);
  }

  @Test
  void deleteTest() {
    doNothing().when(orderRepository).delete(any());
    orderService.delete(1);
    verify(orderRepository).delete(any());
    verifyNoMoreInteractions(orderRepository);
  }

  @Test
  void updateTest() {
    CertificateMapper myCertificateMapper = new CertificateMapper(new TagMapper());
    UserMapper myUserMapper = new UserMapper();

    UpdateOrderRequest orderRequest = new UpdateOrderRequest();
    orderRequest.setId(1);
    orderRequest.setCertificateId(1);
    orderRequest.setUserId(1);
    orderRequest.setCost(12.0);

    OrderEntity oldEntity = new OrderEntity();
    oldEntity.setId(1);
    oldEntity.setUserEntity(userEntity2);
    oldEntity.setCertificateEntity(certificateEntity2);
    oldEntity.setCost(23.0);
    oldEntity.setDate(LocalDateTime.now());

    OrderEntity newOrderEntity = new OrderEntity();
    newOrderEntity.setId(1);
    newOrderEntity.setUserEntity(userEntity);
    newOrderEntity.setCertificateEntity(certificateEntity);
    newOrderEntity.setCost(12.0);
    newOrderEntity.setDate(oldEntity.getDate());

    OrderDTO oldOrderDTO = new OrderDTO();
    oldOrderDTO.setCost(oldEntity.getCost());
    oldOrderDTO.setId(oldEntity.getId());
    oldOrderDTO.setDate(oldEntity.getDate());
    oldOrderDTO.setCertificate(myCertificateMapper.mapToDTO(oldEntity.getCertificateEntity()));
    oldOrderDTO.setUser(myUserMapper.mapToDTO(oldEntity.getUserEntity()));

    OrderDTO newOrderDTO = new OrderDTO();
    newOrderDTO.setCost(newOrderEntity.getCost());
    newOrderDTO.setId(newOrderEntity.getId());
    newOrderDTO.setDate(newOrderEntity.getDate());
    newOrderDTO.setCertificate(myCertificateMapper.mapToDTO(newOrderEntity.getCertificateEntity()));
    newOrderDTO.setUser(myUserMapper.mapToDTO(newOrderEntity.getUserEntity()));

    when(orderMapper.mapToDTO(any())).thenReturn(oldOrderDTO).thenReturn(newOrderDTO);

    when(orderRepository.getById(anyInt())).thenReturn(oldEntity);
    when(userDAORepository.getById(1)).thenReturn(newOrderEntity.getUserEntity());
    when(certificateRepository.getById(1)).thenReturn(newOrderEntity.getCertificateEntity());
    when(orderRepository.save(any())).thenReturn(newOrderEntity);
    when(orderMapper.mapToEntity(any())).thenReturn(newOrderEntity);

    OrderDTO orderDTO = orderService.update(orderRequest);

    assertThat(orderDTO.getId(), is(newOrderEntity.getId()));
    assertThat(orderDTO.getDate(), is(newOrderEntity.getDate()));
    assertThat(orderDTO.getCost(), is(newOrderEntity.getCost()));
    assertThat(orderDTO.getCertificate(), is(myCertificateMapper.mapToDTO(newOrderEntity.getCertificateEntity())));
    assertThat(orderDTO.getUser(), is(myUserMapper.mapToDTO(newOrderEntity.getUserEntity())));

    verify(userDAORepository,times(1)).getById(1);
    verify(certificateRepository,times(1)).getById(1);
    verify(orderRepository,times(1)).save(any());
    verify(orderMapper,times(1)).mapToEntity(any());
    verify(orderMapper,times(2)).mapToDTO(any());
    verifyNoMoreInteractions(orderRepository,userDAORepository,certificateRepository);
    verifyNoInteractions(certificateMapper,userMapper);
  }
}
