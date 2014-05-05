package com.luxurite.mqtt.server;

import com.ibm.micro.client.mqttv3.MqttClient;
import com.ibm.micro.client.mqttv3.MqttDeliveryToken;
import com.ibm.micro.client.mqttv3.MqttMessage;
import com.ibm.micro.client.mqttv3.MqttTopic;
/**
 * ʹ�� Java Ϊ MQ Telemetry Transport �����첽��������
 * 
 * 
 * 
 *
 * ��Ϣ��������ľ����ʵ��
 * 
 * @author longgangbai
 * 
 */
public class WSMQTTServerPubAsync {
	  public static void main(String[] args) {
		    try {
		    	  //����MqttClient����
			      MqttClient client = new MqttClient(WSMQTTServerCommon.TCPAddress, WSMQTTServerCommon.clientId);
			     
			      //����MQTT��ص�����
			      MqttTopic topic = client.getTopic(WSMQTTServerCommon.topicString);
			      
			      //����MQTT����Ϣ��
			      MqttMessage message = new MqttMessage();
			      //������Ϣ���������
			      message.setQos(2);
			      
			      //�����Ƿ��ڷ������б�����Ϣ��
			      message.setRetained(false);
			      
			      //������Ϣ������
			      message.setPayload(WSMQTTServerCommon.publication.getBytes());
			      
			      //����һ��MQTT�Ļص���
			      WSMQTTServerCallBack callback = new WSMQTTServerCallBack(WSMQTTServerCommon.clientId);
			      
			      //MqttClient��
			      client.setCallback(callback);
			      
			      //MqttClient����
			      client.connect();
			      
			      System.out.println("Publishing \"" + message.toString()
			          + "\" on topic \"" + topic.getName() + "\" with QoS = "
			          + message.getQos());
			      System.out.println("For client instance \"" + client.getClientId()
			          + "\" on address " + client.getServerURI() + "\"");
			      
			      //������Ϣ����ȡ��ִ
			      MqttDeliveryToken token = topic.publish(message);
			      
			      System.out.println("With delivery token \"" + token.hashCode()
			          + " delivered: " + token.isComplete());
			      Thread.sleep(100000000000000l);
			      
			      //�ر�����
			      if (client.isConnected())
			          client.disconnect(WSMQTTServerCommon.quiesceTimeout);
			      System.out.println("Disconnected: delivery token \"" + token.hashCode()
			          + "\" received: " + token.isComplete());
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
	  }
}

