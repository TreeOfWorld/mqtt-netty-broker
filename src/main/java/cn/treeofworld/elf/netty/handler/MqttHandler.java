package cn.treeofworld.elf.netty.handler;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessage;

/**
 * MqttHandler
 *
 * @author: elf
 * @data: 2025/6/25
 * @version: 1.0
 */
public interface MqttHandler {

    void handle(Channel channel, MqttMessage mqttMessage);

}
