package nextstep.cucumber;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;
import io.cucumber.spring.CucumberContextConfiguration;
import nextstep.utils.context.AcceptanceTest;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "nextstep")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty")
@CucumberContextConfiguration
@AcceptanceTest
public class CucumberTest {

}
