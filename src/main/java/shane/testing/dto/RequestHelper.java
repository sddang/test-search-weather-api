package shane.testing.dto;

import com.google.api.client.util.ArrayMap;
import com.google.gson.annotations.SerializedName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shane.testing.core.exception.TestContextException;
import shane.testing.util.ObjectMapperUtils;
import shane.testing.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

final class RequestHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestHelper.class);
    private static final String ERROR_MESSAGE_CONVERT_DTO_FORMAT = "Error in mapping DTO class [%s] class. %s";

    private RequestHelper() {
        throw new IllegalStateException("Utility class");
    }

    public static String convertDTOObjectToJSONString(Object instance) {
        try {
            Map<String, Object> jsonMap = ReflectionUtils.serializeObjectToMap(instance);
            return ObjectMapperUtils.dtoWriteValueAsString(jsonMap);
        } catch (Exception e) {
            LOGGER.error(String.format(ERROR_MESSAGE_CONVERT_DTO_FORMAT, instance.getClass().getSimpleName(),
                    e.getMessage()));
            throw new TestContextException(String.format(ERROR_MESSAGE_CONVERT_DTO_FORMAT,
                    instance.getClass().getSimpleName(), e.getMessage()), e);
        }
    }

    public static List<Field> getAllFieldsHaveValue(Request instance) throws IllegalArgumentException, IllegalAccessException {
        List<Class<?>> classes = ReflectionUtils.getAllClasses(instance);
        List<Field> allFields = ReflectionUtils.getAllFields(classes);
        List<Field> allFieldsWithAccessible = new ArrayList<>();
        for (Field field : allFields) {
            field.setAccessible(true);
            SerializedName serializedName = field.getAnnotation(SerializedName.class);
            if (field.get(instance) != null && serializedName != null)
                allFieldsWithAccessible.add(field);
        }
        return allFieldsWithAccessible;
    }

    public static List<Field> getAllFields(Request instance) {
        List<Class<?>> classes = ReflectionUtils.getAllClasses(instance);
        List<Field> allFields = ReflectionUtils.getAllFields(classes);
        List<Field> allFieldsWithAccessible = new ArrayList<>();
        for (Field field : allFields) {
            field.setAccessible(true);
            SerializedName serializedName = field.getAnnotation(SerializedName.class);
            if (serializedName != null)
                allFieldsWithAccessible.add(field);
        }
        return allFieldsWithAccessible;
    }

    public static Map<String, Object> getDefaultRequestParams(Request instance) {
        try {
            Map<String, Object> requestParams = new ArrayMap<>();
            for (Field field : getAllFieldsHaveValue(instance)) {
                SerializedName serializedName = field.getAnnotation(SerializedName.class);
                requestParams.put(serializedName.value(), field.get(instance));
            }
            return requestParams;
        } catch (IllegalArgumentException | IllegalAccessException e) {
            LOGGER.error(String.format(ERROR_MESSAGE_CONVERT_DTO_FORMAT,
                    instance.getClass().getSimpleName(), e.getMessage()));
            throw new TestContextException(String.format(ERROR_MESSAGE_CONVERT_DTO_FORMAT,
                    instance.getClass().getSimpleName(), e.getMessage()), e);
        }
    }
}
