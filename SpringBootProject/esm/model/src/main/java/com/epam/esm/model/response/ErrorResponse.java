package com.epam.esm.model.response;

import lombok.Data;

/**
 * ErrorResponse model
 */
@Data
public class ErrorResponse {

  private String errorMessage;
  private int errorCode;

}
