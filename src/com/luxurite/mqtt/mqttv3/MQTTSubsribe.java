package com.luxurite.mqtt.mqttv3;

import com.ibm.micro.client.mqttv3.MqttClient;
import com.ibm.micro.client.mqttv3.MqttConnectOptions;
/**
 * MQTTV3的订阅消息类
 * 
 * @author longgangbai
 */
public class MQTTSubsribe { 
	public static String doTest() { 
		try { 
			//创建MqttClient
			MqttClient client = new MqttClient("tcp://192.168.1.124:1883", "java_client0000000000"); 
			//回调处理类
			CallBack callback = new CallBack(); 
			client.setCallback(callback); 
			//创建连接可选项信息
			MqttConnectOptions conOptions = new MqttConnectOptions(); 
			//
			conOptions.setCleanSession(false); 
			//连接broker
			client.connect(conOptions); 
			//发布相关的订阅
			client.subscribe("tokudu/china", 1); 
			//client.disconnect(); 
		} catch (Exception e) { 
			e.printStackTrace(); 
			return "failed"; 
		} 
		return "success"; 
	} 
} 

