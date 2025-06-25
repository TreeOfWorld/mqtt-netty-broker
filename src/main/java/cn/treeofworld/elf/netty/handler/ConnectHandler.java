package cn.treeofworld.elf.netty.handler;

import cn.treeofworld.elf.MqttUtil;
import cn.treeofworld.elf.mqtt.ChannelCache;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * ConnectHandler
 *
 * @author: elf
 * @data: 2025/6/25
 * @version: 1.0
 */
@Slf4j
@Component
public class ConnectHandler implements MqttHandler {

    @Override
    public void handle(Channel channel, MqttMessage mqttMessage) {
        log.info("ConnectAckMessageStrategy");

        /*---------------------------解析接收的消息----------------------------*/
        MqttConnectMessage mqttConnectMessage = (MqttConnectMessage) mqttMessage;
        MqttFixedHeader mqttFixedHeader = mqttConnectMessage.fixedHeader();
        MqttConnectVariableHeader mqttConnectVariableHeader = mqttConnectMessage.variableHeader();

        /*---------------------------构建返回的消息---------------------------*/
        // 构建返回报文
        // 固定报头
        MqttFixedHeader mqttFixedHeaderBack = new MqttUtil.MqttFixedHeaderBuilder()
                .messageType(MqttMessageType.CONNACK)
                .isDup(mqttFixedHeader.isDup())
                .qosLevel(MqttQoS.AT_MOST_ONCE)
                .isRetain(mqttFixedHeader.isRetain())
                .remainingLength(2)
                .build();
        // 可变报头
        MqttConnAckVariableHeader mqttConnAckVariableHeader = new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_ACCEPTED, mqttConnectVariableHeader.isCleanSession());

        // 构建CONNACK消息体
        MqttConnAckMessage connAck = new MqttConnAckMessage(mqttFixedHeaderBack, mqttConnAckVariableHeader);
        log.info("返回消息:" + connAck.toString());
        channel.writeAndFlush(connAck);

        ChannelCache.put(channel.id().asLongText(), channel);
    }
}
