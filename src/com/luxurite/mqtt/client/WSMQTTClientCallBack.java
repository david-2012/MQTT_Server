package com.luxurite.mqtt.client;

import com.ibm.micro.client.mqttv3.*;
/**
 * 
 * ��Ϣ������صĻص���ʹ��
 * 
 * ����ʵ��MqttCallback�Ľӿڲ�ʵ�ֶ�Ӧ����ؽӿڷ���
 *  
 * @author longgangbai
 */
public class WSMQTTClientCallBack implements MqttCallback {
	  private String instanceData = "";
	  public WSMQTTClientCallBack(String instance) {
	    instanceData = instance;
	  }
	  public void messageArrived(MqttTopic topic, MqttMessage message) {
		    try {
		      System.out.println("Message arrived: \"" + message.toString()
		          + "\" on topic \"" + topic.toString() + "\" for instance \""
		          + instanceData + "\"");
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
	  }
	  public void connectionLost(Throwable cause) {
		    System.out.println("Connection lost on instance \"" + instanceData
		        + "\" with cause \"" + cause.getMessage() + "\" Reason code " 
		        + ((MqttException)cause).getReasonCode() + "\" Cause \"" 
		        + ((MqttException)cause).getCause() +  "\"");    
		    cause.printStackTrace();
	  }
	  public void deliveryComplete(MqttDeliveryToken token) {
		    try {
		      System.out.println("Delivery token \"" + token.hashCode()
		          + "\" received by instance \"" + instanceData + "\"");
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
	  }
}
