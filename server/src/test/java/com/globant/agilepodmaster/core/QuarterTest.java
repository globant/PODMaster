package com.globant.agilepodmaster.core;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Test Quarter class.
 * @author jose.dominguez@globant.com
 *
 */
public class QuarterTest {

  /**
   * Test Comparator.
   */
  @Test
  public void testComparator() {

    List<Quarter> quarters = Arrays.asList(Quarter.Q4, Quarter.Q2, Quarter.Q1,
        Quarter.Q3);

    Collections.sort(quarters);

    assertThat(quarters,
        contains(Quarter.Q1, Quarter.Q2, Quarter.Q3, Quarter.Q4));
  }

}
