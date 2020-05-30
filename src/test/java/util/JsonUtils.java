package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonUtils() {
        throw new AssertionError("Util class must not be instantiated.");
    }

    public static <T> T fromString(final String value, final Class<T> clazz) {
        try {
            return MAPPER.readValue(value, clazz);
        } catch (JsonProcessingException e) {
            log.error("Could not read value from string %s.", value);
        }
        return null;
    }

}
