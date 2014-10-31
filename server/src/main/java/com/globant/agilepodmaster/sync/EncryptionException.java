package com.globant.agilepodmaster.sync;

public class EncryptionException extends RuntimeException {
  public EncryptionException(String message, Exception exception) {
    super(message, exception);
  }
}
