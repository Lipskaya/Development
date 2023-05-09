package com.epam.esm.exception;

/**
 * Application specific model for entity not found cases
 */
public class NotFoundException extends RuntimeException {

  private final ErrorCode errorCode;

  public NotFoundException(String s, ErrorCode errorCode) {
    super(s);
    this.errorCode = errorCode;
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }
}

