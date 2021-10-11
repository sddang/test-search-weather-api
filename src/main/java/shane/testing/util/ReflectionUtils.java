package shane.testing.util;

import com.google.gson.annotations.SerializedName;
import lombok.SneakyThrows;
import shane.testing.dto.Request;

import java.lang.reflect.Field;
import java.util.*;

public class ReflectionUtils {
    private ReflectionUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static List<Class<?>> getAllClasses(Object clazz) {
        List<Class<?>> classList = new ArrayList<>();
        Class<?> currentClazz = clazz.getClass();
        Class<?> superclass = currentClazz.getSuperclass();
        classList.add(currentClazz);
        if (isObjectClass(superclass))
            return classList;
        classList.add(superclass);
        addParentClass(superclass, classList);
        return classList;
    }

    public static List<Field> getAllFields(List<Class<?>> classes) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> classType : classes) {
            Field[] allFields = classType.getDeclaredFields();
            for (Field field : allFields) {
                SerializedName serializedName = field.getAnnotation(SerializedName.class);
                if (serializedName != null) {
                    field.setAccessible(true);
                    fields.add(field);
                    fields.addAll(Arrays.asList(allFields));
                }
            }
        }
        return fields;
    }

    private static void addParentClass(Class<?> superclass, List<Class<?>> classList) {
        while (Boolean.FALSE.equals(isObjectClass(superclass))) {
            Class<?> currentClazz = superclass;
            superclass = currentClazz.getSuperclass();
            if (isObjectClass(superclass))
                break;
            classList.add(superclass);
        }
    }

    private static boolean isObjectClass(Class<?> classType) {
        return classType.getSimpleName().equalsIgnoreCase("Object");
    }

    @SneakyThrows
    public static Map<String, Object> serializeObjectToMap(Object instance) {
        Map<String, Object> fieldMap = new HashMap<>();
        List<Field> listFields = findAllSerializeFieldInClass(instance);
        Set<Field> setFields = new HashSet<>(listFields);
        for (Field field : setFields) {
            field.setAccessible(true);
            Object fieldValue = field.get(instance);
            if (fieldValue == null)
                continue;
            convertObjectFieldToJsonMap(fieldValue, field, fieldMap);
        }
        return fieldMap;
    }

    @SneakyThrows
    private static void convertObjectFieldToJsonMap(Object fieldValue, Field field, Map<String, Object> fieldMap) {
        List<Object> fieldList = new ArrayList<>();
        SerializedName serializedName = field.getAnnotation(SerializedName.class);
        // Tricky logic to ignore filed for serializing ignore field
        if (serializedName == null) return;
        if (fieldValue instanceof List) {
            addListObjectToJsonMap(fieldValue, serializedName, fieldList, fieldMap);
        } else if (Request.class.isAssignableFrom(fieldValue.getClass()) && !isPrimitiveObject(fieldValue)) {
            fieldMap.put(serializedName.value(), serializeObjectToMap(fieldValue));
        } else {
            fieldMap.put(serializedName.value(), fieldValue);
        }
    }

    private static void addListObjectToJsonMap(Object value, SerializedName serializedName, List<Object> fieldList, Map<String, Object> fieldMap) {
        List<Object> values = (List<Object>) value;
        for (Object v : values) {
            if (isPrimitiveObject(v)) {
                fieldList.add(v);
            } else {
                fieldList.add(serializeObjectToMap(v));
            }
        }
        fieldMap.put(serializedName.value(), fieldList);
    }

    private static List<Field> findAllSerializeFieldInClass(Object fieldValue) {
        List<Class<?>> classes = getAllClasses(fieldValue);
        return getAllFields(classes);
    }

    private static boolean isPrimitiveObject(Object value) {
        Class<?> clazz = value.getClass();
        return clazz.equals(Boolean.class) ||
                clazz.equals(Integer.class) ||
                clazz.equals(Character.class) ||
                clazz.equals(Byte.class) ||
                clazz.equals(Short.class) ||
                clazz.equals(Double.class) ||
                clazz.equals(Long.class) ||
                clazz.equals(Float.class) ||
                clazz.equals(String.class);
    }
}
