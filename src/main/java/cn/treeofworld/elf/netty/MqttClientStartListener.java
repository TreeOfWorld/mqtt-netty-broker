package cn.treeofworld.elf.netty;

import jakarta.annotation.Resource;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class MqttClientStartListener implements ApplicationRunner {
    @Resource
    MqttBroker mqttBroker;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        mqttBroker.start();
    }
}
