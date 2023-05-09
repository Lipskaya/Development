package com.epam.esm.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.epam.esm.model.dto.TagDTO;
import com.epam.esm.repository.SearchCriteria;
import com.epam.esm.response.TagListResponse;
import com.epam.esm.service.TagService;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
 * Rest Controller to process TagDTO - related HTTP requests
 */
@Validated
@RestController
@Slf4j
@RequiredArgsConstructor
public class TagController {

  private static final String LOG_DELETE_START = "deleteTag() called for id";
  private static final String LOG_DELETE_END = "deleteTag() end for id: ";
  private static final String LOG_UPDATE_START = "update() called";
  private static final String LOG_UPDATE_END = "update() return: ";
  private static final String LOG_ADD_TAG_START = "addTag() called";
  private static final String LOG_ADD_TAG_END = "addTag() return: ";
  private static final String LOG_GET_TAG_START = "getTag() called";
  private static final String LOG_GET_TAG_END = "getTag() return: ";
  private static final String LOG_GET_TAGS_START = "getTagEntities() called";
  private static final String LOG_GET_TAGS_END = "getTagEntities() return: ";

  private final TagService tagService;

  /**
   * Retrieves all tags
   *
   * @return list of tags
   */
  @GetMapping(value = "/v1/tags", produces = {MediaType.APPLICATION_JSON_VALUE})
  public TagListResponse getTags(
      @RequestParam(required = false, defaultValue = "5") @Min(value = 1, message = "itemsPerPage Min value should be 1") @Max(value = 20, message = "itemsPerPage should Not be more than 20") int itemsPerPage,
      @RequestParam(required = false, defaultValue = "1") @Min(value = 1, message = "page Min value should be 1") int page) {
    log.debug(LOG_GET_TAGS_START);
    SearchCriteria searchCriteriaRequest = new SearchCriteria();
    searchCriteriaRequest.setItemsPerPage(itemsPerPage);
    searchCriteriaRequest.setPage(page);
    TagListResponse tagListResponse = tagService.getAll(searchCriteriaRequest);
    tagListResponse.getTags()
        .forEach(t -> t.add(linkTo(methodOn(TagController.class).getTag(t.getId())).withSelfRel()));
    tagListResponse.add(
        linkTo(methodOn(TagController.class).getTags(itemsPerPage, page +1))
            .withSelfRel());
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
  public TagDTO getTag(
      @PathVariable("tagId") @Min(value = 1, message = "tagId Min value should be 1") int tagId) {
    log.debug(LOG_GET_TAG_START);
    TagDTO tagDTO = tagService.getById(tagId);
    tagDTO.add(linkTo(methodOn(TagController.class).getTag(tagDTO.getId())).withSelfRel());
    log.debug(LOG_GET_TAG_END + tagDTO);
    return tagDTO;
  }

  /**
   * Retrieves a tag by id
   *
   * @return tag
   */
  @GetMapping(value = "/v1/tags/favorite", produces = {MediaType.APPLICATION_JSON_VALUE})
  public TagDTO getFavoriteTag() {
    log.debug("get favorite start");
    TagDTO tagDTO = tagService.getFavorite();
    tagDTO.add(linkTo(methodOn(TagController.class).getTag(tagDTO.getId())).withSelfRel());
    log.debug("get favorite end" + tagDTO);
    return tagDTO;
  }

  /**
   * Saves a new tag
   *
   * @param tag
   * @return tag
   */
  @PostMapping(value = "/v1/tags", produces = {MediaType.APPLICATION_JSON_VALUE})
  public TagDTO addTag(@Valid @RequestBody TagDTO tag) {
    log.debug(LOG_ADD_TAG_START + tag);
    TagDTO tagDTO = tagService.insert(tag);
    tagDTO.add(linkTo(methodOn(TagController.class).getTag(tagDTO.getId())).withSelfRel());
    log.debug(LOG_ADD_TAG_END + tagDTO);
    return tagDTO;
  }

  /**
   * Updates a tag
   *
   * @param tag
   * @return tag
   */
  @PutMapping(value = "/v1/tags/{tagId}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public TagDTO updateTag(
      @PathVariable("tagId") @Min(value = 1, message = "tagId Min value should be 1") int tagId,
      @Valid @RequestBody TagDTO tag) {
    log.debug(LOG_UPDATE_START + tag);
    tag.setId(tagId);
    TagDTO tagDTO = tagService.update(tag);
    tagDTO.add(linkTo(methodOn(TagController.class).getTag(tagDTO.getId())).withSelfRel());
    log.debug(LOG_UPDATE_END + tagDTO);
    return tagDTO;
  }

  /**
   * Deletes a tag by id
   *
   * @param tagId
   */
  @DeleteMapping(value = "/v1/tags/{tagId}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public void deleteTag(
      @PathVariable("tagId") @Min(value = 1, message = "tagId Min value should be 1") int tagId) {
    log.debug(LOG_DELETE_START + tagId);
    tagService.delete(tagId);
    log.debug(LOG_DELETE_END + tagId);
  }
}
