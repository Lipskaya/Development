package com.epam.esm.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.epam.esm.exception.NotFoundException;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Sort;
import com.epam.esm.model.Tag;
import java.util.List;
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
class TagEntityDaoIntegrationTest {

  private static final String TAG_NAME_PARAMETER = "nameTag_1";
  private static final String TAG_NAME_UPDATE_PARAMETER = "TestTag_two_Updated";
  private static final String TAG_NAME_INSERTED_PARAMETER = "TestTag_inserted";

  @Autowired
  public TagDao tagDao;

  @Test
  void tagDaoGetByIdTest() {
    Tag expectedTag = new Tag();
    expectedTag.setId(1);
    expectedTag.setName(TAG_NAME_PARAMETER);
    Tag realTag = tagDao.getById(1);
    assertThat(realTag, is(expectedTag));
  }

  @Test
  void tagDaoGetAllTest() {
    Sort sort = new Sort();
    sort.setPaginationOffset(1);
    sort.setPaginationLimit(10);
    List<Tag> realTagList = tagDao.getAll(sort);
    assertThat(realTagList.size(), is(5));
  }

  @Test
  void tagDaoDeleteTest() {
    Tag expectedTag = tagDao.getById(1);
    assertNotNull(expectedTag);
    tagDao.delete(expectedTag);
    Sort sort = new Sort();
    sort.setPaginationOffset(1);
    sort.setPaginationLimit(10);
    List<Tag> realTagList = tagDao.getAll(sort);
    assertThat(realTagList.size(), is(4));
    assertThat(realTagList.contains(expectedTag), is(false));
  }

  @Test
  void tagDaoUpdateTest() {
    Tag expectedTag = tagDao.getById(2);
    expectedTag.setName(TAG_NAME_UPDATE_PARAMETER);
    Tag realTag = tagDao.update(expectedTag);
    assertEquals(realTag, expectedTag);
    realTag = tagDao.getById(2);
    assertThat(realTag, is(expectedTag));
  }

  @Test
  void tagDaoInsertTest() {
    Tag expectedTag = new Tag();
    expectedTag.setName(TAG_NAME_INSERTED_PARAMETER);
    Tag realTag = tagDao.insert(expectedTag);
    assertThat(realTag, is(expectedTag));

  }

  @Test
  void getTagsByCertificateTest() {
    Certificate certificate = new Certificate();
    certificate.setId(2);
    List<Tag> certificateTags = tagDao.getTagsByCertificate(certificate);
    assertThat(certificateTags.size(), is(1));
  }

  @Test
  void tagDaoGetByIdExceptionTest() {
    NotFoundException thrown = assertThrows(
        NotFoundException.class,
        () -> tagDao.getById(111),
        "Expected tagDao.getById(1) to throw exception, but it didn't"
    );
    assertThat(thrown.getMessage(), is("TagEntity with ID=111 not found"));
  }
}
