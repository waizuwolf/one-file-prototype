package com.waizuwolf.onefile.common.exception;

public class InputValidationException extends Exception {

  public InputValidationException(String message) {
    super(message);
  }

  InputValidationException(String message, Throwable throwable) {
    super(message, throwable);
  }

}