package com.globant.agilepodmaster.metrics.filter;

import com.globant.agilepodmaster.metrics.Metric;
import com.globant.agilepodmaster.metrics.MetricsAggregator;
import com.globant.agilepodmaster.snapshot.AbstractMetric;

import java.beans.PropertyEditorSupport;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Function;

import lombok.NoArgsConstructor;

/**
 * Property Editor to map strings to Function. This Function will calculate and
 * return a Metric.
 * 
 * @author jose.dominguez@globant.com
 *
 */
@NoArgsConstructor
public class FunctionPropertyEditor extends PropertyEditorSupport {
  
  /**
   * Constructor.
   * @param value the Function.
   */
  public FunctionPropertyEditor(
      Function<List<AbstractMetric>, Metric<?>> value) {
    super(value);
  }

  @Override
  public String getAsText() {
    @SuppressWarnings("unchecked")
    Function<List<AbstractMetric>, Metric<?>> function = 
        (Function<List<AbstractMetric>, Metric<?>>) super
        .getValue();

    for (Entry<String, Function<List<AbstractMetric>, Metric<?>>> entry 
        : MetricsAggregator.metricCalculatorMap
        .entrySet()) {
      if (function.equals(entry.getValue())) {
        return entry.getKey();
      }
    }
    throw new IllegalArgumentException("Unknown metric for function: " + function);
  }

  @Override
  public void setAsText(String metricName) throws IllegalArgumentException {
    if (!MetricsAggregator.metricCalculatorMap.containsKey(metricName)) {
      throw new IllegalArgumentException("Invalid metric name: " + metricName);
    }
    super.setValue(MetricsAggregator.metricCalculatorMap.get(metricName));
  }

}
