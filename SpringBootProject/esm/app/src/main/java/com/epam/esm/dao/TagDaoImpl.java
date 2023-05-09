package com.epam.esm.dao;

import com.epam.esm.exception.ErrorCode;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.dao.mapper.TagRowMapper;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Sort;
import com.epam.esm.model.Tag;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class TagDaoImpl implements TagDao {

  private static final String SQL_GET_TAG_BY_ID = "SELECT id,name FROM tag where id = ?";
  private static final String SQL_GET_ALL = "SELECT id,name FROM tag LIMIT ? OFFSET ?";
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
  private static final String SQL_TOTAL_TAGS = "SELECT count(*) FROM tag";
  private static final String TAG_NOT_FOUND_START_MESSAGE = "TagEntity with ID=";
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
  private static final String LOG_GET_TOTAL_ITEMS_START = "getTotalItems() called";
  private static final String LOG_GET_TOTAL_ITEMS_END = "getTotalItems() return: ";
  private static final String SQL_GET_FAVORITE_TAG = "select * from tag\n"
      + "where tag.id = \n"
      + "(select tag_id \n"
      + "from\n"
      + "(select TT.tag_id, count(TT.tag_id) as occurance from\n"
      + "(select tag_id from \n"
      + "(SELECT * FROM spring_boot_certificates.gift_certificate\n"
      + "WHERE \n"
      + "spring_boot_certificates.gift_certificate.id in\n"
      + "(select KK.gift_certificate_id \n"
      + "from\n"
      + "(select * from spring_boot_certificates.`order`\n"
      + "having \n"
      + "spring_boot_certificates.`order`.user_id = \n"
      + "(select K.max_user_id from\n"
      + "(select user_id as max_user_id, sum(cost) as total_orders_cost \n"
      + "FROM \n"
      + "spring_boot_certificates.`order`\n"
      + "group by user_id\n"
      + "order by total_orders_cost desc\n"
      + "limit 1) as K)) as KK)) as S\n"
      + "join gift_tag on gift_tag.gift_certificate_id = S.id) as TT\n"
      + "group by TT.tag_id\n"
      + "order by occurance desc\n"
      + "limit 1) as MT)";

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
   * Retrieves TagEntity from database by provided id
   *
   * @param id
   * @return TagEntity
   */
  @Override
  public Tag getById(Integer id) {
    log.debug(LOG_GET_BY_ID_START);
    try {
      Tag tag = jdbcTemplate.queryForObject(SQL_GET_TAG_BY_ID, tagRowMapper, id);
      log.debug(LOG_GET_BY_ID_END + tag);
      return tag;
    } catch (EmptyResultDataAccessException e) {
      log.error(TAG_NOT_FOUND_START_MESSAGE, e);
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
    log.debug(LOG_GET_ALL_START);
    List<Tag> tags = jdbcTemplate.query(SQL_GET_ALL, tagRowMapper,
        sortRequest.getPaginationLimit(),
        (sortRequest.getPaginationOffset() - 1) * sortRequest.getPaginationLimit());
    log.debug(LOG_GET_ALL_END + tags);
    return tags;
  }

  /**
   * Saves a new TagEntity in database
   *
   * @param tag
   * @return TagEntity
   */
  @Override
  public Tag insert(Tag tag) {
    log.debug(LOG_INSERT_START);
    Map<String, Object> parameters = new HashMap<>(1);
    parameters.put(TAG_NAME_PARAMETER, tag.getName());
    Number newId = simpleJdbcInsert.executeAndReturnKey(parameters);
    tag.setId(newId.intValue());
    log.debug(LOG_INSERT_END + tag);
    return tag;
  }


  /**
   * Clear all id in gift_tags table and save new list of tag in dataBase
   *
   * @param certificate
   */
  @Override
  public void updateCertificateTags(Certificate certificate) {
    log.debug(LOG_UPDATE_CERTIFICATE_TAGS_START);
    jdbcTemplate.update(SQL_DELETE_CERTIFICATE_TAGS, certificate.getId());
    for (Tag t : certificate.getTags()) {

      if (t.getId() == null) {
        //save new tag to db
        t = insert(t);
      }

      int updatedCertificateTags = jdbcTemplate
          .update(SQL_INSERT_CERTIFICATE_TAGS, certificate.getId(), t.getId());
      if (updatedCertificateTags == 0) {
        log.error(TAG_NOT_FOUND_START_MESSAGE);
        throw new NotFoundException(TAG_NOT_FOUND_START_MESSAGE + t.getId() + TAG_NOT_FOUND_END_MESSAGE,
            ErrorCode.ERROR_CERTIFICATE);
      }
    }
    log.debug(LOG_UPDATE_CERTIFICATE_TAGS_END + certificate);
  }

  /**
   * Updates a tag in database
   *
   * @param tag
   * @return
   */
  @Override
  public Tag update(Tag tag) {
    log.debug(LOG_UPDATE_START);
    int result = jdbcTemplate.update(SQL_UPDATE_TAG, tag.getName(), tag.getId());
    if (result == 1) {
      log.debug(LOG_UPDATE_END + tag);
      return tag;
    } else {
      log.error(TAG_NOT_FOUND_START_MESSAGE);
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
    log.debug(LOG_DELETE_START);
    jdbcTemplate.update(SQL_DELETE_TAG, tag.getId());
    log.debug(LOG_DELETE_END + tag);
  }

  /**
   * Retrieves all tags by CertificateEntity
   *
   * @param certificate
   * @return list of tags
   */
  @Override
  public List<Tag> getTagsByCertificate(Certificate certificate) {
    log.debug(LOG_GET_TAGS_BY_CERTIFICATES_START);
    List<Tag> tags = jdbcTemplate
        .query(SQL_GET_TAGS_BY_CERTIFICATE, tagRowMapper, certificate.getId());
    log.debug(LOG_GET_TAGS_BY_CERTIFICATES_END + tags);
    return tags;
  }

  /**
   * Returns total items from data base
   * @return
   */
  @Override
  public int getTotalItems() {
    log.debug(LOG_GET_TOTAL_ITEMS_START);
    int total = jdbcTemplate.queryForObject(SQL_TOTAL_TAGS, Integer.class);
    log.debug(LOG_GET_TOTAL_ITEMS_END + total);
    return total;
  }

  @Override
  public Tag getFavorite() {
    log.debug("get favorite DAO start");
    try {
      Tag tag = jdbcTemplate.queryForObject(SQL_GET_FAVORITE_TAG, tagRowMapper);
      log.debug("get favorite DAO end" + tag);
      return tag;
    } catch (EmptyResultDataAccessException e) {
      log.error(TAG_NOT_FOUND_START_MESSAGE, e);
      throw new NotFoundException(TAG_NOT_FOUND_START_MESSAGE + TAG_NOT_FOUND_END_MESSAGE,
          ErrorCode.ERROR_TAG);
    }
  }
}
