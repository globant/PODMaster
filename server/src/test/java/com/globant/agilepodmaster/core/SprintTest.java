package com.globant.agilepodmaster.core;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Test of Sprint class.
 * @author jose.dominguez@globant.com
 *
 */
public class SprintTest {

  /**
   * Test sprint order.
   */
  @Test
  public void shouldSortSprintsProperly() {
    
    Organization organization = new Organization("Organization1");
    Product product = new Product("Product1", organization);
    Project project1 = new Project("Project1", product);
    Project project2 = new Project("Project2", product);
    Release release1 = new Release("Release1", project1);
    Release release2 = new Release("Release2", project2);

    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_MONTH, -5);

    Sprint sprint11 = new Sprint("Sprint1", release1, calendar.getTime(), new Date());
    Sprint sprint12 = new Sprint("Sprint2", release1, new Date(), new Date());
    Sprint sprint21 = new Sprint("Sprint1", release2, calendar.getTime(), new Date());
    Sprint sprint22 = new Sprint("Sprint2", release2, new Date(), new Date());
    
    List<Sprint> sortedList = Arrays.asList(sprint12, sprint21, sprint22, sprint11);

    java.util.Collections.sort(sortedList);
    
    assertThat(sortedList.get(0), equalTo(sprint11));
    assertThat(sortedList.get(1), equalTo(sprint12));
    assertThat(sortedList.get(2), equalTo(sprint21));
    assertThat(sortedList.get(3), equalTo(sprint22));
    
  }

}
