package cn.treeofworld.elf.netty.handler;

import cn.treeofworld.elf.MqttUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * PingreqHandler
 *
 * @author: elf
 * @data: 2025/6/26
 * @version: 1.0
 */
@Slf4j
@Component
public class PingReqHandler implements MqttHandler {
    @Override
    public void handle(Channel channel, MqttMessage mqttMessage) {
        log.info("Received ping message, send pong message");

        MqttFixedHeader fixedHeader = new MqttUtil.MqttFixedHeaderBuilder()
                .messageType(MqttMessageType.PINGRESP)
                .isDup(false)
                .qosLevel(MqttQoS.AT_MOST_ONCE)
                .isRetain(false)
                .remainingLength(0)
                .build();

        MqttMessage pingBackMsg = new MqttMessage(fixedHeader);

        log.info("返回消息:" + pingBackMsg.toString());

        channel.writeAndFlush(pingBackMsg);
    }
}
