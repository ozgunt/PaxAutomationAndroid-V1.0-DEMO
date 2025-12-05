package stepdefinitions;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")       // src/test/resources/features altını tara
@ConfigurationParameter(
        key = GLUE_PROPERTY_NAME,
        value = "stepdefinitions,hooks"    // step + hook paketlerin
)
@ConfigurationParameter(
        key = PLUGIN_PROPERTY_NAME,
        value = "pretty"
)
public class CucumberTest {
    // Boş. Tüm işi anotasyonlar yapıyor.
}
