package com.globant.agilepodmaster;

import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.globant.agilepodmaster.AgilePodMaster.ApplicationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationConfiguration.class)
@WebAppConfiguration
@IntegrationTest({ "server.port=" + AbstractIntegrationTest.SERVER_PORT, "management.port=0" })
public abstract class AbstractIntegrationTest {
  public static final int SERVER_PORT = 9090;
}
