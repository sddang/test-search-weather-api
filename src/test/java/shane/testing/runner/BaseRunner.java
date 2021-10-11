package shane.testing.runner;

import com.qaprosoft.carina.core.foundation.cucumber.CucumberBaseTest;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import shane.testing.core.config.GuiceFactory;

public class BaseRunner extends CucumberBaseTest {

    @AfterTest
    public void notifySuiteResult(ITestContext ctx) {
        GuiceFactory.getInstance().injectToClass(this);
    }
}
