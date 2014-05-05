package com.luxurite.mqtt.mqttv3;

import com.ibm.micro.client.mqttv3.MqttClient;
import com.ibm.micro.client.mqttv3.MqttConnectOptions;
/**
 * MQTTV3�Ķ�����Ϣ��
 * 
 * @author longgangbai
 */
public class MQTTSubsribe { 
	public static String doTest() { 
		try { 
			//����MqttClient
			MqttClient client = new MqttClient("tcp://192.168.1.124:1883", "java_client0000000000"); 
			//�ص�������
			CallBack callback = new CallBack(); 
			client.setCallback(callback); 
			//�������ӿ�ѡ����Ϣ
			MqttConnectOptions conOptions = new MqttConnectOptions(); 
			//
			conOptions.setCleanSession(false); 
			//����broker
			client.connect(conOptions); 
			//������صĶ���
			client.subscribe("tokudu/china", 1); 
			//client.disconnect(); 
		} catch (Exception e) { 
			e.printStackTrace(); 
			return "failed"; 
		} 
		return "success"; 
	} 
} 

