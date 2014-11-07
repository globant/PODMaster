package com.globant.agilepodmaster.metrics;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.globant.agilepodmaster.metrics.partitions.Partition;
import com.globant.agilepodmaster.metrics.partitions.Partitioner;

@RestController
public class MetricsController {
  @Autowired
  private EntityLinks entityLinks;

  @Autowired
  private MetricsAggregatorCommand aggregatorCommand;

  @RequestMapping("/metrics")
  public MetricsAggregationCollectionResource metrics(
      @RequestParam(value="aggregation")
        List<Partitioner<SprintPodMetric, Partition<?>>> partitioners, 
      @RequestParam(value="filter", required=false) List<FilterCriteria> filter) {

    List<MetricAggregation> aggregated = aggregatorCommand.execute(partitioners);

    MetricsAggregationCollectionResource response = 
        new MetricsAggregationCollectionResource(aggregated);
    
    Link selfLink = linkTo(methodOn(MetricsController.class).metrics(partitioners, filter))
                      .withSelfRel();
    response.add(selfLink);
    
    return response;
  }
}