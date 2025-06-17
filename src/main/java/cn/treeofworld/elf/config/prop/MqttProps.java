package cn.treeofworld.elf.config.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "mqtt")
public class MqttProps {

    private String serverIp;
    private int serverPort;

}
