package com.globant.agilepodmaster.dashboard;

import lombok.RequiredArgsConstructor;

import com.globant.agilepodmaster.test.utils.TestDataFixtures;
import com.globant.agilepodmaster.widget.Widget;

@RequiredArgsConstructor
public class DashboardFixtures extends TestDataFixtures<Dashboard> {
  private final Dashboard dashboard;

  public static DashboardFixtures createDashboard(Dashboard.DashboardType type) {
    return new DashboardFixtures(new Dashboard(type));
  }

  public DashboardFixtures withWidget(String title, String name) {
    return this.withWidget(title, name, 10, 20);
  }

  public DashboardFixtures withWidget(String title, String name, int height, int width) {
    return this.withWidget(title, name, height, width, String.format("%s - %s", title, name));
  }

  public DashboardFixtures withWidget(String title, String name, int height, int width,
      String viewName) {
    Widget widget = new Widget();
    widget.setName(name);
    widget.setTitle(title);
    widget.setHeight(height);
    widget.setWidth(width);
    widget.setViewName(viewName);

    dashboard.addWidget(widget);

    return this;
  }

  public Dashboard build() {
    return dashboard;
  }
}
