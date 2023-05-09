package com.epam.esm.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.TagEntity;
import com.epam.esm.model.Sort;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.mapper.TagEntityMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class TagEntityServiceTest {

  private static final String TAG_ONE_NAME_PARAMETER = "FirstTestTag1";
  private static final String TAG_SECOND_NAME_PARAMETER = "FirstTestTag2";
  private static final String TAG_THIRD_NAME_PARAMETER = "FirstTestTag3";

  @Mock
  private TagDao tagDao;
  @Mock
  private TagRepository tagRepository;
  @Spy
  TagEntityMapper tagEntityMapper;
  @InjectMocks
  TagService tagService;

  Tag expectedTag1;
  Tag expectedTag2;
  Tag expectedTag3;
  List<Tag> expectedTags;

  @BeforeEach
  void setup() {
    expectedTags = new ArrayList<>();
    expectedTag1 = new Tag();
    expectedTag1.setId(1);
    expectedTag1.setName(TAG_ONE_NAME_PARAMETER);
    expectedTag2 = new Tag();
    expectedTag2.setId(2);
    expectedTag2.setName(TAG_SECOND_NAME_PARAMETER);
    expectedTag3 = new Tag();
    expectedTag3.setId(3);
    expectedTag3.setName(TAG_THIRD_NAME_PARAMETER);
    expectedTags.add(expectedTag1);
    expectedTags.add(expectedTag2);
    expectedTags.add(expectedTag3);
  }

  @Test
  void getByIdTest() {
//    when(tagDao.getById(anyInt())).thenReturn(expectedTag1);
    when(tagRepository.getById(anyInt())).thenReturn(expectedTag1);

    Tag realTag = tagService.getById(1);
    assertThat(realTag, is(expectedTag1));
  }

  @Test
  void getAllTest() {
 //   when(tagDao.getAll(any())).thenReturn(expectedTags);

    Page<Tag> tagsPage = new PageImpl<Tag>(expectedTags);


    when(tagRepository.findAll(any(Pageable.class))).thenReturn(tagsPage);

    Sort sort = new Sort();
    sort.setPaginationOffset(1);
    sort.setPaginationLimit(5);

    Page<Tag> realTags = tagService.getAll(sort);
    assertThat(realTags.getSize(), is(3));
    assertThat(realTags.getContent().contains(expectedTag1), is(true));
    assertThat(realTags.getContent().contains(expectedTag2), is(true));
    assertThat(realTags.getContent().contains(expectedTag3), is(true));
  } 

  @Test
  void insertTest() {
 //   when(tagDao.insert(any())).thenReturn(expectedTag1);
    when(tagRepository.save(any())).thenReturn(expectedTag1);
    Tag realTag = tagService.insert(expectedTag1);
    assertThat(realTag, is(expectedTag1));
  }

  @Test
  void updateTest() {
 //   when(tagDao.update(any())).thenReturn(expectedTag1);
    when(tagRepository.save(any())).thenReturn(expectedTag1);
    Tag realTag = tagService.update(expectedTag1);
    assertThat(realTag, is(expectedTag1));
  }

  @Test
  void deleteTest() {
//    doNothing().when(tagDao).delete(any());
    doNothing().when(tagRepository).delete(any());
    tagService.delete(1);
  }
}
