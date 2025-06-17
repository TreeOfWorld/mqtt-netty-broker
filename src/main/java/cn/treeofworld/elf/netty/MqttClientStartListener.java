package cn.treeofworld.elf.netty;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class MqttClientStartListener implements ApplicationRunner {

    final MqttBroker mqttBroker;

    public MqttClientStartListener(MqttBroker mqttBroker) {
        this.mqttBroker = mqttBroker;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        mqttBroker.start();
    }
}
