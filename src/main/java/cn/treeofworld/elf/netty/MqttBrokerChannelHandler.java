package cn.treeofworld.elf.netty;

import cn.treeofworld.elf.netty.strategy.MessageStrategy;
import cn.treeofworld.elf.netty.strategy.MessageStrategyManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@ChannelHandler.Sharable
public class MqttBrokerChannelHandler extends ChannelInboundHandlerAdapter {

    final MessageStrategyManager messageStrategyManager;

    public MqttBrokerChannelHandler(MessageStrategyManager messageStrategyManager) {
        this.messageStrategyManager = messageStrategyManager;
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) {
        MqttMessage mqttMessage = (MqttMessage) msg;
        log.info("--------------------------begin---------------------------*");
        log.info("来自终端:" + channelHandlerContext.channel().remoteAddress());
        log.info("接收消息：" + mqttMessage.toString());
        try {
            MqttMessageType type = mqttMessage.fixedHeader().messageType();
            MessageStrategy messageStrategy = messageStrategyManager.getMessageStrategy(type);
            if (messageStrategy != null) {
                messageStrategy.sendResponseMessage(channelHandlerContext.channel(), mqttMessage);
            }
        } catch (Exception e) {
            log.error("处理消息失败", e);
        }
        log.info("--------------------------end---------------------------*");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 当出现异常就关闭连接
        log.error("异常:", cause);
        ctx.close();
    }

}
