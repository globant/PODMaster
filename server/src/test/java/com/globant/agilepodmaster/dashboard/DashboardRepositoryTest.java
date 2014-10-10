package com.globant.agilepodmaster.dashboard;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.globant.agilepodmaster.AbstractUnitTest;

@Transactional
public class DashboardRepositoryTest extends AbstractUnitTest {
  @Autowired
  DashboardRepository repo;

  @Test
  public void shouldCreateDashboard() {
    Dashboard dashboard = DashboardFixtures.createDashboard(Dashboard.DashboardType.Account)
        .withWidget("Widget Title", "Widget Name").build();

    Dashboard dashboardCreated = repo.save(dashboard);

    assertThat(dashboardCreated, notNullValue());
    assertThat(dashboardCreated.getId(), notNullValue());

    System.out.printf("Dashboard id: %d%n", dashboardCreated.getId());
  }
}
