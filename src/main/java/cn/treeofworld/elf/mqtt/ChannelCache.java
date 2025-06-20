package cn.treeofworld.elf.mqtt;

import java.util.HashMap;
import java.util.Map;

import io.netty.channel.Channel;

/**
 * LocalCache
 * <p>
 * 用于存储本地保存的信息
 *
 * <ul>
 *     <li>channel信息</li>
 * </ul>
 *
 * @author: elf
 * @data: 2025/6/20
 * @version: 1.0
 */
public class ChannelCache {

    /**
     * key: clientId, value: channel
     */
    private static final Map<String, Channel> channelMap = new HashMap<>();

    public static void put(String clientId, Channel channel) {
        channelMap.put(clientId, channel);
    }

    public static void remove(String clientId) {
        channelMap.remove(clientId);
    }

    public static Channel get(String clientId) {
        return channelMap.get(clientId);
    }

}
