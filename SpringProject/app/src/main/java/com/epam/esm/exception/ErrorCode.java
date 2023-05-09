package com.epam.esm.exception;

/**
 * Enum for application specific error codes
 */
public enum ErrorCode {
  ERROR_TAG(40401, "Tag not found"),
  ERROR_BODY_VALIDATION(400100, "Request body validation error"),
  ERROR_PARAM_VALIDATION(400200, "Request parameter validation error"),
  ERROR_CERTIFICATE(40402, "Certificate not found");

  private int internalErrorCode;
  private String message;

  private ErrorCode(int internalErrorCode, String message) {
    this.internalErrorCode = internalErrorCode;
    this.message = message;
  }

  public int getInternalErrorCode() {
    return internalErrorCode;
  }

  public String getMessage() {
    return message;
  }
}
