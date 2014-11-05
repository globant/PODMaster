package com.globant.agilepodmaster.test.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Base clase to convert and object json format.
 * @author Andres Postiglioni.
 *
 * @param <S>
 */
public abstract class JsonBuilder<S> {
  
  /**
   * Convert to json format.
   * @return a String in json format.
   */
  public String buildJson() {
    return asJson(this.build());
  }
  
  /**
   * Build the object. 
   * @return the object.
   */
  public abstract S build();

  /**
   * Convert an object to an String in json format.
   * @param data object to convert.
   * @param <S> data object type.
   * @return a string in json format.
   */
  public static <S> String asJson(S data) {
    try {
      return new ObjectMapper().writeValueAsString(data);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }  
}
