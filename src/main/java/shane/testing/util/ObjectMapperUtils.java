package shane.testing.util;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shane.testing.config.services.EnvironmentConstant;
import shane.testing.core.exception.TestContextException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class ObjectMapperUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectMapperUtils.class);

    private ObjectMapperUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Gson getGsonInstance() {
        return new GsonBuilder().setDateFormat(EnvironmentConstant.API_DATE_FORMAT).disableHtmlEscaping().create();
    }

    public static ObjectMapper getMapperInstance() {
        return new ObjectMapper();
    }

    public static <T> T convertResponseToDTOObject(String jsonString, Class<T> clazz) {
        try {
            ObjectMapper mapper = getMapperInstance();
            mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            return mapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            LOGGER.error("Error in mapping to response [{}] class. {}", clazz.getSimpleName(), e.getMessage());
            throw new TestContextException(
                    String.format("Error in mapping to response [%s] class. %s", clazz.getSimpleName(), e.getMessage()),
                    e);
        }
    }


    public static <T> T createDTOObjectByDataTable(Class<T> dtoClass, Map<String, String> dataTable) {
        T dtoClassInstance;
        try {
            dtoClassInstance = dtoClass.getDeclaredConstructor().newInstance();
            List<Class<?>> classes = ReflectionUtils.getAllClasses(dtoClassInstance);
            List<Field> allFields = ReflectionUtils.getAllFields(classes);
            Map<String, Field> fields = allFields.stream()
                    .collect(Collectors.toMap(f -> f.getName().toLowerCase(), f -> f, (field1, field2) -> field1));
            for (Entry<String, String> param : dataTable.entrySet()) {
                String paramKey = param.getKey();
                String paramValue = param.getValue();
                if (fields.containsKey(paramKey.toLowerCase())) {
                    Field paramFiled = fields.get(paramKey.toLowerCase());
                    paramFiled.setAccessible(true);
                    paramFiled.set(dtoClassInstance, paramValue);
                }
            }
            return dtoClassInstance;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            LOGGER.error(String.format("Error in mapping DTO class [%s] to request params. %s",
                    dtoClass.getClass().getSimpleName(), e.getMessage()));
            throw new TestContextException(String.format("Error in mapping DTO class [%s] to request params. %s",
                    dtoClass.getClass().getSimpleName(), e.getMessage()), e);
        }
    }

    public static String dtoWriteValueAsString(Object object) throws JsonProcessingException {
        return MAPPER.writeValueAsString(object);
    }

}
