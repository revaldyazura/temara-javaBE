package com.temara.backend.exception;

import java.util.Map;

public class GeneralException extends RuntimeException {

  private static final long serialVersionUID = 6062004051956765122L;
  private Throwable rootError;
  private Map<String, String> headers;

  public GeneralException() {
    super("Exception occured, pleas contact administrator!");
  }

  public GeneralException(String message) {
    super(message);
    this.rootError = null;
  }

  public GeneralException(String message, Throwable rootError) {
    super(message);
    this.rootError = rootError;
  }

  @Override
  public Throwable getCause() {
    return rootError;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }
}
