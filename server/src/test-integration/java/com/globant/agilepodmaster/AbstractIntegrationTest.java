package com.globant.agilepodmaster;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.globant.agilepodmaster.AgilePodMaster.ApplicationConfiguration;
import com.globant.agilepodmaster.AgilePodMaster.WebConfiguration;

/**
 * Base class for all integration tests.
 * 
 * @author Andres Postiglioni.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
@SpringApplicationConfiguration(classes = { 
    ApplicationConfiguration.class, WebConfiguration.class })
public abstract class AbstractIntegrationTest {
  @Value("${local.server.port}")
  private int serverPort;

  private String protocol = "https";

  private String host = "localhost";

  public String buildServerUrl(String path) {
    return String.format("%s://%s:%d%s", protocol, host, serverPort, path);
  }
}
