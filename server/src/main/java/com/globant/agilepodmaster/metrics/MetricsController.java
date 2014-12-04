package com.globant.agilepodmaster.metrics;

import com.globant.agilepodmaster.core.QAbstractMetric;
import com.globant.agilepodmaster.metrics.filter.BooleanExpressionPropertyEditor;
import com.globant.agilepodmaster.metrics.partition.Partition;
import com.globant.agilepodmaster.metrics.partition.Partitioner;
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
   * @param collect the list of metrics to be shown. 
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
      @RequestParam(value = "collect", required = false) 
      List<String> collect) {
    
    if (filters == null) {
      filters = new LinkedList<BooleanExpression>();
    }
    
    filters.add(QAbstractMetric.abstractMetric.snapshot().id.eq(snapshotId));
    BooleanExpression predicate = null;
    
    if (filters != null) {
      predicate = BooleanExpression.allOf(filters.toArray(new BooleanExpression[0]));
    }

    Set<MetricAggregation> aggregated = command.execute(partitioners, predicate, collect);

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
  }  
}