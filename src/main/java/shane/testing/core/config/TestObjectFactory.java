package shane.testing.core.config;

import io.cucumber.core.backend.ObjectFactory;
import io.cucumber.guice.CucumberModules;

public class TestObjectFactory implements ObjectFactory {

    public TestObjectFactory() {
        GuiceFactory.getInstance().createInject(CucumberModules.createScenarioModule());
    }

    @Override
    public void start() {
        GuiceFactory.getInstance().cleanGuiceInstance();
    }

    @Override
    public void stop() {
        GuiceFactory.getInstance().cleanGuiceInstance();
    }

    @Override
    public boolean addClass(Class<?> glueClass) {
        return true;
    }

    @Override
    public <T> T getInstance(Class<T> glueClass) {
        T instance = GuiceFactory.getInstance().createObjectInstance(glueClass);
        GuiceFactory.getInstance().injectToClass(instance);
        return instance;
    }

    public GuiceFactory getGuiceInstance() {
        return GuiceFactory.getInstance();
    }
}
