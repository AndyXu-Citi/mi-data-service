package com.marathon.common.util;

import com.marathon.domain.entity.Event;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

/**
 * JsonFileUtil使用示例
 * 演示如何使用JsonFileUtil工具类读取JSON文件并转换为Java对象
 *
 * @author marathon
 */
@Slf4j
public class JsonFileUtilExample {

    public static void main(String[] args) {
        // 示例1：读取单个对象
        readSingleObjectExample();
        
        // 示例2：读取对象列表
        readObjectListExample();
        
        // 示例3：写入JSON文件示例
        writeObjectExample();
    }

    /**
     * 读取单个对象的示例
     */
    private static void readSingleObjectExample() {
        log.info("=== 读取单个对象示例 ===");
        
        // 假设有一个event.json文件在类路径下
        String jsonFilePath = "data/event.json";
        Event event = JsonFileUtil.readJsonFromClassPath(jsonFilePath, Event.class);
        
        if (event != null) {
            log.info("成功读取单个赛事对象：{} - {}", event.getEventId(), event.getEventName());
        } else {
            log.warn("未能读取到赛事对象，文件可能不存在或格式错误");
        }
    }

    /**
     * 读取对象列表的示例
     */
    private static void readObjectListExample() {
        log.info("=== 读取对象列表示例 ===");
        
        // 假设有一个events.json文件在类路径下，包含多个赛事对象
        String jsonFilePath = "data/events.json";
        List<Event> events = JsonFileUtil.readJsonListFromClassPath(jsonFilePath, Event.class);
        
        if (events != null && !events.isEmpty()) {
            log.info("成功读取赛事列表，共{}个赛事", events.size());
            events.forEach(event -> log.info("赛事：{} - {}", event.getEventId(), event.getEventName()));
        } else {
            log.warn("未能读取到赛事列表，文件可能不存在或格式错误");
        }
    }

    /**
     * 写入JSON文件的示例
     */
    private static void writeObjectExample() {
        log.info("=== 写入JSON文件示例 ===");
        
        // 创建一个示例对象
        Event event = new Event();
        event.setEventId(1L);
        event.setEventName("2024北京马拉松");
        event.setEventType(1);
        event.setEventStatus(1);
        
        // 写入到文件
        String outputPath = "target/example-event.json";
        boolean success = JsonFileUtil.writeJsonToFile(event, outputPath);
        
        if (success) {
            log.info("成功写入JSON文件到：{}", outputPath);
        } else {
            log.error("写入JSON文件失败");
        }
    }
}