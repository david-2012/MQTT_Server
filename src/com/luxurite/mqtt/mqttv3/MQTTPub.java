package com.luxurite.mqtt.mqttv3;

import com.ibm.micro.client.mqttv3.MqttClient;
import com.ibm.micro.client.mqttv3.MqttDeliveryToken;
import com.ibm.micro.client.mqttv3.MqttMessage;
import com.ibm.micro.client.mqttv3.MqttTopic;
/**
 * MQTTV3的发布消息类
 * 
 * @author longgangbai
 */
public class MQTTPub { 
	public static void doTest(){ 
		try { 
			MqttClient client = new MqttClient("tcp://192.168.1.124:1883","mqttserver-pub"); 
			MqttTopic topic = client.getTopic("tokudu/china"); 
			MqttMessage message = new MqttMessage("Hello World. Hello IBM".getBytes()); 
			message.setQos(1); 
			client.connect();
			while(true){
				MqttDeliveryToken token = topic.publish(message); 
				while (!token.isComplete()){ 
					token.waitForCompletion(1000); 
				} 
			}
		} catch (Exception e) { 
			e.printStackTrace(); 
		} 
	} 
} 





