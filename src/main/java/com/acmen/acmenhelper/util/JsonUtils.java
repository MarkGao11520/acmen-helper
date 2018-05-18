package com.acmen.acmenhelper.util;

import com.acmen.acmenhelper.exception.GlobalException;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author gaowenfeng
 * @date 2018/5/17
 */
public class JsonUtils {
    private final static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T fromJSON(String jsonString, Class<T> clazz) {

        T object = null;
        try {
            object = objectMapper.readValue(jsonString, clazz);
        } catch (Exception e) {
            throw new GlobalException(1 , "json转换异常" , e);
        }
        return object;
    }
}
