package com.globant.agilepodmaster.dashboard;

import com.globant.agilepodmaster.widget.Widget;

import lombok.RequiredArgsConstructor;

/**
 * Builder of dashboards + widgets.
 * @author Andres Postiglioni.
 *
 */
@RequiredArgsConstructor
public class DashboardFixtures {
  private final Dashboard dashboard;

  /**
   * Creates dashboard fixtures of a type.
   * @param type dashboard type.
   * @return a dashboard fixtures.
   */
  public static DashboardFixtures createDashboard(Dashboard.DashboardType type) {
    return new DashboardFixtures(new Dashboard(type));
  }

  /**
   * Add a widget using default width and height and view name.
   * @param title the title.
   * @param name the name.
   * @return the same DashboardFixtures.
   */
  public DashboardFixtures withWidget(String title, String name) {
    return this.withWidget(title, name, 10, 20);
  }

  /**
   * Add a widget using default view name.
   * @param title the title.
   * @param name the name.
   * @param height the height.
   * @param width the width.
   * @return the same DashboardFixtures.
   */
  public DashboardFixtures withWidget(String title, String name, int height, int width) {
    return this.withWidget(title, name, height, width, String.format("%s - %s", title, name));
  }

  /** Add a widget using default view name.
   * @param title the title.
   * @param name the name.
   * @param height the height.
   * @param width the width.
   * @param viewName the view name.
   * @return the same DashboardFixtures.
   */
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

  /**
   * Build the dashboard.
   * @return the dashboard.
   */
  public Dashboard build() {
    return dashboard;
  }
}
