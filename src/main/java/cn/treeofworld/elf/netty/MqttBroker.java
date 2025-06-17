package cn.treeofworld.elf.netty;

import cn.treeofworld.elf.config.prop.MqttProps;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class MqttBroker {

    final MqttBrokerChannelInitializer mqttBrokerChannelInitializer;
    final MqttProps mqttProps;

    public MqttBroker(MqttBrokerChannelInitializer mqttBrokerChannelInitializer, MqttProps mqttProps) {
        this.mqttBrokerChannelInitializer = mqttBrokerChannelInitializer;

        this.mqttProps = mqttProps;
    }

    // 多线程事件循环器:接收的连接
    private EventLoopGroup bossGroup;
    // 实际工作的线程组 多线程事件循环器:处理已经被接收的连接
    private EventLoopGroup workGroup;


    private ChannelFuture channelFuture;
    private volatile Channel channel;

    private final AtomicInteger nextMessageId = new AtomicInteger(1);


    public void start() {
        log.info("mqtt server start:" + mqttProps.getServerPort());

        bossGroup = new NioEventLoopGroup();
        workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class).childHandler(mqttBrokerChannelInitializer).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

            // 绑定端口，开始接收进来的连接
            channelFuture = serverBootstrap.bind(mqttProps.getServerPort()).sync();

        } catch (Exception e) {
            log.error("mqtt server start error:", e);
        }
    }

    public void stop() {
        // 关闭channel
        try {
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("mqtt server stop error:", e);
        }

        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
        bossGroup = null;
        workGroup = null;

    }


}
