package com.epam.esm.response;

import lombok.Data;

/**
 * ErrorResponse model
 */
@Data
public class ErrorResponse {

  private String errorMessage;
  private int errorCode;

}
