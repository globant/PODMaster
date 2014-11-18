package com.globant.agilepodmaster.metrics;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.globant.agilepodmaster.core.SprintPodMetric;
import com.globant.agilepodmaster.metrics.filter.BooleanExpressionPropertyEditor;
import com.globant.agilepodmaster.metrics.partition.Partition;
import com.globant.agilepodmaster.metrics.partition.Partitioner;
import com.mysema.query.types.expr.BooleanExpression;

@RestController
public class MetricsController {
  private MetricsAggregatorCommand command;

  @Autowired
  public MetricsController(MetricsAggregatorCommand command) {
    this.command = command;
  }

  @RequestMapping("/metrics")
  public MetricsAggregationCollectionResource metrics(
      @RequestParam(value = "aggregation")
        List<Partitioner<SprintPodMetric, ? extends Partition<?>>> partitioners, 
      @RequestParam(value = "filter", required = false) List<BooleanExpression> filters) {

    BooleanExpression predicate = filters != null 
        ? BooleanExpression.allOf(filters.toArray(new BooleanExpression[0])) 
        : null;

    Set<MetricAggregation> aggregated = command.execute(partitioners, predicate);

    MetricsAggregationCollectionResource response = 
        new MetricsAggregationCollectionResource(aggregated);

    //TODO: The filter parameters is rendered inaccurately
//    Link selfLink = linkTo(methodOn(MetricsController.class).metrics(partitioners, filters))
//                    .withSelfRel();
//    response.add(selfLink);
    
    return response;
  }
  
  @InitBinder
  public void initBinderAll(WebDataBinder binder) {
    binder.registerCustomEditor(BooleanExpression.class, new BooleanExpressionPropertyEditor());
  }  
}