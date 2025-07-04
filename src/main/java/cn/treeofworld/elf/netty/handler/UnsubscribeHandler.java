package cn.treeofworld.elf.netty.handler;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * UnsubscribeHandler
 *
 * @author: elf
 * @data: 2025/6/26
 * @version: 1.0
 */
@Slf4j
@Component
public class UnsubscribeHandler implements MqttHandler {
    @Override
    public void handle(Channel channel, MqttMessage mqttMessage) {
        log.info("UnSubscribeAckMessageStrategy");

        /*---------------------------解析接收的消息---------------------------*/
        MqttMessageIdVariableHeader messageIdVariableHeader = (MqttMessageIdVariableHeader) mqttMessage.variableHeader();

        /*---------------------------构建返回的消息---------------------------*/
        MqttMessageIdVariableHeader variableHeaderBack = MqttMessageIdVariableHeader.from(messageIdVariableHeader.messageId());
        MqttFixedHeader mqttFixedHeaderBack = new MqttFixedHeader(MqttMessageType.UNSUBACK, false, MqttQoS.AT_MOST_ONCE, false, 2);
        MqttUnsubAckMessage unSubAckMsg = new MqttUnsubAckMessage(mqttFixedHeaderBack, variableHeaderBack);
        log.info("返回消息:" + unSubAckMsg.toString());

        channel.writeAndFlush(unSubAckMsg);
    }
}
