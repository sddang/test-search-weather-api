package shane.testing.runner;

import com.google.api.client.util.ArrayMap;
import com.qaprosoft.carina.core.foundation.utils.R;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import shane.testing.config.services.EnvironmentConstant;
import shane.testing.core.config.ScenarioContext;
import shane.testing.core.config.TestObjectFactory;
import shane.testing.core.exception.TestContextException;
import shane.testing.core.runner.Runner;
import shane.testing.util.TestUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class BaseParallelRunner extends Runner {
    private static final ExecutorService executorService = Executors.newFixedThreadPool(4);
    private static final Map<String, Object> cacheData = new ArrayMap<>();
    private static final TestObjectFactory testObjectFactory = new TestObjectFactory();
    protected static final int POOLING_RETRY_COUNT = R.CONFIG.getInt(EnvironmentConstant.POOLING_RETRY_SET_UP_CACHE_DATA_COUNT);
    protected static final int POOLING_TIMEOUT_MS = R.CONFIG.getInt(EnvironmentConstant.POOLING_TIMEOUT_MS);

    @BeforeSuite
    public void setUpData() throws TestContextException {
    }

    private void initCacheData(
            List<Callable<Map<String, Object>>> createLinkSOFTasks) {
        int maxRetry = POOLING_RETRY_COUNT;
        boolean isCompleted = false;
        String message = "";
        do {
            try {
                List<Future<Map<String, Object>>> results = executorService.invokeAll(createLinkSOFTasks);
                results.forEach(this::addScenarioDataToCache);
                isCompleted = true;
            } catch (TestContextException | InterruptedException e) {
                TestUtils.waitForSync(POOLING_TIMEOUT_MS);
                message = e.toString();
                maxRetry--;
            }
        } while (maxRetry > 0 && Boolean.FALSE.equals(isCompleted));
        if (Boolean.FALSE.equals(isCompleted))
            throw new TestContextException(
                    String.format("Cannot prepare test data %s",
                            message));
    }

    @BeforeMethod
    public void injectCacheToScenarioContext() {
        ScenarioContext scenarioContext = ScenarioContext.getInstance();
        cacheData.forEach(scenarioContext::setContext);
    }

    @AfterMethod
    public void cleanDataInScenarioContext() {
        ScenarioContext.getInstance().clearAllDataInThreadContext();
    }

    @AfterSuite
    public void cleanUpData() throws InterruptedException {
    }

    private void addScenarioDataToCache(Future<Map<String, Object>> scenarioData) {
        try {
            cacheData.putAll(scenarioData.get(120, TimeUnit.SECONDS));
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            LOGGER.error("Error in add cache data");
            throw new TestContextException("Error in prepare data", e);
        }
    }
}
