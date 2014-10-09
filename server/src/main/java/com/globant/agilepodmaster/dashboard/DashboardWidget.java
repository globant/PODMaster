package com.globant.agilepodmaster.dashboard;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.globant.agilepodmaster.core.AbstractEntity;
import com.globant.agilepodmaster.widget.Widget;

@Entity
@Data
@RequiredArgsConstructor
public class DashboardWidget extends AbstractEntity {
  private DashboardWidget() {
  }

  @ManyToOne(cascade = CascadeType.ALL)
  @NonNull
  private Widget widget;
  // private final Dashboard dashboard;

  @NonNull
  private Boolean collapsed;
}
