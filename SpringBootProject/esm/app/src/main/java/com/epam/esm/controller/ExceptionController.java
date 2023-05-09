package com.epam.esm.controller;

import com.epam.esm.exception.ErrorCode;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.model.response.ErrorResponse;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * General Spring handler for exceptions
 */
@ControllerAdvice
@Slf4j
public class ExceptionController {

  private static final String ERROR_MESSAGE = "Database error";
  private static final String SERVER_ERROR_MESSAGE = "Server error";
  private static final String LOG_NOT_FOUND_EXCEPTION_START = "Handling NotFoundException exception start";
  private static final String LOG_NOT_FOUND_EXCEPTION_END = "Return NotFoundException exception response: ";
  private static final String LOG_DATA_ACCESS_EXCEPTION_START = "Handling DataAccessException exception start";
  private static final String LOG_DATA_ACCESS_EXCEPTION_END = "Return DataAccessException exception response: ";
  private static final String LOG_GENERAL_EXCEPTION_START = "Handling general Exception start";
  private static final String LOG_GENERAL_EXCEPTION_END = "Return general Exception response: ";
  private static final String LOG_GENERAL_MESSAGE = "Exception happened: ";
  private static final String LOG_BODY_VALIDATION_EXCEPTION_START = "Body validation exception start";
  private static final String LOG_BODY_VALIDATION_EXCEPTION_ERROR = "Body validation exception happened: ";
  private static final String LOG_BODY_VALIDATION_EXCEPTION_END = "Body validation exception end";
  private static final String LOG_PARAMETER_VALIDATION_EXCEPTION_START = "Request parameter validation exception start";
  private static final String LOG_PARAMETER_VALIDATION_EXCEPTION_ERROR = "Request parameter validation exception happened: ";
  private static final String LOG_PARAMETER_VALIDATION_EXCEPTION_END = "Request parameter validation exception end";
  private static final String DASH = "-";

  /**
   * ExceptionHandler for NotFoundException
   *
   * @param exception
   * @return ResponseEntity
   */
  @ExceptionHandler(value = NotFoundException.class)
  public ResponseEntity<ErrorResponse> exception(NotFoundException exception) {
    log.debug(LOG_NOT_FOUND_EXCEPTION_START);
    log.error(LOG_GENERAL_MESSAGE, exception);
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setErrorMessage(exception.getMessage());
    errorResponse.setErrorCode(exception.getErrorCode().getInternalErrorCode());
    ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    log.debug(LOG_NOT_FOUND_EXCEPTION_END + responseEntity);
    return responseEntity;
  }

  @ExceptionHandler(value = EntityNotFoundException.class)
  public ResponseEntity<ErrorResponse> exception(EntityNotFoundException exception) {
    log.debug(LOG_NOT_FOUND_EXCEPTION_START);
    log.error(LOG_GENERAL_MESSAGE, exception);
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setErrorMessage(exception.getMessage());

    //TODO: codes for User, Certificate, Order...
    errorResponse.setErrorCode(ErrorCode.ERROR_TAG.getInternalErrorCode());

    ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    log.debug(LOG_NOT_FOUND_EXCEPTION_END + responseEntity);
    return responseEntity;
  }

  /**
   * ExceptionHandler for DataAccessException
   *
   * @param exception
   * @return
   */
  @ExceptionHandler(value = DataAccessException.class)
  public ResponseEntity<ErrorResponse> exception(DataAccessException exception) {
    log.debug(LOG_DATA_ACCESS_EXCEPTION_START);
    log.error(LOG_GENERAL_MESSAGE, exception);
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setErrorMessage(ERROR_MESSAGE);
    errorResponse.setErrorCode(500);
    ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(errorResponse,
        HttpStatus.INTERNAL_SERVER_ERROR);
    log.debug(LOG_DATA_ACCESS_EXCEPTION_END + responseEntity);
    return responseEntity;
  }

  /**
   * ExceptionHandler for general exceptions
   *
   * @param exception
   * @return
   */
  @ExceptionHandler(value = Exception.class)
  public ResponseEntity<ErrorResponse> exception(Exception exception) {
    log.debug(LOG_GENERAL_EXCEPTION_START);
    log.error(LOG_GENERAL_MESSAGE, exception);
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setErrorMessage(SERVER_ERROR_MESSAGE);
    errorResponse.setErrorCode(500);
    ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(errorResponse,
        HttpStatus.INTERNAL_SERVER_ERROR);
    log.debug(LOG_GENERAL_EXCEPTION_END + responseEntity);
    return responseEntity;
  }

  /**
   * ExceptionHandler for request body validation exception
   * @param exception
   * @return
   */
  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> validationException(MethodArgumentNotValidException exception) {
    log.debug(LOG_BODY_VALIDATION_EXCEPTION_START);
    log.error(LOG_BODY_VALIDATION_EXCEPTION_ERROR, exception);

    String errorMessage = exception.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(x -> x.getDefaultMessage())
        .collect(Collectors.joining(DASH));

    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setErrorMessage(errorMessage);
    errorResponse.setErrorCode(ErrorCode.ERROR_BODY_VALIDATION.getInternalErrorCode());

    ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(errorResponse,
        HttpStatus.BAD_REQUEST);
    log.debug(LOG_BODY_VALIDATION_EXCEPTION_END + responseEntity);
    return responseEntity;
  }

  /**
   * ExceptionHandler for request parameter validation exception
   * @param exception
   * @return
   */
  @ExceptionHandler(value = ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> validationException(ConstraintViolationException exception) {
    log.debug(LOG_PARAMETER_VALIDATION_EXCEPTION_START);
    log.error(LOG_PARAMETER_VALIDATION_EXCEPTION_ERROR, exception);

    String errorMessage = exception.getConstraintViolations().stream()
        .map(x -> x.getMessage())
        .collect(Collectors.joining(DASH));

    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setErrorMessage(errorMessage);
    errorResponse.setErrorCode(ErrorCode.ERROR_PARAM_VALIDATION.getInternalErrorCode());

    ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(errorResponse,
        HttpStatus.BAD_REQUEST);
    log.debug(LOG_PARAMETER_VALIDATION_EXCEPTION_END + responseEntity);
    return responseEntity;
  }
}
