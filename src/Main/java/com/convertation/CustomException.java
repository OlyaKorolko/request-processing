package com.convertation;

// тут я свое исключение сделала, конструкторы обычно вот такие используют. Не передают одно исключение в другое, это странно)
public class CustomException extends Exception {
  public CustomException(String message) {
    super(message);
  }

  public CustomException(String message, Throwable cause) {
    super(message, cause);
  }
}
