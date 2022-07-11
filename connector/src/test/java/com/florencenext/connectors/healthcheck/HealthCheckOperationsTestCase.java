package com.florencenext.connectors.healthcheck;

import com.florencenext.connectors.healthcheck.api.model.entities.Healtcheck;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.mule.functional.junit4.MuleArtifactFunctionalTestCase;
import org.mule.runtime.api.event.Event;
import org.mule.runtime.api.metadata.DataType;
import org.mule.runtime.api.transformation.TransformationService;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class HealthCheckOperationsTestCase extends MuleArtifactFunctionalTestCase {

    @Inject
    TransformationService transformationService;
  /**
   * Specifies the mule config xml with the flows that are going to be executed in the tests, this file lives in the test resources.
   */
  @Override
  protected String getConfigFile() {
    return "test-mule-config.xml";
  }
  
  @Test
  public void executeExternalHealthcheckNoExtOperation() throws Exception {

  }
}
