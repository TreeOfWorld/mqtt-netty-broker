package cn.treeofworld.elf.netty;

import cn.treeofworld.elf.netty.handler.*;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.mqtt.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@ChannelHandler.Sharable
public class MqttBrokerChannelHandler extends ChannelInboundHandlerAdapter {

    final ConnectHandler connectHandler;
    final PublishHandler publishHandler;
    final SubscribeHandler subscribeHandler;
    final UnsubscribeHandler unsubscribeHandler;
    final PingReqHandler pingReqHandler;
    final DisconnectHandler disconnectHandler;

    public MqttBrokerChannelHandler(ConnectHandler connectHandler,
                                    PublishHandler publishHandler, SubscribeHandler subscribeHandler, UnsubscribeHandler unsubscribeHandler,
                                    PingReqHandler pingReqHandler, DisconnectHandler disconnectHandler) {

        this.connectHandler = connectHandler;
        this.publishHandler = publishHandler;
        this.subscribeHandler = subscribeHandler;
        this.unsubscribeHandler = unsubscribeHandler;
        this.pingReqHandler = pingReqHandler;
        this.disconnectHandler = disconnectHandler;
    }

    /**
     * 标注各个入参的类型
     * <p>
     * <li>ctx {@link io.netty.channel.DefaultChannelHandlerContext}</li>
     * <li>ctx.channel() {@link io.netty.channel.socket.nio.NioSocketChannel}</li>
     * <li>msg {@link io.netty.handler.codec.mqtt.MqttMessage}</li>
     *
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        MqttMessage mqttMessage = (MqttMessage) msg;
        log.info("{}", msg.getClass().getName());
        log.info("--------------------------begin---------------------------*");
        log.info("来自终端:" + ctx.channel().remoteAddress());
        log.info("接收消息：" + mqttMessage.toString());

        // 收到的channelHandlerContext中包含终端的channel信息，这里可以根据channel信息做一些业务处理
        log.info("channel id:{}", ctx.channel().id().asLongText());
        // 收到的msg中包含固定头、可变头、有效载荷三个部分，这里只处理有效载荷部分

        try {
            // todo 这里可以优化，其实可以直接根据msg的类型来判断应该使用哪个handler处理（应该是netty已经做过一次判断了）
            MqttMessageType type = mqttMessage.fixedHeader().messageType();
            switch (type) {
                case CONNECT -> connectHandler.handle(ctx.channel(), mqttMessage);
                case PUBLISH -> publishHandler.handle(ctx.channel(), mqttMessage);
                case SUBSCRIBE -> subscribeHandler.handle(ctx.channel(), mqttMessage);
                case UNSUBSCRIBE -> unsubscribeHandler.handle(ctx.channel(), mqttMessage);
                case PINGREQ -> pingReqHandler.handle(ctx.channel(), mqttMessage);
                case DISCONNECT -> disconnectHandler.handle(ctx.channel(), mqttMessage);
                default -> throw new Exception("未知的消息类型");
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
