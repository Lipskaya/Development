package com.epam.com.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.epam.esm.dao.TagDao;
import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class TagServiceTest {

  private static final String TAG_ONE_NAME_PARAMETER = "FirstTestTag1";
  private static final String TAG_SECOND_NAME_PARAMETER = "FirstTestTag2";
  private static final String TAG_THIRD_NAME_PARAMETER = "FirstTestTag3";

  @Mock
  private TagDao tagDao;
  @InjectMocks
  TagService tagService;

  Tag expectedTag1;
  Tag expectedTag2;
  Tag expectedTag3;
  List<Tag> expectedTags;

  @BeforeEach
  void setup() {
    MockitoAnnotations.initMocks(this);
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
    when(tagDao.getById(anyInt())).thenReturn(expectedTag1);
    Tag realTag = tagService.getById(1);
    assertThat(realTag, is(expectedTag1));
  }

  @Test
  void getAllTest() {
    when(tagDao.getAll(any())).thenReturn(expectedTags);
    List<Tag> realTags = tagService.getAll();
    assertThat(realTags.size(), is(3));
    assertThat(realTags.contains(expectedTag1), is(true));
    assertThat(realTags.contains(expectedTag2), is(true));
    assertThat(realTags.contains(expectedTag3), is(true));
  }

  @Test
  void insertTest() {
    when(tagDao.insert(any())).thenReturn(expectedTag1);
    Tag realTag = tagService.insert(expectedTag1);
    assertThat(realTag, is(expectedTag1));
  }

  @Test
  void updateTest() {
    when(tagDao.update(any())).thenReturn(expectedTag1);
    Tag realTag = tagService.update(expectedTag1);
    assertThat(realTag, is(expectedTag1));
  }

  @Test
  void deleteTest() {
    doNothing().when(tagDao).delete(any());
    tagService.delete(1);
  }
}
