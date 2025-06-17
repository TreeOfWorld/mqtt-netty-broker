package cn.treeofworld.elf.netty.strategy;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PublishAckMessageStrategy implements MessageStrategy {

    @Override
    public void sendResponseMessage(Channel channel, MqttMessage mqttMessage) {
        log.info("PublishAckMessageStrategy");
    }
}
