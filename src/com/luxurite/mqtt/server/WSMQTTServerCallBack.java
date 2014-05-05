package com.luxurite.mqtt.server;

import com.ibm.micro.client.mqttv3.*;
/**
 * 发布消息的回调类
 * 
 * 必须实现MqttCallback的接口并实现对应的相关接口方法
 * 		◦CallBack 类将实现 MqttCallBack。每个客户机标识都需要一个回调实例。在此示例中，构造函数传递客户机标识以另存为实例数据。在回调中，将它用来标识已经启动了该回调的哪个实例。
 * 	◦必须在回调类中实现三个方法：
 * 
 * 	public void messageArrived(MqttTopic topic, MqttMessage message)
 * 	接收已经预订的发布。
 * 
 * 	public void connectionLost(Throwable cause)
 * 	在断开连接时调用。
 * 
 * 	public void deliveryComplete(MqttDeliveryToken token))
 * 		接收到已经发布的 QoS 1 或 QoS 2 消息的传递令牌时调用。
 * 
 * 
 * 	◦由 MqttClient.connect 激活此回调。
 * 
 * @author longgangbai
 */
public class WSMQTTServerCallBack implements MqttCallback {
	  private String instanceData = "";
	  public WSMQTTServerCallBack(String instance) {
	    instanceData = instance;
	  }
	  /**
	   * 接收到消息的回调的方法
	   */
	  public void messageArrived(MqttTopic topic, MqttMessage message) {
	    try {
	      System.out.println("Message arrived: \"" + message.toString()
	          + "\" on topic \"" + topic.toString() + "\" for instance \""
	          + instanceData + "\"");
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }
	  /**
	   * 消息连接丢失
	   */
	  public void connectionLost(Throwable cause) {
	    System.out.println("Connection lost on instance \"" + instanceData
	        + "\" with cause \"" + cause.getMessage() + "\" Reason code " 
	        + ((MqttException)cause).getReasonCode() + "\" Cause \"" 
	        + ((MqttException)cause).getCause() +  "\"");    
	    cause.printStackTrace();
	  }
	  /**
	   * 
	   */
	  public void deliveryComplete(MqttDeliveryToken token) {
	    try {
	      System.out.println("Delivery token \"" + token.hashCode()
	          + "\" received by instance \"" + instanceData + "\"");
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }
}
