package cn.treeofworld.elf.netty.handler;

import cn.treeofworld.elf.mqtt.SubscribeCache;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * SubscribeHandler
 *
 * @author: elf
 * @data: 2025/6/26
 * @version: 1.0
 */
@Slf4j
@Component
public class SubscribeHandler implements MqttHandler {
    @Override
    public void handle(Channel channel, MqttMessage mqttMessage) {
        log.info("SubscribeAckMessageStrategy");

        MqttSubscribeMessage mqttSubscribeMessage = (MqttSubscribeMessage) mqttMessage;

        /*---------------------------解析接收的消息---------------------------*/
        MqttMessageIdVariableHeader messageIdVariableHeader = mqttSubscribeMessage.variableHeader();
        MqttMessageIdVariableHeader variableHeaderBack = MqttMessageIdVariableHeader.from(messageIdVariableHeader.messageId());
        Set<String> topics = mqttSubscribeMessage.payload().topicSubscriptions().stream().map(mqttTopicSubscription -> mqttTopicSubscription.topicName()).collect(Collectors.toSet());
        List<Integer> grantedQoSLevels = new ArrayList<>(topics.size());
        for (int i = 0; i < topics.size(); i++) {
            grantedQoSLevels.add(mqttSubscribeMessage.payload().topicSubscriptions().get(i).qualityOfService().value());
        }

        /*---------------------------构建返回的消息---------------------------*/
        //		有效负载
        MqttSubAckPayload payloadBack = new MqttSubAckPayload(grantedQoSLevels);
        MqttFixedHeader mqttFixedHeaderBack = new MqttFixedHeader(MqttMessageType.SUBACK, false, MqttQoS.AT_MOST_ONCE, false, 2 + topics.size());
        MqttSubAckMessage subAckMsg = new MqttSubAckMessage(mqttFixedHeaderBack, variableHeaderBack, payloadBack);
        log.info("返回消息:" + subAckMsg.toString());
        channel.writeAndFlush(subAckMsg);

        log.info("订阅信息：{}", mqttSubscribeMessage.payload().topicSubscriptions().get(0));
        SubscribeCache.put(mqttSubscribeMessage.payload().topicSubscriptions().get(0).topicName(), channel.id().asLongText());
    }
}
