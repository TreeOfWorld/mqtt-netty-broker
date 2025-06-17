package cn.treeofworld.elf.netty.strategy;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.extern.slf4j.Slf4j;

/**
 * 处理ping消息
 * <p>
 * 设备发送ping消息到broker，broker返回pong消息
 * <p>
 * 这个消息是为了让client与broker包活的
 */
@Slf4j
public class PingMessageStrategy implements MessageStrategy {

    @Override
    public void sendResponseMessage(Channel channel, MqttMessage mqttMessage) {
        log.info("Received ping message, send pong message");

        MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.PINGRESP, false, MqttQoS.AT_MOST_ONCE, false, 0);
        MqttMessage pingBackMsg = new MqttMessage(fixedHeader);
        log.info("返回消息:" + pingBackMsg.toString());

        channel.writeAndFlush(pingBackMsg);
    }
}
