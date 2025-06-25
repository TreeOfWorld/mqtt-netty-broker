package cn.treeofworld.elf.netty.handler;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * DisconnectHandler
 *
 * @author: elf
 * @data: 2025/6/26
 * @version: 1.0
 */
@Slf4j
@Component
public class DisconnectHandler implements MqttHandler {
    @Override
    public void handle(Channel channel, MqttMessage mqttMessage) {
        // todo 处理断开连接
        log.info("删除channel");
    }
}
