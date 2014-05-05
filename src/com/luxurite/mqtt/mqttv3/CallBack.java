package com.luxurite.mqtt.mqttv3;

import com.ibm.micro.client.mqttv3.MqttCallback;
import com.ibm.micro.client.mqttv3.MqttDeliveryToken;
import com.ibm.micro.client.mqttv3.MqttMessage;
import com.ibm.micro.client.mqttv3.MqttTopic;
/**
 * 回调处理类
 * 处理订阅的消息类
 * 
 * @author longgangbai
 */
public class CallBack implements MqttCallback { 
	
	public CallBack() { 
	} 
	/**
	 * 接收到信息的处理
	 */
	public void messageArrived(MqttTopic topic, MqttMessage message) { 
		try { 
			System.out.println(" MQTTSubsribe  message.toString()"+message.toString());
		} catch (Exception e) { 
			e.printStackTrace(); 
		} 
	} 
	public void connectionLost(Throwable cause) {
		
	} 
	public void deliveryComplete(MqttDeliveryToken token) {
		
	} 
} 

