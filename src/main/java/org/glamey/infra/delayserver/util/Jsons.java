package org.glamey.infra.delayserver.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Jsons {

    private static final Logger LOGGER = LoggerFactory.getLogger(Jsons.class);
    private static final ObjectMapper mapper;
    private static final String EMPTY_JSON = "{}";

    static {
        mapper = new ObjectMapper();
        //        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);       //属性为NULL 不序列化
        //        mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);    //属性为默认值不序列化
        //        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);      // 属性为 空（“”） 或者为 NULL 都不序列化
        mapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //    mapper.setDateFormat(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    public static <T> String toJson(T obj) {
        if (obj == null) {
            return EMPTY_JSON;
        }

        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            LOGGER.error("object to json error", e);
        }
        return null;
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        if (json == null || "".equals(json)) {
            return null;
        }

        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            LOGGER.error("json to object error", e);
        }
        return null;
    }
}
