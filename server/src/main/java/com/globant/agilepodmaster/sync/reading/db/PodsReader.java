package com.globant.agilepodmaster.sync.reading.db;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.globant.agilepodmaster.sync.SyncContext;
import com.globant.agilepodmaster.sync.reading.PodsBuilder;
import com.globant.agilepodmaster.sync.reading.Reader;

/**
 * Class that reads Pods and its members for a DB.
 * 
 * @author jose.dominguez@globant.com
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PodsReader implements Reader<PodsBuilder> {

  @Override
  public void readInto(PodsBuilder podsBuilder,  SyncContext context) {
    podsBuilder
    .withPod("POD1")
      .withPodMember("Walter", "Pacheco", "walter").addToPod()
      .withPodMember("Pedro", "Laurentz", "pedro").addToPod()
      .withPodMember("Maria", "Gomez", "maria").addToPod()
      .addToSnapshot()
      .withPod("POD2")
      .withPodMember("Jorge", "Dartes", "jorge").addToPod()
      .withPodMember("Claudia", "Parigi", "claudia").addToPod()
      .withPodMember("Roxana", "Andrade", "roxana").addToPod()
      .addToSnapshot();
  }

}
