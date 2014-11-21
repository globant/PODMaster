package com.globant.agilepodmaster.sync;

import java.util.Arrays;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.globant.agilepodmaster.core.Organization;
import com.globant.agilepodmaster.core.OrganizationRepository;
import com.globant.agilepodmaster.core.Product;
import com.globant.agilepodmaster.core.ProductRepository;
import com.globant.agilepodmaster.core.Project;
import com.globant.agilepodmaster.core.ProjectRepository;
import com.globant.agilepodmaster.core.Snapshot;
import com.globant.agilepodmaster.core.SnapshotRepository;
import com.globant.agilepodmaster.sync.reading.db.PodsReader;
import com.globant.agilepodmaster.sync.reading.jira.JiraAPIFactory;
import com.globant.agilepodmaster.sync.reading.jira.JiraRestClient;
import com.globant.agilepodmaster.sync.reading.jira.ReleasesReader;

@Slf4j
@RestController
public class SyncController implements
    ResourceProcessor<RepositoryLinksResource> {

  @Autowired
  ReleasesReader releasesReader;

  @Autowired
  RestTemplate restTemplate;

  @Autowired
  PodsReader podsReader;

  @Autowired
  OrganizationRepository organizationRepository;

  @Autowired
  ProductRepository productRepository;

  @Autowired
  ProjectRepository projectRepository;
  
  @Autowired
  SnapshotRepository snapshotRepository;

  @Autowired
  private Environment env;

  @Value("${gopods.jira.username}")
  private String username;

  @Value("${gopods.jira.password}")
  private String password;

  @Value("${gopods.jira.urlroot:https://jira.corp.globant.com/}")
  private String urlRoot;
  
  @Override
  public RepositoryLinksResource process(RepositoryLinksResource resource) {
      resource.add(ControllerLinkBuilder.linkTo( SyncController.class).withRel("sync"));

      return resource;
  }

  @RequestMapping(value = "/sync", method = RequestMethod.POST)
  public SyncContext sync(
      @RequestParam(value = "organization", defaultValue = "EA") String organizationName) {

    List<Organization> organizations = organizationRepository
        .findByName(organizationName);
    if (CollectionUtils.isEmpty(organizations)) {
      throw new PropertyNotFoundException("No organization found with name: "
          + organizationName);
    }
    Organization organization = organizations.get(0);

    List<Product> products = productRepository.findByOrganization(organization);
    if (CollectionUtils.isEmpty(products)) {
      throw new PropertyNotFoundException(
          "No products found for organization: " + organizationName);
    }
    Product product = products.get(0);

    List<Project> projects = projectRepository.findByProduct(product);
    if (CollectionUtils.isEmpty(projects)) {
      throw new PropertyNotFoundException(
          "No projects found for organization: " + organizationName);
    }
    
    Project project = projects.get(0);
    
    // TODO We are considering a JIRA for all the projects.
    JiraRestClient jiraRestClient = new JiraAPIFactory()
        .withCredentials(username, password).withTemplate(restTemplate)
        .withUrlRoot(urlRoot).create();
    
    String jiraProjectName = env.getProperty("gopods.jira."
        + project.getName().toLowerCase() + ".project_name");
    if (StringUtils.isEmpty(jiraProjectName)) {
      throw new PropertyNotFoundException("This property was not found: "
          + jiraProjectName);
    }
    
    String jiraRapidViewId = env.getProperty("gopods.jira."
        + project.getName().toLowerCase() + ".rapid_view_id");
    if (StringUtils.isEmpty(jiraRapidViewId)) {
      throw new PropertyNotFoundException("This property was not found: "
          + jiraRapidViewId);

    }
    
    ReleaseReaderConfiguration.Project projectConfig = new ReleaseReaderConfiguration.Project(
        project.getName(), jiraProjectName,  jiraRapidViewId, jiraRestClient);
    ReleaseReaderConfiguration.Product productConfig = new ReleaseReaderConfiguration.Product(
        product.getName(), Arrays.asList(projectConfig));
    ReleaseReaderConfiguration configuration = new ReleaseReaderConfiguration(
        organization.getName(), Arrays.asList(productConfig));

    releasesReader.setConfiguration(configuration);
    
    SyncContext context = new SyncContext();
    
    SnapshotBuilder snapshotBuilder = new SnapshotBuilder(context);
    podsReader.readInto(snapshotBuilder, context);
    releasesReader.readInto(snapshotBuilder, context);
    Snapshot snapshot = snapshotBuilder.build();
    
    snapshotRepository.save(snapshot);
    return context;

  }

  @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Entity no Found")
  public class EntiryNotFoundException extends RuntimeException {
    public EntiryNotFoundException(String msg) {
      super(msg);
      log.error(msg);
    }
  }

  @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Application Property Not Found")
  public class PropertyNotFoundException extends RuntimeException {
    public PropertyNotFoundException(String msg) {
      super(msg);
      log.error(msg);
    }
  }

  @ResponseStatus(value = HttpStatus.METHOD_FAILURE, reason = "Data Source conexion issue")
  @ExceptionHandler(ConexionRestException.class)
  public void conflict() {
  }

}
