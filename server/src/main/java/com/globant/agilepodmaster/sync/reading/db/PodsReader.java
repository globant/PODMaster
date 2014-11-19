package com.globant.agilepodmaster.sync.reading.db;

import com.globant.agilepodmaster.sync.SyncContext;
import com.globant.agilepodmaster.sync.reading.PodsBuilder;
import com.globant.agilepodmaster.sync.reading.Reader;

import org.springframework.stereotype.Service;

/**
 * Class that reads Pods and its members for a DB.
 * 
 * @author jose.dominguez@globant.com
 *
 */
@Service
public class PodsReader implements Reader<PodsBuilder> {

  @Override
  public void readInto(PodsBuilder podsBuilder,  SyncContext context) {
    podsBuilder
    .withPod("POD1")
      .withPodMember("Walter", "Pacheco", "walter@ea.com").addToPod()
      .withPodMember("Pedro", "Laurentz", "pedro@ea.com.com").addToPod()
      .withPodMember("Maria", "Gomez", "maria@ea.com").addToPod()
      .addToSnapshot()
    .withPod("POD2")
      .withPodMember("Jorge", "Dartes", "jorge@ea.com").addToPod()
      .withPodMember("Claudia", "Parigi", "claudia@ea.com").addToPod()
      .withPodMember("Roxana", "Andrade", "roxana@ea.com").addToPod()
      .addToSnapshot();
  }

}
