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

import com.epam.esm.exception.NotFoundException;
import com.epam.esm.model.dto.TagDTO;
import com.epam.esm.repository.SearchCriteria;
import com.epam.esm.repository.entity.TagEntity;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.response.TagListResponse;
import com.epam.esm.service.mapper.TagMapper;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {
  private static final String TAG_ONE_NAME_PARAMETER = "FirstTestTag1";
  private static final String TAG_SECOND_NAME_PARAMETER = "FirstTestTag2";
  private static final String TAG_THIRD_NAME_PARAMETER = "FirstTestTag3";

  @Mock
  private TagRepository tagRepository;
  @Mock
  private TagMapper tagMapper;
  @InjectMocks
  private TagService tagService;

  TagEntity expectedTagEntity1;
  TagEntity expectedTagEntity2;
  TagEntity expectedTagEntity3;
  List<TagEntity> expectedTagEntities;

  @BeforeEach
  void setup() {
    expectedTagEntities = new ArrayList<>();
    expectedTagEntity1 = new TagEntity();
    expectedTagEntity1.setId(1);
    expectedTagEntity1.setName(TAG_ONE_NAME_PARAMETER);
    expectedTagEntity2 = new TagEntity();
    expectedTagEntity2.setId(2);
    expectedTagEntity2.setName(TAG_SECOND_NAME_PARAMETER);
    expectedTagEntity3 = new TagEntity();
    expectedTagEntity3.setId(3);
    expectedTagEntity3.setName(TAG_THIRD_NAME_PARAMETER);
    expectedTagEntities.add(expectedTagEntity1);
    expectedTagEntities.add(expectedTagEntity2);
    expectedTagEntities.add(expectedTagEntity3);
  }

  @Test
  void getByIdTest() {
    TagDTO expectedDTO  = new TagDTO();
    when(tagRepository.getById(anyInt())).thenReturn(expectedTagEntity1);
    when(tagMapper.mapToDTO(expectedTagEntity1)).thenReturn(expectedDTO);
    TagDTO tagDTO = tagService.getById(1);
    assertThat(tagDTO, is(expectedDTO));

    verify(tagRepository,times(1)).getById(1);
    verify(tagMapper,times(1)).mapToDTO(expectedTagEntity1);
    verifyNoMoreInteractions(tagMapper,tagRepository);
  }

  @Test
  void getByIdTestNotFound() {
    when(tagRepository.getById(anyInt())).thenThrow(new EntityNotFoundException());

    NotFoundException thrown = assertThrows(
        NotFoundException.class,
        () -> tagService.getById(1),
        "Expected getById() to throw, but it didn't"
    );

    assertThat(thrown.getMessage(), is("Requested TagEntity not found (id = 1)"));
  }

  @Test
  void updateTestNotFound() {
    when(tagRepository.getById(anyInt())).thenThrow(new EntityNotFoundException());
    TagDTO tagDTO = new TagDTO();
    tagDTO.setId(1);
    tagDTO.setName("name");
    NotFoundException thrown = assertThrows(
        NotFoundException.class,
        () -> tagService.update(tagDTO),
        "Expected getById() to throw, but it didn't"
    );
    assertThat(thrown.getMessage(), is("Requested TagEntity not found (id = 1)"));
  }

  @Test
  void getAllTest() {
    Page<TagEntity> tagsPage = new PageImpl<TagEntity>(expectedTagEntities);
    when(tagRepository.findAll(any(Pageable.class))).thenReturn(tagsPage);
    SearchCriteria searchCriteria = new SearchCriteria();
    searchCriteria.setPage(1);
    searchCriteria.setItemsPerPage(5);
    TagListResponse realTags = tagService.getAll(searchCriteria);
    assertThat(realTags.getTags().size(), is(3));
    assertThat(realTags.getTags().contains(tagMapper.mapToDTO(expectedTagEntity1)), is(true));
    assertThat(realTags.getTags().contains(tagMapper.mapToDTO(expectedTagEntity2)), is(true));
    assertThat(realTags.getTags().contains(tagMapper.mapToDTO(expectedTagEntity3)), is(true));

    verify(tagRepository,times(1)).findAll(any(Pageable.class));
    verify(tagMapper,times(2)).mapToDTO(expectedTagEntity1);
    verifyNoMoreInteractions(tagRepository);
  }

  @Test
  void insertTest() {

    TagDTO tagDTO = new TagDTO();
    tagDTO.setId(expectedTagEntity1.getId());
    tagDTO.setName(expectedTagEntity1.getName());


    when(tagMapper.mapToDTO(expectedTagEntity1)).thenReturn(tagDTO);
    when(tagMapper.mapToEntity(tagDTO)).thenReturn(expectedTagEntity1);

    when(tagRepository.save(any())).thenReturn(expectedTagEntity1);

    TagDTO realDTO = tagService.insert(tagDTO);
    assertThat(realDTO, is(tagDTO));

    verify(tagRepository,times(1)).save(any());
    verify(tagMapper,times(1)).mapToDTO(expectedTagEntity1);
    verify(tagMapper,times(1)).mapToEntity(tagDTO);
    verifyNoMoreInteractions(tagMapper,tagRepository);
  }

  @Test
  void updateTest() {
    TagDTO tagDTO = new TagDTO();
    tagDTO.setId(expectedTagEntity1.getId());
    tagDTO.setName(expectedTagEntity1.getName());

    when(tagMapper.mapToDTO(expectedTagEntity1)).thenReturn(tagDTO);
    when(tagMapper.mapToEntity(tagDTO)).thenReturn(expectedTagEntity1);

    when(tagRepository.save(any())).thenReturn(expectedTagEntity1);
    when(tagRepository.getById(anyInt())).thenReturn(expectedTagEntity1);

    TagDTO realTagDTO = tagService.update(tagDTO);
    assertThat(realTagDTO, is(tagDTO));

    verify(tagRepository,times(1)).save(any());
    verify(tagRepository,times(1)).getById(anyInt());
    verify(tagMapper,times(2)).mapToDTO(expectedTagEntity1);
    verify(tagMapper,times(1)).mapToEntity(tagDTO);
    verifyNoMoreInteractions(tagMapper,tagRepository);
  }

  @Test
  void deleteTest() {
    doNothing().when(tagRepository).delete(any());
    tagService.delete(1);
    verify(tagRepository).delete(any());
    verifyNoMoreInteractions(tagRepository);
  }
}
