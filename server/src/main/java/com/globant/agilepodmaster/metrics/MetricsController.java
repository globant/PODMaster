package com.globant.agilepodmaster.metrics;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.globant.agilepodmaster.core.QAbstractMetric;
import com.globant.agilepodmaster.metrics.filter.BooleanExpressionPropertyEditor;
import com.globant.agilepodmaster.metrics.partition.Partition;
import com.globant.agilepodmaster.metrics.partition.Partitioner;
import com.mysema.query.types.expr.BooleanExpression;

@RestController
@RequestMapping("/snapshots")
public class MetricsController {
  private MetricsAggregatorCommand command;

  @Autowired
  public MetricsController(MetricsAggregatorCommand command) {
    this.command = command;
  }

  @RequestMapping("/{snapshotid}/metrics")
  public MetricsAggregationCollectionResource metrics(
      @PathVariable("snapshotid") 
        Long snapshotId,
      @RequestParam(value = "aggregation")
        List<Partitioner<? extends Partition<?>>> partitioners, 
      @RequestParam(value = "filter", required = false) 
        List<BooleanExpression> filters) {
    
    if (filters == null) {
      filters = new LinkedList<BooleanExpression>();
    }
    
    filters.add(QAbstractMetric.abstractMetric.snapshot().id.eq(snapshotId));
    BooleanExpression predicate = null;
    
    if (filters != null) {
      predicate = BooleanExpression.allOf(filters.toArray(new BooleanExpression[0]));
    }

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