package shane.testing.core.config;

import com.google.inject.Module;
import com.google.inject.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shane.testing.core.exception.TestContextException;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class GuiceFactory {
    private static final ThreadLocal<GuiceFactory> instance = new ThreadLocal<>();
    private static final Map<String, Module> listInjectedModules = new HashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(GuiceFactory.class);
    private Injector inject;

    private GuiceFactory() {
        inject = Guice.createInjector();
    }

    public static GuiceFactory getInstance() {
        if (instance.get() == null)
            synchronized (GuiceFactory.class) {
                if (instance.get() == null) {
                    instance.set(new GuiceFactory());
                    instance.get().reInjectModules();
                }
            }
        return instance.get();
    }

    public void injectToClass(Object clazz) {
        Injector injectInstance = instance.get().inject;
        injectInstance.injectMembers(clazz);
        instance.get().inject = injectInstance;
    }

    public void createInject(Module module) {
        Injector injectInstance = instance.get().inject;
        try {
            injectModule(injectInstance, module);
        } catch (CreationException e) {
            LOGGER.warn("Got error [BINDING_ALREADY_SET] when injecting module {}", e.getErrorMessages());
        } catch (Exception e) {
            throw new TestContextException("Cannot inject module to base class", e);
        }
    }

    /**
     * Return null if object not instance in Container
     *
     * @param cls
     * @return
     */
    public <T> T getInstanceObjectInject(Class<T> cls, Class<? extends Annotation> annotation) {
        try {
            Injector injectInstance = instance.get().inject;
            return injectInstance.getInstance(Key.get(cls, annotation));
        } catch (ConfigurationException e) {
            return null;
        }
    }

    public <T> T createObjectInstance(Class<T> cls) {
        Injector injectInstance = this.inject;
        return injectInstance.getInstance(cls);
    }

    private synchronized void injectModule(Injector injectInstance, Module module) {
        injectInstance = injectInstance.createChildInjector(module);
        instance.get().inject = injectInstance;
        storeInjectModule(module);
    }

    private void storeInjectModule(Module module) {
        String moduleName = module.getClass().getSimpleName();
        boolean isAlreadyStored = listInjectedModules.containsKey(moduleName);
        if (!isAlreadyStored)
            listInjectedModules.put(module.getClass().getSimpleName(), module);
    }

    private void reInjectModules() {
        listInjectedModules.forEach((k, v) -> {
            Injector reInject = instance.get().inject;
            reInject = reInject.createChildInjector(v);
            instance.get().inject = reInject;
        });
    }

    public void cleanGuiceInstance() {
        instance.remove();
    }
}
