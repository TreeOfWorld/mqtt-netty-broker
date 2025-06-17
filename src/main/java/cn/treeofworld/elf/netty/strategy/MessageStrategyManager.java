package cn.treeofworld.elf.netty.strategy;

import io.netty.handler.codec.mqtt.MqttMessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageStrategyManager {

    //根据消息类型获取对应的策略类
    public MessageStrategy getMessageStrategy(MqttMessageType messageType) {
        return switch (messageType) {
            case CONNECT -> new ConnectAckMessageStrategy();
            case PUBLISH -> new PublishAckMessageStrategy();
            case PUBREL -> new PublishCompleteMessageStrategy();
            case SUBSCRIBE -> new SubscribeAckMessageStrategy();
            case UNSUBSCRIBE -> new UnSubscribeAckMessageStrategy();
            case PINGREQ -> new PingMessageStrategy();
            default -> nonImplemented(messageType);
        };
    }

    private MessageStrategy nonImplemented(MqttMessageType messageType) {
        log.warn("No strategy found for message type: {}", messageType.name());
        return null;
    }
}
