package com.globant.agilepodmaster.metrics.partition;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @author Andres Postiglioni
 *
 */
@Component
public class PartitionerConverter implements Converter<String, Partitioner<?>> {
  private final List<Partitioner<?>> partitioners;
  
  @Autowired  
  public PartitionerConverter(List<Partitioner<?>> partitioners) {
    this.partitioners = Collections.unmodifiableList(partitioners);
  }

  @Override
  public Partitioner<?> convert(String source) {
    return partitioners.stream().filter(p -> p.accepts(source)).findFirst().get();
  }
}
