package com.luxurite.mqtt.mqttv3;

/**
 * MQTTV3的测试类
 * 
 * @author longgangbai
 */
public class MQTTMain {
	public static void main(String[] args) {
		//订阅消息的方法
		MQTTSubsribe.doTest();
		//发布消息的类
		MQTTPub.doTest();
		
	}
}
