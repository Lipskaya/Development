package com.epam.esm.controller;

import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest Controller to process Tag - related HTTP requests
 */
@Validated
@RestController
public class TagController {
  private static final Logger LOGGER = Logger.getLogger(TagController.class.getCanonicalName());
  private static final String LOG_DELETE_START = "deleteTag() called for id";
  private static final String LOG_DELETE_END = "deleteTag() end for id: ";
  private static final String LOG_UPDATE_START = "update() called";
  private static final String LOG_UPDATE_END = "update() return: ";
  private static final String LOG_ADD_TAG_START = "addTag() called";
  private static final String LOG_ADD_TAG_END = "addTag() return: ";
  private static final String LOG_GET_TAG_START = "getTag() called";
  private static final String LOG_GET_TAG_END = "getTag() return: ";
  private static final String LOG_GET_TAGS_START = "getTags() called";
  private static final String LOG_GET_TAGS_END = "getTags() return: ";

  private TagService tagService;

  @Autowired
  public TagController(TagService tagService) {
    this.tagService = tagService;
  }

  /**
   * Retrieves all tags
   *
   * @return list of tags
   */
  @GetMapping(value = "/tags", produces = {MediaType.APPLICATION_JSON_VALUE})
  public List<Tag> getTags() {
    LOGGER.debug(LOG_GET_TAGS_START);
    List<Tag> tags = tagService.getAll();
    LOGGER.debug(LOG_GET_TAGS_END + tags);
    return tags;
  }

  /**
   * Retrieves a tag by id
   *
   * @param tagId
   * @return tag
   */
  @GetMapping(value = "/tags/{tagId}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public Tag getTag(@PathVariable("tagId") @Min(value = 1, message = "tagId Min value should be 1") int tagId) {
    LOGGER.debug(LOG_GET_TAG_START);
    Tag tag = tagService.getById(tagId);
    LOGGER.debug(LOG_GET_TAG_END + tag);
    return tag;
  }

  /**
   * Saves a new tag
   *
   * @param tag
   * @return tag
   */
  @PostMapping(value = "/tags", produces = {MediaType.APPLICATION_JSON_VALUE})
  public Tag addTag(@Valid @RequestBody Tag tag) {
    LOGGER.debug(LOG_ADD_TAG_START + tag);
    Tag newTag = tagService.insert(tag);
    LOGGER.debug(LOG_ADD_TAG_END + newTag);
    return newTag;
  }

  /**
   * Updates a tag
   *
   * @param tag
   * @return tag
   */
  @PutMapping(value = "/tags", produces = {MediaType.APPLICATION_JSON_VALUE})
  public Tag updateTag(@Valid @RequestBody Tag tag) {
    LOGGER.debug(LOG_UPDATE_START + tag);
    Tag updateTag = tagService.update(tag);
    LOGGER.debug(LOG_UPDATE_END + updateTag);
    return updateTag;
  }

  /**
   * Deletes a tag by id
   *
   * @param tagId
   */
  @DeleteMapping(value = "/tags/{tagId}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public void deleteTag(@PathVariable("tagId") @Min(value = 1, message = "tagId Min value should be 1") int tagId) {
    LOGGER.debug(LOG_DELETE_START + tagId);
    tagService.delete(tagId);
    LOGGER.debug(LOG_DELETE_END + tagId);
  }
}
