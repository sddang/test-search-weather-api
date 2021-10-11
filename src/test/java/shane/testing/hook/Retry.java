package shane.testing.hook;

import com.qaprosoft.carina.core.foundation.utils.R;
import io.cucumber.testng.PickleWrapper;
import org.apache.commons.lang.StringUtils;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.util.*;
import java.util.stream.Collectors;

public class Retry implements IRetryAnalyzer {
    private static final int MAX_RETRY = R.CONFIG.getInt("retry_count");
    private static final Map<String, Integer> retryCount = new HashMap<>();

    /**
     * We need add synchronized for waiting to sync retryCount when running test in parallel
     */
    @Override
    public synchronized boolean retry(ITestResult result) {
        String keyName = getScenarioName(result);
        int currentRetryCount = retryCount.getOrDefault(keyName, 0);
        retryCount.put(keyName, ++currentRetryCount);
        return currentRetryCount <= MAX_RETRY;
    }

    private String getScenarioName(ITestResult result) {
        List<String> scenarioInputDataToHash = Arrays.stream(result.getParameters()).map(Object::toString).collect(Collectors.toList());
        scenarioInputDataToHash.add(Integer.toString(Thread.currentThread().getName().hashCode()));
        scenarioInputDataToHash.add(Integer.toString(getScenarioHashCode(result)));
        // use bitwise AND 0xfffffff to convert all negative number to positive number after hashing with hashCode()
        int scenarioNameHashNumberWithThreadName = StringUtils.join(scenarioInputDataToHash, "-").hashCode() & 0xfffffff;
        return Integer.toString(scenarioNameHashNumberWithThreadName);
    }

    private int getScenarioHashCode(ITestResult result) {
        int scenarioHashId = 0;
        Optional<Object> parameter = Arrays.stream(result.getParameters()).filter(p -> PickleWrapper.class.isAssignableFrom(p.getClass())).findFirst();
        if (parameter.isPresent()) {
            PickleWrapper pickleWrapper = (PickleWrapper) parameter.get();
            String hashDataInput = String.format("%s-%d-%d", pickleWrapper.getPickle().getName(), pickleWrapper.getPickle().getLine(), pickleWrapper.getPickle().getScenarioLine());
            scenarioHashId = hashDataInput.hashCode();
        }
        return scenarioHashId;
    }
}
