package com.globant.agilepodmaster.metrics;

import com.globant.agilepodmaster.core.QAbstractMetric;
import com.globant.agilepodmaster.metrics.filter.BooleanExpressionPropertyEditor;
import com.globant.agilepodmaster.metrics.filter.FunctionPropertyEditor;
import com.globant.agilepodmaster.metrics.partition.Partition;
import com.globant.agilepodmaster.metrics.partition.Partitioner;
import com.globant.agilepodmaster.snapshot.AbstractMetric;
import com.mysema.query.types.expr.BooleanExpression;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * Controller that offer a rest API for metrics.
 * @author Andres Postiglioni
 *
 */
@RestController
@RequestMapping("/snapshots")
public class MetricsController {
  private MetricsAggregatorCommand command;

  /**
   * Constructor.
   * @param command the command that implements the logic.
   */
  @Autowired
  public MetricsController(MetricsAggregatorCommand command) {
    this.command = command;
  }

  /**
   * Provides all metrics grouped depending on partitioners and filtered.
   * @param snapshotId the snapshot id.
   * @param partitioners the partitioners to group metrics.
   * @param filters the filters 
   * @param calculators the list of metrics to be calculated. 
   * @return  A collection of metric aggregations
   */
  @RequestMapping("/{snapshotid}/metrics")
  public MetricsAggregationCollectionResource metrics(
      @PathVariable("snapshotid") 
        Long snapshotId,
      @RequestParam(value = "aggregation")
        List<Partitioner<? extends Partition<?>>> partitioners, 
      @RequestParam(value = "filter", required = false) 
        List<BooleanExpression> filters,
      @RequestParam(value = "calculators", required = false) 
      List<Function<List<AbstractMetric>, Metric<?>>> calculators) {
    
    if (filters == null) {
      filters = new LinkedList<BooleanExpression>();
    }
    
        
    filters.add(QAbstractMetric.abstractMetric.snapshot().id.eq(snapshotId));
    BooleanExpression predicate = null;
    
    if (filters != null) {
      predicate = BooleanExpression.allOf(filters.toArray(new BooleanExpression[0]));
    }

    Set<MetricAggregation> aggregated = command.execute(partitioners, predicate, calculators);

    MetricsAggregationCollectionResource response = 
        new MetricsAggregationCollectionResource(aggregated);

    //TODO: The filter parameters is rendered inaccurately
//    Link selfLink = linkTo(methodOn(MetricsController.class).metrics(partitioners, filters))
//                    .withSelfRel();
//    response.add(selfLink);
    
    return response;
  }
  
  /**
   * Registers custom editor to a binder.
   * @param binder the binder.
   */
  @InitBinder
  public void initBinderAll(WebDataBinder binder) {
    binder.registerCustomEditor(BooleanExpression.class, new BooleanExpressionPropertyEditor());
    binder.registerCustomEditor(Function.class, new FunctionPropertyEditor());
    
  }  
}