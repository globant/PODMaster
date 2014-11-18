package com.globant.agilepodmaster;

import com.globant.agilepodmaster.AgilePodMaster.ApplicationConfiguration;

import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Base class for all the tests.
 * @author jose.dominguez@globant.com
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationConfiguration.class)
public abstract class AbstractUnitTest {

}
