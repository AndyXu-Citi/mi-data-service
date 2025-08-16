package com.marathon.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JavaType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * JSON文件工具类
 * 用于读取JSON文件并转换为Java对象
 *
 * @author marathon
 */
@Slf4j
public class JsonFileUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 从类路径读取JSON文件并转换为指定类型的对象
     *
     * @param jsonFilePath JSON文件路径（类路径下）
     * @param clazz        目标类型
     * @param <T>          泛型类型
     * @return 转换后的对象
     */
    public static <T> T readJsonFromClassPath(String jsonFilePath, Class<T> clazz) {
        if (!StringUtils.hasText(jsonFilePath) || clazz == null) {
            log.error("参数不能为空：jsonFilePath={}, clazz={}", jsonFilePath, clazz);
            return null;
        }

        try {
            Resource resource = new ClassPathResource(jsonFilePath);
            if (!resource.exists()) {
                log.error("JSON文件不存在：{}", jsonFilePath);
                return null;
            }

            try (InputStream inputStream = resource.getInputStream()) {
                return objectMapper.readValue(inputStream, clazz);
            }
        } catch (IOException e) {
            log.error("读取JSON文件失败：{}，错误：{}", jsonFilePath, e.getMessage(), e);
            return null;
        }
    }

    /**
     * 从文件系统路径读取JSON文件并转换为指定类型的对象
     *
     * @param jsonFilePath JSON文件路径（文件系统路径）
     * @param clazz        目标类型
     * @param <T>          泛型类型
     * @return 转换后的对象
     */
    public static <T> T readJsonFromFilePath(String jsonFilePath, Class<T> clazz) {
        if (!StringUtils.hasText(jsonFilePath) || clazz == null) {
            log.error("参数不能为空：jsonFilePath={}, clazz={}", jsonFilePath, clazz);
            return null;
        }

        try {
            return objectMapper.readValue(new java.io.File(jsonFilePath), clazz);
        } catch (IOException e) {
            log.error("读取JSON文件失败：{}，错误：{}", jsonFilePath, e.getMessage(), e);
            return null;
        }
    }

    /**
     * 从类路径读取JSON文件并转换为List集合
     *
     * @param jsonFilePath JSON文件路径（类路径下）
     * @param clazz        集合中元素的类型
     * @param <T>          泛型类型
     * @return 转换后的List集合
     */
    public static <T> List<T> readJsonListFromClassPath(String jsonFilePath, Class<T> clazz) {
        if (!StringUtils.hasText(jsonFilePath) || clazz == null) {
            log.error("参数不能为空：jsonFilePath={}, clazz={}", jsonFilePath, clazz);
            return null;
        }

        try {
            Resource resource = new ClassPathResource(jsonFilePath);
            if (!resource.exists()) {
                log.error("JSON文件不存在：{}", jsonFilePath);
                return null;
            }

            try (InputStream inputStream = resource.getInputStream()) {
                JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
                return objectMapper.readValue(inputStream, javaType);
            }
        } catch (IOException e) {
            log.error("读取JSON文件失败：{}，错误：{}", jsonFilePath, e.getMessage(), e);
            return null;
        }
    }

    /**
     * 从文件系统路径读取JSON文件并转换为List集合
     *
     * @param jsonFilePath JSON文件路径（文件系统路径）
     * @param clazz        集合中元素的类型
     * @param <T>          泛型类型
     * @return 转换后的List集合
     */
    public static <T> List<T> readJsonListFromFilePath(String jsonFilePath, Class<T> clazz) {
        if (!StringUtils.hasText(jsonFilePath) || clazz == null) {
            log.error("参数不能为空：jsonFilePath={}, clazz={}", jsonFilePath, clazz);
            return null;
        }

        try {
            JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
            return objectMapper.readValue(new java.io.File(jsonFilePath), javaType);
        } catch (IOException e) {
            log.error("读取JSON文件失败：{}，错误：{}", jsonFilePath, e.getMessage(), e);
            return null;
        }
    }

    /**
     * 将对象转换为JSON字符串
     *
     * @param object 要转换的对象
     * @return JSON字符串
     */
    public static String toJsonString(Object object) {
        if (object == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            log.error("对象转JSON字符串失败：{}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 将对象写入JSON文件
     *
     * @param object       要写入的对象
     * @param jsonFilePath 目标文件路径
     * @return 是否成功
     */
    public static boolean writeJsonToFile(Object object, String jsonFilePath) {
        if (object == null || !StringUtils.hasText(jsonFilePath)) {
            log.error("参数不能为空：object={}, jsonFilePath={}", object, jsonFilePath);
            return false;
        }

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new java.io.File(jsonFilePath), object);
            return true;
        } catch (IOException e) {
            log.error("写入JSON文件失败：{}，错误：{}", jsonFilePath, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 获取ObjectMapper实例
     *
     * @return ObjectMapper实例
     */
    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}