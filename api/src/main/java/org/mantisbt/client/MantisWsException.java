package org.mantisbt.client;

/**
 * @author Jeremie Lagarde
 * @since 1.2.5
 */
@SuppressWarnings("serial")
public class MantisWsException extends RuntimeException {

  public MantisWsException(String message) {
    super(message);
  }

  public MantisWsException(String message, Throwable cause) {
    super(message, cause);
  }
}
