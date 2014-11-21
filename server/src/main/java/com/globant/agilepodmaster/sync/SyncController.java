package com.globant.agilepodmaster.sync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * Controller to call synchronization process.
 * @author jose.dominguez@globant.com
 *
 */
@Slf4j
@RestController
public class SyncController implements
    ResourceProcessor<RepositoryLinksResource> {

  private SyncCommand command;

  /**
   * Constructor.
   * @param command command to execute the synchronization.
   */
  @Autowired
  public SyncController(SyncCommand command) {
    this.command = command;
  }

  @Override
  public RepositoryLinksResource process(RepositoryLinksResource resource) {
    resource.add(ControllerLinkBuilder.linkTo(SyncController.class).withRel(
        "sync"));

    return resource;
  }

  /**
   * Synchronizes.
   * @param organizationName the name of the organization.
   * @return the context of the excution.
   */
  @RequestMapping(value = "/sync", method = RequestMethod.POST)
  public SyncContext sync(
      @RequestParam(value = "organization", defaultValue = "EA") String organizationName) {
    return command.execute(organizationName);

  }

  /**
   * Method to treat conexion issues.
   */
  @ResponseStatus(value = HttpStatus.METHOD_FAILURE, reason = "Data Source conexion issue")
  @ExceptionHandler(ConexionRestException.class)
  public void conflict() {
  }

}
