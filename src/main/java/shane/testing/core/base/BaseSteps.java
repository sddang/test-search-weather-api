package shane.testing.core.base;

import shane.testing.core.config.ScenarioContext;


public class BaseSteps extends ServiceInjection {

    /**
     * Contains shared value between context It will access through the TestContext
     * to ensure thread-safe
     */
    protected final ScenarioContext scenarioContext;
    protected String currentTime;

    public BaseSteps() {
        super();
        this.scenarioContext = ScenarioContext.getInstance();
    }
}