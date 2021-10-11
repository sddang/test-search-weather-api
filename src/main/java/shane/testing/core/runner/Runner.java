package shane.testing.core.runner;

import com.qaprosoft.carina.core.foundation.utils.R;
import io.cucumber.testng.FeatureWrapper;
import io.cucumber.testng.PickleWrapper;
import io.cucumber.testng.TestNGCucumberRunner;
import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class Runner {
    protected static final Logger LOGGER = Logger.getLogger(Runner.class);
    private TestNGCucumberRunner testNGCucumberRunner;
    private boolean enableParallel;
    private List<String> parallelTags = new ArrayList<>();
    private final BiConsumer<Object[], List<Object[]>> filterWithoutParallelScenarios =
            (feature, scenariosWithFilter) -> {
                for (Object scenario : feature) {
                    if (isPickleWrapperImplObject(scenario)) {
                        PickleWrapper pickleWrapper = (PickleWrapper) scenario;
                        boolean notIncludeParallelTag = pickleWrapper.getPickle().getTags().stream().noneMatch(tag -> parallelTags.contains(tag));
                        if (notIncludeParallelTag) {
                            scenariosWithFilter.add(feature);
                        }
                    }
                }
            };
    private final BiConsumer<Object[], List<Object[]>> filterWithParallelScenarios =
            (feature, scenariosWithFilter) -> {
                for (Object scenario : feature) {
                    if (isPickleWrapperImplObject(scenario)) {
                        PickleWrapper pickleWrapper = (PickleWrapper) scenario;
                        boolean includeParallelTag = pickleWrapper.getPickle().getTags().stream().anyMatch(tag -> parallelTags.contains(tag));
                        if (includeParallelTag) {
                            scenariosWithFilter.add(feature);
                        }
                    }
                }
            };

    @BeforeClass(alwaysRun = true)
    @Parameters({"parallel"})
    public void setUpClass(boolean isParallel) {
        parallelTags = Arrays.stream(R.CONFIG.get("default.tags.parallel").split(",")).collect(Collectors.toList());
        enableParallel = isParallel;
        testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
    }

    @Test(groups = {"cucumber"},
            description = "Runs Cucumber Feature",
            dataProvider = "scenariosParallel",
            testName = "Feature Run Parallel",
            priority = 1)
    public void featureParallel(PickleWrapper pickleWrapper, FeatureWrapper featureWrapper) {
        testNGCucumberRunner.runScenario(pickleWrapper.getPickle());
    }

    @Test(groups = {"cucumber"},
            description = "Runs Cucumber Feature",
            dataProvider = "scenarios",
            testName = "Feature Run Non-Parallel",
            priority = 0)
    public void feature(PickleWrapper pickleWrapper, FeatureWrapper featureWrapper) {
        testNGCucumberRunner.runScenario(pickleWrapper.getPickle());
    }

    @DataProvider(parallel = true)
    public Object[][] scenariosParallel(ITestContext context) {
        if (enableParallel) {
            return filterParallelScenario(testNGCucumberRunner.provideScenarios());
        } else {
            return new Object[][]{};
        }
    }

    @DataProvider
    public Object[][] scenarios(ITestContext context) {
        if (enableParallel) {
            return new Object[][]{};
        } else {
            return filterNonParallelScenario(testNGCucumberRunner.provideScenarios());
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
        if (testNGCucumberRunner == null) {
            return;
        }
        testNGCucumberRunner.finish();
    }

    private Object[][] filterParallelScenario(Object[][] features) {
        List<Object[]> scenariosWithFilterParallel = new ArrayList<>();
        filterScenario(features, scenariosWithFilterParallel, filterWithParallelScenarios);
        return scenariosWithFilterParallel.toArray(new Object[0][0]);
    }

    private Object[][] filterNonParallelScenario(Object[][] features) {
        List<Object[]> scenariosWithFilterParallel = new ArrayList<>();
        filterScenario(features, scenariosWithFilterParallel, filterWithoutParallelScenarios);
        return scenariosWithFilterParallel.toArray(new Object[0][0]);
    }

    private boolean isPickleWrapperImplObject(Object clazz) {
        return PickleWrapper.class.isAssignableFrom(clazz.getClass());
    }

    private void filterScenario(Object[][] features, List<Object[]> scenariosWithFilter, BiConsumer<Object[], List<Object[]>> consumer) {
        for (Object[] feature : features) {
            consumer.accept(feature, scenariosWithFilter);
        }
    }
}
