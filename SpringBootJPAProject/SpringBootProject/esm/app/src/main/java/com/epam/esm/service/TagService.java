package com.epam.esm.service;

import com.epam.esm.model.dto.TagDTO;
import com.epam.esm.exception.ErrorCode;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.repository.SearchCriteria;
import com.epam.esm.repository.entity.TagEntity;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.response.TagListResponse;
import com.epam.esm.service.mapper.TagMapper;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TagDTO Service
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
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
  private static final String TAG_NOT_FOUND_START = "Requested TagEntity not found (id = ";
  private static final String END = ")";

  private final TagRepository tagRepository;
  private final TagMapper tagMapper;

  /**
   * Finds all Tags
   *
   * @param searchCriteriaRequest
   * @return list of tags
   */
  public TagListResponse getAll(SearchCriteria searchCriteriaRequest) {
    log.debug(LOG_GET_ALL_START);
    Pageable pageable = PageRequest.of(searchCriteriaRequest.getPage() - 1, searchCriteriaRequest.getItemsPerPage());
    Page<TagEntity> tagPage = tagRepository.findAll(pageable);
    List<TagDTO> tagDTOS = tagPage.stream()
        .map(tag -> tagMapper.mapToDTO(tag))
        .collect(Collectors.toList());
    TagListResponse tagListResponse = new TagListResponse();
    tagListResponse.setTags(tagDTOS);
    tagListResponse.setPage(searchCriteriaRequest.getPage());
    tagListResponse.setItemsPerPage(searchCriteriaRequest.getItemsPerPage());
    tagListResponse.setTotalItems(tagPage.getTotalElements());
    log.debug(LOG_GET_ALL_END + tagListResponse);
    return tagListResponse;
  }

  /**
   * Retrieves the TagDTO by id
   *
   * @param tagId
   * @return TagDTO
   */
  public TagDTO getById(int tagId) {
    log.debug(LOG_GET_BY_ID_START + tagId);
    try {
      TagEntity tagEntity = tagRepository.getById(tagId);
      TagDTO tagDTO = tagMapper.mapToDTO(tagEntity);
      log.debug(LOG_GET_BY_ID_END + tagDTO);
      return tagDTO;
    } catch (EntityNotFoundException exception) {
      throw new NotFoundException(TAG_NOT_FOUND_START + tagId + END, ErrorCode.ERROR_TAG);
    }
  }

  /**
   * Saves a new tag
   *
   * @param tag
   * @return TagDTO
   */
  @Transactional
  public TagDTO insert(TagDTO tag) {
    log.debug(LOG_INSERT_START + tag);
    TagEntity newTagEntity = tagRepository.save(tagMapper.mapToEntity(tag));
    TagDTO tagDTO = tagMapper.mapToDTO(newTagEntity);
    log.debug(LOG_INSERT_END + tagDTO);
    return tagDTO;
  }

  /**
   * Updates the tag
   *
   * @param tag
   * @return TagDTO
   */
  @Transactional
  public TagDTO update(TagDTO tag) {
    log.debug(LOG_UPDATE_START + tag);
    getById(tag.getId());
    TagEntity updateTagEntity = tagRepository.save(tagMapper.mapToEntity(tag));
    TagDTO tagDTO = tagMapper.mapToDTO(updateTagEntity);
    log.debug(LOG_UPDATE_END + tagDTO);
    return tagDTO;
  }

  /**
   * Deletes a tag
   *
   * @param tagId
   */
  public void delete(int tagId) {
    log.debug(LOG_DELETE_START + tagId);
    TagEntity t = new TagEntity();
    t.setId(tagId);
    tagRepository.delete(t);
    log.debug(LOG_DELETE_END + t);
  }

  /**
   * Get favorite tag
   * @return
   */
  public TagDTO getFavorite() {
    log.debug("get favorite service start");
    return tagMapper.mapToDTO(tagRepository.getFavoriteTag());
  }
}
