package cn.treeofworld.elf.netty.strategy;

import cn.treeofworld.elf.mqtt.ChannelCache;
import cn.treeofworld.elf.mqtt.SubscribeCache;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PublishAckMessageStrategy implements MessageStrategy {

    @Override
    public void sendResponseMessage(Channel channel, MqttMessage mqttMessage) {
        MqttPublishMessage mqttPublishMessage = (MqttPublishMessage) mqttMessage;

        int packageId = mqttPublishMessage.variableHeader().packetId();
        log.info("PublishAckMessageStrategy packageId:{}", packageId);

        MqttFixedHeader mqttFixedHeader = mqttPublishMessage.fixedHeader();

        MqttMessageIdVariableHeader mqttMessageIdVariableHeader = MqttMessageIdVariableHeader.from(1);
        MqttFixedHeader mqttFixedHeaderBack = new MqttFixedHeader(MqttMessageType.PUBLISH, mqttFixedHeader.isDup(), MqttQoS.AT_MOST_ONCE, mqttFixedHeader.isRetain(), 0x02);

        MqttPubAckMessage mqttPubAckMessage = new MqttPubAckMessage(mqttFixedHeaderBack, mqttMessageIdVariableHeader);

        MqttPublishMessage publishMessage = new MqttPublishMessage(mqttFixedHeader, mqttPublishMessage.variableHeader(), mqttPublishMessage.payload());

        // 向每一个订阅了该主题的客户端发送发布消息
        String topic = mqttPublishMessage.variableHeader().topicName();
        SubscribeCache.get(topic).forEach(channelId -> {
            log.info("PublishAckMessageStrategy channelId:{}", channelId);
            ChannelCache.get(channelId).writeAndFlush(publishMessage.retain());
        });
        // 释放消息
        publishMessage.release();

        // 解析MqttQoS
        MqttQoS mqttQoS = mqttFixedHeader.qosLevel();

        switch (mqttQoS) {
            case AT_MOST_ONCE -> processAtMostOnceMsg(channel, mqttFixedHeader, packageId);
            case AT_LEAST_ONCE -> processAtLeastOnceMsg(channel, mqttFixedHeader, packageId);
            case EXACTLY_ONCE -> processExactlyOnceMsg(channel, mqttFixedHeader, packageId);
            default -> throw new IllegalArgumentException("unknown QoS");
        }
    }

    private void processAtMostOnceMsg(Channel channel, MqttFixedHeader mqttFixedHeader, int packageId) {
        return;
    }

    public void processAtLeastOnceMsg(Channel channel, MqttFixedHeader mqttFixedHeader, int packageId) {
        log.info("packageId:{}", packageId);
        MqttMessageIdVariableHeader mqttMessageIdVariableHeaderBack = MqttMessageIdVariableHeader.from(packageId);
        MqttFixedHeader mqttFixedHeaderBack = new MqttFixedHeader(MqttMessageType.PUBACK, mqttFixedHeader.isDup(), MqttQoS.AT_MOST_ONCE, mqttFixedHeader.isRetain(), 0x02);
        MqttPubAckMessage pubAckMsg = new MqttPubAckMessage(mqttFixedHeaderBack, mqttMessageIdVariableHeaderBack);
        log.info("返回消息:" + pubAckMsg.toString());

        channel.writeAndFlush(pubAckMsg);
    }

    public void processExactlyOnceMsg(Channel channel, MqttFixedHeader mqttFixedHeader, int packageId) {
        log.info("packageId:{}", packageId);
        MqttFixedHeader mqttFixedHeaderBack = new MqttFixedHeader(MqttMessageType.PUBREC, false, MqttQoS.AT_LEAST_ONCE, false, 0x02);
        MqttMessageIdVariableHeader mqttMessageIdVariableHeaderBack = MqttMessageIdVariableHeader.from(packageId);
        MqttMessage pubAckMsg = new MqttMessage(mqttFixedHeaderBack, mqttMessageIdVariableHeaderBack);
        log.info("返回消息:" + pubAckMsg.toString());

        channel.writeAndFlush(pubAckMsg);
    }
}
