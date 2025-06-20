package cn.treeofworld.elf.mqtt;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * SubscribeCache
 * <p>
 * 维护客户端的订阅信息
 *
 * @author: elf
 * @data: 2025/6/20
 * @version: 1.0
 */
@Slf4j
public class SubscribeCache {

    private static final Map<String, Set<String>> cacheMap = new HashMap<>();

    public static void put(String topic, String channelId) {
        if (cacheMap.containsKey(topic)) {
            cacheMap.get(topic).add(channelId);
        } else {
            Set<String> channelIds = new HashSet<>();
            channelIds.add(channelId);
            cacheMap.put(topic, channelIds);
        }

        log.info("SubscribeCache put topic:{}, channelId:{}", topic, channelId);
    }

    public static Set<String> get(String topic) {
        return cacheMap.get(topic);
    }

    public static void remove(String topic) {
        cacheMap.remove(topic);
    }

}
