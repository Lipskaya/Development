package com.epam.esm.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.epam.esm.entity.TagEntity;
import com.epam.esm.model.Sort;
import com.epam.esm.model.Tag;
import com.epam.esm.response.TagListResponse;
import com.epam.esm.service.TagService;
import com.epam.esm.service.mapper.TagEntityMapper;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest Controller to process TagEntity - related HTTP requests
 */
@Validated
@RestController
@Slf4j
public class TagController {
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

  @Autowired
  private TagService tagService;
  @Autowired
  private TagEntityMapper tagEntityMapper;

//  @Autowired
//  public TagController(TagService tagService) {
//    this.tagService = tagService;
//  }

  /**
   * Retrieves all tags
   *
   * @return list of tags
   */
  @GetMapping(value = "/v1/tags", produces = {MediaType.APPLICATION_JSON_VALUE})
  public TagListResponse getTags( @RequestParam(required = false, defaultValue = "5") @Min(value = 1, message = "itemsPerPage Min value should be 1") @Max(value = 20, message = "itemsPerPage should Not be more than 20") int itemsPerPage,
                            @RequestParam(required = false, defaultValue = "1") @Min(value = 1, message = "page Min value should be 1") int page) {
    log.debug(LOG_GET_TAGS_START);
    Sort sortRequest = new Sort();
    sortRequest.setPaginationLimit(itemsPerPage);
    sortRequest.setPaginationOffset(page);

    Page<Tag> tagPage = tagService.getAll(sortRequest);

    List<TagEntity> tagEntities = new ArrayList<>();

    for (Tag tag: tagPage ) {
      TagEntity tagEntity = tagEntityMapper.mapToEntity(tag);
      tagEntity.add(linkTo(methodOn(TagController.class).getTag(tagEntity.getId())).withSelfRel());
      tagEntities.add(tagEntity);
    }
    log.debug(LOG_GET_TAGS_END + tagEntities);

    TagListResponse tagListResponse = new TagListResponse();
    tagListResponse.setTagEntities(tagEntities);
    tagListResponse.setPage(page);
    tagListResponse.setItemsPerPage(itemsPerPage);
    tagListResponse.setTotalItems(tagPage.getTotalElements());
    log.debug(LOG_GET_TAGS_END + tagListResponse);
    return tagListResponse;
  }

  /**
   * Retrieves a tag by id
   *
   * @param tagId
   * @return tag
   */
  @GetMapping(value = "/v1/tags/{tagId}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public TagEntity getTag(@PathVariable("tagId") @Min(value = 1, message = "tagId Min value should be 1") int tagId) {
    log.debug(LOG_GET_TAG_START);
    Tag tag = tagService.getById(tagId);
    TagEntity tagEntity = tagEntityMapper.mapToEntity(tag);
    tagEntity.add(linkTo(methodOn(TagController.class).getTag(tagEntity.getId())).withSelfRel());
    log.debug(LOG_GET_TAG_END + tagEntity);
    return tagEntity;
  }

  /**
   * Retrieves a tag by id
   *
   * @return tag
   */
  @GetMapping(value = "/v1/tags/favorite", produces = {MediaType.APPLICATION_JSON_VALUE})
  public TagEntity getFavoriteTag() {
    log.debug("get favorite start");
    TagEntity tag = tagService.getFavorite();
    tag.add(linkTo(methodOn(TagController.class).getTag(tag.getId())).withSelfRel());
    log.debug("get favorite end" + tag);
    return tag;
  }

  /**
   * Saves a new tag
   *
   * @param tag
   * @return tag
   */
  @PostMapping(value = "/v1/tags", produces = {MediaType.APPLICATION_JSON_VALUE})
  public TagEntity addTag(@Valid @RequestBody Tag tag) {
    log.debug(LOG_ADD_TAG_START + tag);
    Tag newTag = tagService.insert(tag);
    TagEntity tagEntity = tagEntityMapper.mapToEntity(newTag);
    tagEntity.add(linkTo(methodOn(TagController.class).getTag(tagEntity.getId())).withSelfRel());
    log.debug(LOG_ADD_TAG_END + tagEntity);
    return tagEntity;
  }

  /**
   * Updates a tag
   *
   * @param tag
   * @return tag
   */
  @PutMapping(value = "/v1/tags/{tagId}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public TagEntity updateTag(
      @PathVariable("tagId") @Min(value = 1, message = "tagId Min value should be 1") int tagId,
      @Valid @RequestBody Tag tag) {
    log.debug(LOG_UPDATE_START + tag);
    tag.setId(tagId);
    Tag updateTag = tagService.update(tag);
    TagEntity tagEntity = tagEntityMapper.mapToEntity(updateTag);
    tagEntity.add(linkTo(methodOn(TagController.class).getTag(tagEntity.getId())).withSelfRel());
    log.debug(LOG_UPDATE_END + tagEntity);
    return tagEntity;
  }

  /**
   * Deletes a tag by id
   *
   * @param tagId
   */
  @DeleteMapping(value = "/v1/tags/{tagId}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public void deleteTag(@PathVariable("tagId") @Min(value = 1, message = "tagId Min value should be 1") int tagId) {
    log.debug(LOG_DELETE_START + tagId);
    tagService.delete(tagId);
    log.debug(LOG_DELETE_END + tagId);
  }
}
