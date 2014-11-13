package com.globant.agilepodmaster.sync.reading.db;

import com.globant.agilepodmaster.core.PodMemberRepository;
import com.globant.agilepodmaster.core.PodRepository;
import com.globant.agilepodmaster.sync.SyncContext;
import com.globant.agilepodmaster.sync.reading.PodsBuilder;
import com.globant.agilepodmaster.sync.reading.Reader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

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
    // TODO Auto-generated method stub
    
  }


}
