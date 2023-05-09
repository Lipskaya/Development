package com.epam.esm.service;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.TagEntity;
import com.epam.esm.exception.ErrorCode;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.model.Sort;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.mapper.TagEntityMapper;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TagEntity Service
 */
@Service
@Transactional
@Slf4j

public class TagService {

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
  private static final String LOG_GET_TOTAL_ITEMS_START = "getTotalItems() called";
  private static final String LOG_GET_TOTAL_ITEMS_END = "getTotalItems() return: ";

  private static final String TAG_NOT_FOUND_START_MESSAGE = "TagEntity with ID=";
  private static final String TAG_NOT_FOUND_END_MESSAGE = " not found";


  //  private final TagDao tagDao;
//  private final TagEntityMapper tagEntityMapper;
  @Autowired
  private TagRepository tagRepository;

//  @Autowired
//  public TagService(TagDao tagDao, TagEntityMapper tagEntityMapper) {
//    this.tagDao = tagDao;
//    this.tagEntityMapper = tagEntityMapper;
//  }

  /**
   * Finds all Tags
   *
   * @return list of tags
   * @param sortRequest
   */
  public Page<Tag> getAll(Sort sortRequest) {
    log.debug(LOG_GET_ALL_START);
 //   List<Tag> tags = tagDao.getAll(sortRequest);
    Pageable pageable = PageRequest.of(sortRequest.getPaginationOffset() - 1, sortRequest.getPaginationLimit());
    Page<Tag> tags = tagRepository.findAll(pageable);
//    List<TagEntity> tagEntities = new ArrayList<>();
//    for (Tag t:tags) {
//      tagEntities.add(tagEntityMapper.mapToEntity(t));
//    }
    log.debug(LOG_GET_ALL_END + tags);
    return tags;
  }

  /**
   * Retrieves the TagEntity by id
   *
   * @param tagId
   * @return TagEntity
   */
  public Tag getById(int tagId) {
    log.debug(LOG_GET_BY_ID_START + tagId);
//    Tag tag = tagDao.getById(tagId);
//    Tag tag = null;
//    try{
    Tag tag = tagRepository.getById(tagId);
//    }catch (EntityNotFoundException e){
//      throw new NotFoundException(TAG_NOT_FOUND_START_MESSAGE + tagId + TAG_NOT_FOUND_END_MESSAGE,
//          ErrorCode.ERROR_TAG);
//    }

//    TagEntity tagEntity = tagEntityMapper.mapToEntity(tag);
    log.debug(LOG_GET_BY_ID_END + tag);
    return tag;
  }

  /**
   * Saves a new tag
   *
   * @param tag
   * @return TagEntity
   */
  public Tag insert(Tag tag) {
    log.debug(LOG_INSERT_START + tag);
 //   Tag newTag = tagDao.insert(tag);
    Tag newTag = tagRepository.save(tag);
//    TagEntity tagEntity = tagEntityMapper.mapToEntity(newTag);
    log.debug(LOG_INSERT_END + newTag);
    return newTag;
  }

  /**
   * Updates the tag
   *
   * @param tag
   * @return TagEntity
   */
  public Tag update(Tag tag) {
    log.debug(LOG_UPDATE_START + tag);
    //Tag updateTag = tagDao.update(tag);
    Tag updateTag = tagRepository.save(tag);
        //TagEntity tagEntity = tagEntityMapper.mapToEntity(updateTag);
    log.debug(LOG_UPDATE_END + updateTag);
    return updateTag;
  }


  /**
   * Deletes a tag
   *
   * @param tagId
   */
  public void delete(int tagId) {
    log.debug(LOG_DELETE_START + tagId);
    Tag t = new Tag();
    t.setId(tagId);
    //tagDao.delete(t);
    tagRepository.delete(t);
    log.debug(LOG_DELETE_END + t);
  }

//  /**
//   * Returns total items of tags in data base
//   * @return
//   */
//  public int getTotalItems() {
//    log.debug(LOG_GET_TOTAL_ITEMS_START);
//    int total = tagDao.getTotalItems();
//    log.debug(LOG_GET_TOTAL_ITEMS_END + total);
//    return total;
//  }

  public TagEntity getFavorite() {
    log.debug("get favorite service start");
    //TODO: need separate implementation in repository
//    Tag tag = tagDao.getFavorite();
//    TagEntity tagEntity = tagEntityMapper.mapToEntity(tag);
    //log.debug("get favorite service end " + tag);
    //return tagEntity;
    throw new UnsupportedOperationException("not implemented");
  }
}
