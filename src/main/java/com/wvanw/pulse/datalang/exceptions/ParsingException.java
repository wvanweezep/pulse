package com.wvanw.pulse.datalang.exceptions;

public class ParsingException extends RuntimeException {

  private final int index;

  public ParsingException(String message, int index) {
    super(message);
    this.index = index;
  }

  public int getIndex() {
    return index;
  }
}
