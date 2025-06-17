package cn.treeofworld.elf.config;

import cn.treeofworld.elf.config.prop.MqttProps;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 用于注入Props配置类
 */
@Configuration
@EnableConfigurationProperties(MqttProps.class)
public class Config {
}
