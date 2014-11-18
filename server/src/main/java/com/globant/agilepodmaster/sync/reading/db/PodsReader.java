package com.globant.agilepodmaster.sync.reading.db;

import com.globant.agilepodmaster.core.PodMemberRepository;
import com.globant.agilepodmaster.core.PodRepository;
import com.globant.agilepodmaster.sync.SyncContext;
import com.globant.agilepodmaster.sync.reading.PodMemberDTO;
import com.globant.agilepodmaster.sync.reading.PodsBuilder;
import com.globant.agilepodmaster.sync.reading.Reader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that reads Pods and its members for a DB.
 * 
 * @author jose.dominguez@globant.com
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PodsReader implements Reader<PodsBuilder> {

  @Autowired
  PodRepository podRepository;

  @Autowired  
  PodMemberRepository podMemberRepository;


  @Override
  public void readInto(PodsBuilder builder, SyncContext context) {
    Map<String, PodMemberDTO> membersMap = new HashMap<String, PodMemberDTO>();

    PodMemberDTO memberDTO = new PodMemberDTO("walter@ea.com", "Walter", "Roa",
        "POD1");
    membersMap.put("walter@ea.com", memberDTO);

    memberDTO = new PodMemberDTO("pedro@ea.com", "Pedro", "Lopez", "POD1");
    membersMap.put("pedro@ea.com", memberDTO);

    memberDTO = new PodMemberDTO("maria@ea.com", "Maria", "Garcia", "POD1");
    membersMap.put("maria@ea.com", memberDTO);

    memberDTO = new PodMemberDTO("jorge@ea.com", "Jorge", "Perez", "POD2");
    membersMap.put("jorge@ea.com", memberDTO);

    memberDTO = new PodMemberDTO("claudia@ea.com", "Claudia", "Parigi", "POD2");
    membersMap.put("claudia@ea.com", memberDTO);

    memberDTO = new PodMemberDTO("rozana@ea.com", "Roxana", "Andrade", "POD2");
    membersMap.put("roxana@ea.com", memberDTO);

    builder.addMembersMap(membersMap);

  }


}
