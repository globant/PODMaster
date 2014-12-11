package com.globant.agilepodmaster;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.Getter;

/**
 * Builds a String with Json format.
 * @author Andres Postiglioni
 *
 */
public class JsonBuilder {
  private List<String> body = new LinkedList<String>();
  
  /**
   * Add a property with a number as value to the body.
   * @param name the name of the property.
   * @param number the value of the property.
   * @return the same builder.
   */
  public JsonBuilder withProperty(String name, Number number) {
    addToBody(name, number);
    return this;
  }

  private void addToBody(String property, Object number) {
    body.add(this.quote(property) + ":" + number);
  }

  private String quote(String str) {
    return str != null ? "\"" + str + "\"" : null; 
  }

  /**
   * Add a property with a String as value to the body.
   * @param name the name of the property.
   * @param value the value of the property.
   * @return the same builder.
   */
  public JsonBuilder withProperty(String name, String value) {
    addToBody(name, this.quote(value));
    return this;
  }

  /**
   * Add a property with a nested value.
   * @param name the name.
   * @param function the function with the value.
   * @return the builder.
   */
  public JsonBuilder withNestedObject(String name, Function<JsonBuilder, JsonBuilder> function) {
    JsonBuilder builder = new JsonBuilder();
    addToBody(name, function.apply(builder).toString());
    return this;
  }

  /**
   * Add a property with an array as value.
   * @param name the name.
   * @param function the function that represents the array.
   * @return the builder.
   */
  public JsonBuilder withArray(String name, Function<ArrayBuilder, ArrayBuilder> function) {
    ArrayBuilder arrayBuilder = new ArrayBuilder();
    addToBody(name, function.apply(arrayBuilder).toString());
    
    return this;
  }

  /**
   * Build the json as String.
   * @return the String.
   */
  public String build() {
    return this.toString();
  }

  @Override
  public String toString() {
    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("{");
    strBuilder.append(
        body.stream().map(p -> p.toString()).collect(Collectors.joining(","))
    ); 
    strBuilder.append("}");
    
    return strBuilder.toString();

  }
  
  /**
   * Static Clas to build an array.
   *
   */
  public static class ArrayBuilder {
    @Getter
    private List<String> elements = new LinkedList<String>();
    
    /**
     * Add an element as a nested value.
     * @param function the function with the value.
     * @return the builder.
     */
    public ArrayBuilder addNestedObject(Function<JsonBuilder, JsonBuilder> function) {
      JsonBuilder builder = new JsonBuilder();
      elements.add(function.apply(builder).toString());
      return this;
    }

    @Override
    public String toString() {
      StringBuilder strBuilder = new StringBuilder();
      strBuilder.append("[");
      strBuilder.append(
          elements.stream().map(p -> p.toString()).collect(Collectors.joining(","))
      );
      strBuilder.append("]");
      
      return strBuilder.toString();
    }
  }
}