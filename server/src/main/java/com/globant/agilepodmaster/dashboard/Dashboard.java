package com.globant.agilepodmaster.dashboard;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.globant.agilepodmaster.core.AbstractEntity;
import com.globant.agilepodmaster.widget.Widget;

/**
 * Entity that represents a Dashboard.
 * @author Andres Postiglioni
 *
 */
@Entity
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = false)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Dashboard extends AbstractEntity {
  
  /**
   * Kind of Dashboards.
   */
  public static enum DashboardType {
    Account, Project, Release, Sprint, Pod
  }

  @Getter
  @Setter
  private boolean locked;

  @Getter
  @Setter
  @OneToMany(mappedBy = "dashboard")
  public Set<DashboardWidget> widgets;

  @NonNull
  @Getter
  private DashboardType type;

  private Dashboard() {
    this.widgets = new HashSet<DashboardWidget>();
  }

  /**
   * Constructor.
   * @param type type of dashboard.
   */
  @JsonCreator
  public Dashboard(@JsonProperty("type") DashboardType type) {
    this();
    this.type = type;
  }

  /**
   * Add a widget to this dashboard.
   * @param widget the widget to add.
   */
  public void addWidget(Widget widget) {
    DashboardWidget db = new DashboardWidget(this, widget, false);
    this.widgets.add(db);
  }
}