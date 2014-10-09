package com.globant.agilepodmaster;

import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.globant.agilepodmaster.AgilePodMaster.ApplicationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationConfiguration.class)
public abstract class AbstractUnitTest {

}
