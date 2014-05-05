package com.luxurite.mqtt.client;

import com.ibm.micro.client.mqttv3.MqttClient;
import com.ibm.micro.client.mqttv3.MqttConnectOptions;
/**
 * 
 * ʹ�� Java Ϊ MQ Telemetry Transport ��������
 * �ڴ������У�������ѭ�̳�����������Ӧ�ó��򡣶�����������ⴴ��Ԥ�������ո�Ԥ���ķ�����
 *	�ṩ��һ��ʾ������Ӧ�ó��� Subscribe��Subscribe ������Ԥ������ MQTT Examples�����ȴ���
 *	�ø�Ԥ���ķ������ȴ�ʱ��Ϊ 30 �롣�������Դ���Ԥ�����ȴ���÷������������Խ��շ�������ǰ
 *	Ϊͬһ�ͻ�����ʶ������Ԥ���ķ�����
 * @author longgangbai 
 */
public class WSMQTTClientSubscribe {
	  public static void main(String[] args) {
		    try {
		    	  
		    	 //����MQTT�ͻ��˶���
			      MqttClient client = new MqttClient(WSMQTTClientConstants.TCPAddress, WSMQTTClientConstants.clientId);
			      
			      //�����ͻ���MQTT�ص���
			      WSMQTTClientCallBack callback = new WSMQTTClientCallBack(WSMQTTClientConstants.clientId);
			      
			      //����MQTT�ص�
			      client.setCallback(callback);
			      
			      //����һ�����Ӷ���
			      MqttConnectOptions conOptions = new MqttConnectOptions();
			      
			      //��������Ự��Ϣ
			      conOptions.setCleanSession(WSMQTTClientConstants.cleanSession);
			      
			      //���ó�ʱʱ��
			      conOptions.setConnectionTimeout(10000);
			      
			      //���ûỰ����ʱ��
			      conOptions.setKeepAliveInterval(20000);
			      
			      //�������ն˿ڵ�֪ͨ��Ϣ
			      conOptions.setWill(client.getTopic("LastWillTopic"), "the client will stop !".getBytes(), 1, false);
			      
			      //����broker
			      client.connect(conOptions);
			      System.out.println("Subscribing to topic \"" + WSMQTTClientConstants.topicString
			          + "\" for client instance \"" + client.getClientId()
			          + "\" using QoS " + WSMQTTClientConstants.QoS + ". Clean session is "
			          + WSMQTTClientConstants.cleanSession);
			      //������ص�������Ϣ
			      client.subscribe(WSMQTTClientConstants.topicString, WSMQTTClientConstants.QoS);
			      System.out.println("Going to sleep for " + WSMQTTClientConstants.sleepTimeout / 1000
			          + " seconds");
			      
			      Thread.sleep(100000000000000l);
			      //�ر���ص�MQTT����
			      if(client.isConnected()){
			    	  client.disconnect();
			      }
			      System.out.println("Finished");
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
	  }
}
