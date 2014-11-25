package com.globant.agilepodmaster;

import lombok.Getter;

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
 * @author Andres Postiglioni.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {
    ApplicationConfiguration.class, WebConfiguration.class})
@WebAppConfiguration
@IntegrationTest("server.port:0")
public abstract class AbstractIntegrationTest {
  @Value("${local.server.port}")
  @Getter
  private int serverPort;
}
