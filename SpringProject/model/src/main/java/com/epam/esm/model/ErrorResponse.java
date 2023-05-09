package com.epam.esm.model;

/**
 * ErrorResponse model
 */
public class ErrorResponse {

  private String errorMessage;
  private int errorCode;

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public int getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(int errorCode) {
    this.errorCode = errorCode;
  }
}
