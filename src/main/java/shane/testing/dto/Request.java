package shane.testing.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;


public interface Request {
    Logger LOGGER = LoggerFactory.getLogger(Request.class);

    default Map<String, Object> getDefaultRequestParams() {
        return RequestHelper.getDefaultRequestParams(this);
    }

    default List<Field> getAllFields() {
        return RequestHelper.getAllFields(this);
    }

    default List<Field> getAllFieldsHaveValue() throws IllegalArgumentException, IllegalAccessException {
        return RequestHelper.getAllFieldsHaveValue(this);
    }

    default String convertDTOObjectToJSONString() {
        return RequestHelper.convertDTOObjectToJSONString(this);
    }

}
