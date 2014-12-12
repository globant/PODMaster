package com.globant.agilepodmaster.metrics;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import com.globant.agilepodmaster.metrics.Quarter;
import com.globant.agilepodmaster.metrics.YearQuarter;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Test sorting of YearQuarter.
 * @author jose.dominguez@globant.com
 *
 */
public class YearQuarterTest {

  /**
   * Test Comparator.
   */
  @Test
  public void testComparator() {

    YearQuarter yq1 = new YearQuarter(2014, Quarter.Q1);
    YearQuarter yq2 = new YearQuarter(2014, Quarter.Q2);
    YearQuarter yq3 = new YearQuarter(2015, Quarter.Q1);
    YearQuarter yq4 = new YearQuarter(2015, Quarter.Q2);

    List<YearQuarter> quarters = Arrays.asList(yq4, yq2, yq1, yq3);

    Collections.sort(quarters);

    assertThat(quarters, contains(yq1, yq2, yq3, yq4));
  }

}
