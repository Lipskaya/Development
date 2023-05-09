package com.epam.esm.dao;

import com.epam.esm.exception.ErrorCode;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.mapper.TagRowMapper;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Sort;
import com.epam.esm.model.Tag;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 * Database implementation of TagDao interface
 */
@Repository
public class TagDaoImpl implements TagDao {

  private static final Logger LOGGER = Logger.getLogger(TagDaoImpl.class.getCanonicalName());
  private static final String SQL_GET_TAG_BY_ID = "SELECT id,name FROM tag where id = ?";
  private static final String SQL_GET_ALL = "SELECT id,name FROM tag";
  private static final String SQL_UPDATE_TAG = "UPDATE tag SET name = ? WHERE id = ?";
  private static final String SQL_DELETE_TAG = "DELETE FROM tag WHERE id = ?";
  private static final String SQL_DELETE_CERTIFICATE_TAGS = "DELETE FROM gift_tag WHERE gift_certificate_id = ?";
  private static final String SQL_INSERT_CERTIFICATE_TAGS = "INSERT INTO gift_tag (gift_certificate_id, tag_id) VALUES(?, ?)";
  private static final String SQL_GET_TAGS_BY_CERTIFICATE =
      "SELECT gift_tag.tag_id AS id, gift_tag.gift_certificate_id, tag.name \n"
          + "FROM gift_certificate \n"
          + "JOIN gift_tag ON gift_certificate.id=gift_tag.gift_certificate_id \n"
          + "JOIN tag ON tag.id = gift_tag.tag_id \n"
          + "WHERE gift_tag.gift_certificate_id = ?";
  private static final String TAG_NOT_FOUND_START_MESSAGE = "Tag with ID=";
  private static final String TAG_NOT_FOUND_END_MESSAGE = " not found";
  private static final String TAG_NAME_PARAMETER = "name";
  private static final String LOG_DELETE_START = "delete() called:";
  private static final String LOG_DELETE_END = "delete() end tag: ";
  private static final String LOG_UPDATE_START = "update() called";
  private static final String LOG_UPDATE_END = "update() return: ";
  private static final String LOG_INSERT_START = "insert() called";
  private static final String LOG_INSERT_END = "insert() return: ";
  private static final String LOG_GET_BY_ID_START = "getById() called";
  private static final String LOG_GET_BY_ID_END = "getById() return: ";
  private static final String LOG_GET_ALL_START = "getAll() called";
  private static final String LOG_GET_ALL_END = "getAll() return: ";
  private static final String LOG_GET_TAGS_BY_CERTIFICATES_START = "getTagsByCertificate() called";
  private static final String LOG_GET_TAGS_BY_CERTIFICATES_END = "getTagsByCertificate() return: ";
  private static final String LOG_UPDATE_CERTIFICATE_TAGS_START = "updateCertificateTags() called";
  private static final String LOG_UPDATE_CERTIFICATE_TAGS_END = "updateCertificateTags() return: ";

  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert simpleJdbcInsert;
  private final TagRowMapper tagRowMapper;

  @Autowired
  public TagDaoImpl(JdbcTemplate jdbcTemplate,
      @Qualifier("simpleJdbcTagInsert")
          SimpleJdbcInsert simpleJdbcInsert, TagRowMapper tagRowMapper) {
    this.jdbcTemplate = jdbcTemplate;
    this.simpleJdbcInsert = simpleJdbcInsert;
    this.tagRowMapper = tagRowMapper;
  }

  /**
   * Retrieves Tag from database by provided id
   *
   * @param id
   * @return Tag
   */
  @Override
  public Tag getById(Integer id) {
    LOGGER.debug(LOG_GET_BY_ID_START);
    try {
      Tag tag = jdbcTemplate.queryForObject(SQL_GET_TAG_BY_ID, tagRowMapper, id);
      LOGGER.debug(LOG_GET_BY_ID_END + tag);
      return tag;
    } catch (EmptyResultDataAccessException e) {
      LOGGER.error(TAG_NOT_FOUND_START_MESSAGE, e);
      throw new NotFoundException(TAG_NOT_FOUND_START_MESSAGE + id + TAG_NOT_FOUND_END_MESSAGE,
          ErrorCode.ERROR_TAG);
    }
  }

  /**
   * Retrieves list of all Tags from database.
   *
   * @param sortRequest
   * @return list of Tags
   */
  @Override
  public List<Tag> getAll(Sort sortRequest) {
    LOGGER.debug(LOG_GET_ALL_START);
    List<Tag> tags = jdbcTemplate.query(SQL_GET_ALL, tagRowMapper);
    LOGGER.debug(LOG_GET_ALL_END + tags);
    return tags;
  }

  /**
   * Saves a new Tag in database
   *
   * @param tag
   * @return Tag
   */
  @Override
  public Tag insert(Tag tag) {
    LOGGER.debug(LOG_INSERT_START);
    Map<String, Object> parameters = new HashMap<>(1);
    parameters.put(TAG_NAME_PARAMETER, tag.getName());
    Number newId = simpleJdbcInsert.executeAndReturnKey(parameters);
    tag.setId(newId.intValue());
    LOGGER.debug(LOG_INSERT_END + tag);
    return tag;
  }


  /**
   * Clear all id in gift_tags table and save new list of tag in dataBase
   *
   * @param certificate
   */
  @Override
  public void updateCertificateTags(Certificate certificate) {
    LOGGER.debug(LOG_UPDATE_CERTIFICATE_TAGS_START);
    jdbcTemplate.update(SQL_DELETE_CERTIFICATE_TAGS, certificate.getId());
    for (Tag t : certificate.getTags()) {

      if (t.getId() == null) {
        //save new tag to db
        t = insert(t);
      }

      int updatedCertificateTags = jdbcTemplate
          .update(SQL_INSERT_CERTIFICATE_TAGS, certificate.getId(), t.getId());
      if (updatedCertificateTags == 0) {
        LOGGER.error(TAG_NOT_FOUND_START_MESSAGE);
        throw new NotFoundException(TAG_NOT_FOUND_START_MESSAGE + t.getId() + TAG_NOT_FOUND_END_MESSAGE,
            ErrorCode.ERROR_CERTIFICATE);
      }
    }
    LOGGER.debug(LOG_UPDATE_CERTIFICATE_TAGS_END + certificate);
  }

  /**
   * Updates a tag in database
   *
   * @param tag
   * @return
   */
  @Override
  public Tag update(Tag tag) {
    LOGGER.debug(LOG_UPDATE_START);
    int result = jdbcTemplate.update(SQL_UPDATE_TAG, tag.getName(), tag.getId());
    if (result == 1) {
      LOGGER.debug(LOG_UPDATE_END + tag);
      return tag;
    } else {
      LOGGER.error(TAG_NOT_FOUND_START_MESSAGE);
      throw new NotFoundException(
          TAG_NOT_FOUND_START_MESSAGE + tag.getId() + TAG_NOT_FOUND_END_MESSAGE,
          ErrorCode.ERROR_TAG);
    }
  }

  /**
   * Deletes a tag in database
   *
   * @param tag
   */
  @Override
  public void delete(Tag tag) {
    LOGGER.debug(LOG_DELETE_START);
    jdbcTemplate.update(SQL_DELETE_TAG, tag.getId());
    LOGGER.debug(LOG_DELETE_END + tag);
  }

  /**
   * Retrieves all tags by Certificate
   *
   * @param certificate
   * @return list of tags
   */
  @Override
  public List<Tag> getTagsByCertificate(Certificate certificate) {
    LOGGER.debug(LOG_GET_TAGS_BY_CERTIFICATES_START);
    List<Tag> tags = jdbcTemplate
        .query(SQL_GET_TAGS_BY_CERTIFICATE, tagRowMapper, certificate.getId());
    LOGGER.debug(LOG_GET_TAGS_BY_CERTIFICATES_END + tags);
    return tags;
  }
}
