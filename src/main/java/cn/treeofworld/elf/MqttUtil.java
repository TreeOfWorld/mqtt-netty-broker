package cn.treeofworld.elf;

import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.experimental.UtilityClass;

/**
 * MqttUtil
 *
 * @author: elf
 * @data: 2025/6/26
 * @version: 1.0
 */
@UtilityClass
public class MqttUtil {

    public class MqttFixedHeaderBuilder {
        private MqttMessageType messageType;
        private boolean isDup;
        private MqttQoS qosLevel;
        private boolean isRetain;
        private int remainingLength;

        public MqttFixedHeaderBuilder messageType(MqttMessageType messageType) {
            this.messageType = messageType;
            return this;
        }

        public MqttFixedHeaderBuilder isDup(boolean isDup) {
            this.isDup = isDup;
            return this;
        }

        public MqttFixedHeaderBuilder qosLevel(MqttQoS qosLevel) {
            this.qosLevel = qosLevel;
            return this;
        }

        public MqttFixedHeaderBuilder isRetain(boolean isRetain) {
            this.isRetain = isRetain;
            return this;
        }

        public MqttFixedHeaderBuilder remainingLength(int remainingLength) {
            this.remainingLength = remainingLength;
            return this;
        }

        public MqttFixedHeader build() {
            return new MqttFixedHeader(messageType, isDup, qosLevel, isRetain, remainingLength);
        }
    }


}
