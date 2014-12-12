package com.globant.agilepodmaster.organization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.PrincipalSid;

import com.globant.agilepodmaster.AbstractEntity;
import com.globant.agilepodmaster.security.SecurityAclService;

@RepositoryEventHandler(Organization.class)
public class OrganizationCrudEventHandler {
  private final SecurityAclService securityService;

  @Autowired
  public OrganizationCrudEventHandler(SecurityAclService securityService) {
    this.securityService = securityService;
  }


  @HandleAfterCreate
  public void afterCreate(Organization organization) {
    this.addACL(organization);
  }

  private void addACL(AbstractEntity type) {
    if (type != null) {
      PrincipalSid ownerPrincipal = new PrincipalSid("rod");

      securityService.addPermission(type, ownerPrincipal, BasePermission.ADMINISTRATION);
      securityService.addPermission(type, ownerPrincipal, BasePermission.READ);
      securityService.addPermission(type, ownerPrincipal, BasePermission.WRITE);
      securityService.addPermission(type, ownerPrincipal, BasePermission.DELETE);

      GrantedAuthoritySid adminRole = new GrantedAuthoritySid("ROLE_ADMIN");
      securityService.addPermission(type, adminRole, BasePermission.ADMINISTRATION);
    }
  }
}
