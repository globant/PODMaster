package com.globant.agilepodmaster.dashboard;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.globant.agilepodmaster.dashboard.Dashboard.DashboardType;

/**
 * Test for Dashboard entity.
 *
 */
public class DashboardTest {

  /**
   * Test equals.
   */
  @Test
  public void equals() {
    Dashboard dashboard = new Dashboard(DashboardType.Account);
    
    assertThat(dashboard, equalTo(dashboard));
  }

}
