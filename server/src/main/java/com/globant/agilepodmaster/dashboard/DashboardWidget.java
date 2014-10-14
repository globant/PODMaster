package com.globant.agilepodmaster.dashboard;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.globant.agilepodmaster.core.AbstractEntity;
import com.globant.agilepodmaster.widget.Widget;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
public class DashboardWidget extends AbstractEntity {
  @SuppressWarnings("unused")
  private DashboardWidget() {
    // required by Hibernate
  }

  @NonNull
  @ManyToOne
  @JsonIgnore
  private Dashboard dashboard;
  
  @ManyToOne(cascade = CascadeType.ALL)
  @NonNull
  private Widget widget;
  // private final Dashboard dashboard;

  @NonNull
  private Boolean collapsed;
}
