package com.epam.esm.service;

import com.epam.esm.dao.TagDao;
import com.epam.esm.model.Tag;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Tag Service
 */
@Service
@Transactional
public class TagService {

  private static final Logger LOGGER = Logger.getLogger(TagService.class.getCanonicalName());
  private static final String LOG_DELETE_START = "delete() called id:";
  private static final String LOG_DELETE_END = "delete() end id: ";
  private static final String LOG_UPDATE_START = "update() called";
  private static final String LOG_UPDATE_END = "update() return: ";
  private static final String LOG_INSERT_START = "insert() called";
  private static final String LOG_INSERT_END = "insert() return: ";
  private static final String LOG_GET_BY_ID_START = "getById() called";
  private static final String LOG_GET_BY_ID_END = "getById() return: ";
  private static final String LOG_GET_ALL_START = "getAll() called";
  private static final String LOG_GET_ALL_END = "getAll() return: ";

  private final TagDao tagDao;

  @Autowired
  public TagService(TagDao tagDao) {
    this.tagDao = tagDao;
  }

  /**
   * Finds all Tags
   *
   * @return list of tags
   */
  public List<Tag> getAll() {
    LOGGER.debug(LOG_GET_ALL_START);
    List<Tag> tags = tagDao.getAll(null);
    LOGGER.debug(LOG_GET_ALL_END + tags);
    return tags;
  }

  /**
   * Retrieves the Tag by id
   *
   * @param tagId
   * @return tag
   */
  public Tag getById(int tagId) {
    LOGGER.debug(LOG_GET_BY_ID_START + tagId);
    Tag tag = tagDao.getById(tagId);
    LOGGER.debug(LOG_GET_BY_ID_END + tag);
    return tag;
  }

  /**
   * Saves a new tag
   *
   * @param tag
   * @return tag
   */
  public Tag insert(Tag tag) {
    LOGGER.debug(LOG_INSERT_START + tag);
    Tag newTag = tagDao.insert(tag);
    LOGGER.debug(LOG_INSERT_END + newTag);
    return newTag;
  }

  /**
   * Updates the tag
   *
   * @param tag
   * @return tag
   */
  public Tag update(Tag tag) {
    LOGGER.debug(LOG_UPDATE_START + tag);
    Tag updateTag = tagDao.update(tag);
    LOGGER.debug(LOG_UPDATE_END + updateTag);
    return updateTag;
  }

  /**
   * Deletes a tag
   *
   * @param tagId
   */
  public void delete(int tagId) {
    LOGGER.debug(LOG_DELETE_START + tagId);
    Tag t = new Tag();
    t.setId(tagId);
    tagDao.delete(t);
    LOGGER.debug(LOG_DELETE_END + t);
  }
}
