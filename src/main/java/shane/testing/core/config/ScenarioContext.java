package shane.testing.core.config;

import com.google.gson.reflect.TypeToken;
import shane.testing.util.ObjectMapperUtils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ScenarioContext {

    private static final ThreadLocal<Map<String, Object>> sharedData = ThreadLocal.withInitial(HashMap::new);
    private static final String GET_ERROR_MESSAGE = "Can't get data value with key %s. Current shared data %s";

    private ScenarioContext() {
    }

    public static ScenarioContext getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public void setData(String key, Object value) {
        sharedData.get().put(key, value);
    }

    public void copyData(Map<String, Object> srcData) {
        sharedData.get().putAll(srcData);
    }

    public Boolean isContains(String key) {
        return sharedData.get().containsKey(key);
    }

    public Map<String, Object> getAllData() {
        String jsonString = ObjectMapperUtils.getGsonInstance().toJson(sharedData.get());
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        return ObjectMapperUtils.getGsonInstance().fromJson(jsonString, type);
    }

    @SuppressWarnings("unchecked")
    public <T> T getData(String key) {
        if (sharedData.get().containsKey(key))
            return (T) sharedData.get().get(key);
        String exceptionMsg = String.format(GET_ERROR_MESSAGE, key, getInstance());
        throw new IllegalAccessError(exceptionMsg);
    }

    @SuppressWarnings("unchecked")
    public <T> T getData(String key, boolean allowNullData) {
        if (sharedData.get().containsKey(key))
            return (T) sharedData.get().get(key);
        if (allowNullData)
            return null;
        String exceptionMsg = String.format(GET_ERROR_MESSAGE, key, getInstance());
        throw new IllegalAccessError(exceptionMsg);
    }

    @Override
    public String toString() {
        return "TestContext [sharedData=" + sharedData.get() + "]";
    }

    public void setContext(String key, Object value) {
        getInstance().setData(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getContext(String key, boolean allowNullData) {
        Boolean contained = getInstance().isContains(key);
        if (Boolean.TRUE.equals(contained))
            return getInstance().getData(key);
        if (allowNullData)
            return null;
        String exceptionMsg = String.format(GET_ERROR_MESSAGE, key, getInstance().getAllData().toString());
        throw new IllegalAccessError(exceptionMsg);
    }

    public <T> T getContext(String key) {
        return getInstance().getData(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T getContext(String key, Object defaultValue) {
        try {
            return getInstance().getData(key);
        } catch (IllegalAccessError e) {
            return (T) defaultValue;
        }
    }

    public void clearAllDataInThreadContext() {
        sharedData.remove();
    }

    private static class InstanceHolder {
        private static final ScenarioContext INSTANCE = new ScenarioContext();
    }
}
