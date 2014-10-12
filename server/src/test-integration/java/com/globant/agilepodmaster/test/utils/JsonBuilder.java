package com.globant.agilepodmaster.test.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class JsonBuilder<S> {
  public String buildJson() {
    return asJson(this.build());
  }
  
  public abstract S build();

  public static <S> String asJson(S data) {
    try {
      return new ObjectMapper().writeValueAsString(data);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }  
}
