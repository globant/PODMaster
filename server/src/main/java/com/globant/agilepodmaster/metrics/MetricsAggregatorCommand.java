package com.globant.agilepodmaster.metrics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.globant.agilepodmaster.metrics.partitions.Partition;
import com.globant.agilepodmaster.metrics.partitions.Partitioner;

@Component
public class MetricsAggregatorCommand {
  private MetricsAggregator aggregator;
  private SprintPodMetricRepository repo;

  @Autowired
  public MetricsAggregatorCommand(MetricsAggregator aggregator, SprintPodMetricRepository repo) {
    this.aggregator = aggregator;
    this.repo = repo;
  }

  public List<MetricAggregation> execute(
      List<Partitioner<SprintPodMetric, Partition<?>>> partitioners) {

    Iterable<SprintPodMetric> spmList = repo.findAll();

    Map<Set<Partition<?>>, List<SprintPodMetric>> partitions = createPartitions(spmList,
        partitioners);
    List<MetricAggregation> aggregated = this.aggregate(partitions);

    return aggregated;
  }

  private List<MetricAggregation> aggregate(Map<Set<Partition<?>>, List<SprintPodMetric>> partitions) {

    List<MetricAggregation> aggregated = new LinkedList<MetricAggregation>();
    partitions.forEach((p, l) -> aggregated.add(new MetricAggregation(p, aggregator.aggregate(l))));

    return aggregated;
  }

  private SprintPodMetricPartitionsMap createPartitions(Iterable<SprintPodMetric> spmList,
      List<Partitioner<SprintPodMetric, Partition<?>>> partitioners) {

    SprintPodMetricPartitionsMap partitions = new SprintPodMetricPartitionsMap();

    spmList.forEach(spm -> partitions.get(createPartitions(spm, partitioners)).add(spm));

    return partitions;
  }

  private Set<Partition<?>> createPartitions(SprintPodMetric spm,
      List<Partitioner<SprintPodMetric, Partition<?>>> partitioners) {

    Set<Partition<?>> partition = new HashSet<Partition<?>>();
    partitioners.forEach(p -> partition.add(p.extractPartition(spm)));

    return partition;
  }
}

@SuppressWarnings("serial")
class SprintPodMetricPartitionsMap extends HashMap<Set<Partition<?>>, List<SprintPodMetric>> {
  @SuppressWarnings("unchecked")
  @Override
  public List<SprintPodMetric> get(Object key) {
    this.putIfAbsent((Set<Partition<?>>) key, new LinkedList<SprintPodMetric>());
    return super.get(key);
  }
}
