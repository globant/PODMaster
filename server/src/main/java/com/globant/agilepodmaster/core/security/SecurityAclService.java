package com.globant.agilepodmaster.core.security;

import java.util.List;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.globant.agilepodmaster.core.AbstractEntity;

@Service
@Slf4j
public class SecurityAclService {
  private MutableAclService mutableAclService;

  public SecurityAclService() {
  }

  @Transactional(readOnly = false)
  public void addPermission(AbstractEntity element, Sid recipient, Permission permission) {
    MutableAcl acl;
    // ObjectIdentity oid = new ObjectIdentityImpl(AbstractEntity.class,
    // element.getId());
    ObjectIdentity oid = new ObjectIdentityImpl(element);

    try {
      acl = (MutableAcl) mutableAclService.readAclById(oid);
    } catch (NotFoundException nfe) {
      acl = mutableAclService.createAcl(oid);
    }

    acl.insertAce(acl.getEntries().size(), permission, recipient, true);
    mutableAclService.updateAcl(acl);

    log.debug("Added permission " + permission + " for Sid " + recipient + " contact " + element);
  }

  @Transactional(readOnly = false)
  public boolean addAccessControlEntry(
      AbstractEntity element, Sid recipient, Permission permission) {
    Assert.notNull(element, "AbstractEntity required");
    Assert.notNull(recipient, "recipient required");
    Assert.notNull(permission, "permission required");

    MutableAcl acl;
    // ObjectIdentity oid = new ObjectIdentityImpl(AbstractEntity.class,
    // element.getId());
    ObjectIdentity oid = new ObjectIdentityImpl(element);

    try {
      acl = (MutableAcl) mutableAclService.readAclById(oid);
    } catch (NotFoundException nfe) {
      acl = mutableAclService.createAcl(oid);
    }

    /*
     * handle duplicate ACL entries
     * http://forum.springsource.org/showthread.php?
     * 73022-Should-AclImpl-allow-duplicate-permissions
     */
    if (doesACEExists(element, recipient, permission)) {
      log.debug("ACE already exists, element:" + element.getId() + ", Sid:" + recipient
          + ", permission:" + permission);
    } else {
      acl.insertAce(acl.getEntries().size(), permission, recipient, true);
      mutableAclService.updateAcl(acl);
      log.debug("Added permission " + permission + " for Sid " + recipient + " contact "
          + element);
    }
    return true;
  }

  /*
   * Check if ACE - Access Control Entry exists already
   */
  public boolean doesACEExists(AbstractEntity element, Sid recipient, Permission permission) {
    boolean result = false;
    ObjectIdentity oid = new ObjectIdentityImpl(element);
    MutableAcl acl;
    try {
      acl = (MutableAcl) mutableAclService.readAclById(oid);
    } catch (NotFoundException nfe) {
      acl = mutableAclService.createAcl(oid);
    }

    List<AccessControlEntry> entries = acl.getEntries();
    for (AccessControlEntry ace : entries) {
      if (ace.getSid().equals(recipient) && ace.getPermission().equals(permission)) {
        // result = true;
        return true;
      }
    }
    return result;
  }

  @Transactional(readOnly = false)
  public void deletePermission(AbstractEntity element) {
    // Delete the ACL information as well
    // ObjectIdentity oid = new ObjectIdentityImpl(AbstractEntity.class,
    // element.getId());
    ObjectIdentity oid = new ObjectIdentityImpl(element);
    mutableAclService.deleteAcl(oid, false);
  }

  @Transactional(readOnly = false)
  public boolean deleteAccessControlEntry(AbstractEntity element, Sid recipient,
      Permission permission) {
    ObjectIdentity oid = new ObjectIdentityImpl(element);
    MutableAcl acl;
    try {
      acl = (MutableAcl) mutableAclService.readAclById(oid);
    } catch (NotFoundException nfe) {
      acl = mutableAclService.createAcl(oid);
    }

    List<AccessControlEntry> entries = acl.getEntries();
    for (int idx = 0; idx < entries.size(); idx++) {
      if (entries.get(idx).getSid().equals(recipient)
          && entries.get(idx).getPermission().equals(permission)) {
        acl.deleteAce(idx);
      }
    }

    mutableAclService.updateAcl(acl);

    if (log.isDebugEnabled()) {
      log.debug("Deleted Permission:" + permission + " for recipient: " + recipient
          + ", for object: " + element);
    }

    return true;
  }

  public MutableAclService getMutableAclService() {
    return mutableAclService;
  }

  public void setMutableAclService(MutableAclService mutableAclService) {
    this.mutableAclService = mutableAclService;
  }
}