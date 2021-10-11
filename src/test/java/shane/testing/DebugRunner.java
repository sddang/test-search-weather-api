package shane.testing;

import io.cucumber.testng.CucumberOptions;
import shane.testing.core.config.TestObjectFactory;
import shane.testing.core.runner.Runner;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"shane.testing.hook",
                "shane.testing.steps"}, plugin = {"pretty", "html:target/cucumber-core-test-report.html",
        "pretty:target/cucumber-core-test-report.txt", "json:target/cucumber-core-test-report.json",
        "junit:target/cucumber-core-test-report.xml"},
        tags = "@SearchWeather",
        dryRun = false,
        monochrome = true,
        objectFactory = TestObjectFactory.class)
public class DebugRunner extends Runner {

}

