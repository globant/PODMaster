package com.globant.agilepodmaster.metrics.partition;

import com.globant.agilepodmaster.core.Quarter;
import com.globant.agilepodmaster.core.YearQuarter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Stream;


/**
 * Represents a set of Partitions.
 * 
 * @author jose.dominguez@globant.com
 *
 */
@SuppressWarnings("serial")
public class PartitionSet extends HashSet<Partition<?>> implements
    Comparable<PartitionSet> {


  @Override
  public int compareTo(PartitionSet other) {

    int result = compareTo(other, "project",
        (String u1, String u2) -> u1.hashCode() - u2.hashCode());
    if (result != 0) {
      return result;
    }

    result = compareTo(other, "pod", (String u1, String u2) -> u1.hashCode()
        - u2.hashCode());
    if (result != 0) {
      return result;
    }

    result = compareTo(other, "quarter",
        (Quarter u1, Quarter u2) -> u1.compareTo(u2));
    if (result != 0) {
      return result;
    }

    result = compareTo(other, "year",
        (Integer u1, Integer u2) -> u1.compareTo(u2));
    if (result != 0) {
      return result;
    }

    result = compareTo(other, "year/quarter",
        (YearQuarter u1, YearQuarter u2) -> u1.compareTo(u2));
    if (result != 0) {
      return result;
    }

    result = compareTo(other, "month",
        (Integer u1, Integer u2) -> u1.compareTo(u2));
    if (result != 0) {
      return result;
    }

    result = compareTo(other, "sprint",
        (Integer u1, Integer u2) -> u1.compareTo(u2));
    if (result != 0) {
      return result;
    }

    return 0;

  }

  @SuppressWarnings("unchecked")
  private <T> Partition<T> getPartition(Stream<Partition<?>> stream, String partitionName) {
    Partition<T> pp = null;
    Optional<Partition<?>> partition = stream
        .filter(t -> t.getPartition().equals(partitionName)).findAny();
    if (partition.isPresent()) {
      pp = (Partition<T>) partition.get();
    }
    return pp;
  }
  
  private <T> int compareTo(PartitionSet other, String partitionName,
      Comparator<T> comparator) {

    Partition<T> pp1 = getPartition(this.stream(), partitionName);
    Partition<T> pp2 = getPartition(other.stream(), partitionName);

    if (pp1 == null && pp2 != null) {
      return -1;
    } else if (pp1 != null && pp2 == null) {
      return 1;
    } else if (pp1 != null && pp2 != null) {
      return comparator.compare(pp1.getKey(), pp2.getKey());
    }
    return 0;
  } 
    
  /**
   * Builder.
   * @param partitions list of partitions.
   * @return a PartitionSet.
   */
  public static PartitionSet build(Partition<?>... partitions) {
    PartitionSet partitionSet = new PartitionSet();
    partitionSet.addAll(Arrays.asList(partitions));
    return partitionSet;

  }

}
