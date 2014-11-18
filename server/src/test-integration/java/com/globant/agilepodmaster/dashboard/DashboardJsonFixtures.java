package com.globant.agilepodmaster.dashboard;

import lombok.RequiredArgsConstructor;

import com.globant.agilepodmaster.test.utils.JsonBuilder;
import com.globant.agilepodmaster.widget.Widget;

/**
 * Dashbord Fixture to be treated as a json string.
 * @author Andres Postiglioni.
 *
 */
@RequiredArgsConstructor
public class DashboardJsonFixtures extends JsonBuilder<Dashboard> {
  private final Dashboard dashboard;

  /**
   * Creates a Dashboard to add widgets (DashboardFixtures).  
   * @param type type of dashboard to use.
   * @return a Dashboard to add widgets (DashboardFixtures).
   */
  public static DashboardJsonFixtures createDashboard(Dashboard.DashboardType type) {
    return new DashboardJsonFixtures(new Dashboard(type));
  }

  /**
   * Add a widget using default width and height and view name.
   * @param title the title.
   * @param name the name.
   * @return the same DashboardFixtures.
   */
  public DashboardJsonFixtures withWidget(String title, String name) {
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
  public DashboardJsonFixtures withWidget(String title, String name, int height, int width) {
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
  public DashboardJsonFixtures withWidget(String title, String name, int height, int width,
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
