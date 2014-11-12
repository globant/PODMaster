package com.globant.agilepodmaster.core;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Entity representing a member of a POD.
 * @author jose.dominguez@globant.com
 *
 */
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@ToString
public class PodMember extends AbstractEntity {

  @Getter
  @NonNull
  private String firstName;

  @Getter
  @NonNull
  private String lastName;

  @Getter
  @NonNull
  private String email;

  @Getter
  @NonNull
  @ManyToOne
  @JsonIgnore
  private Pod pod;

}
