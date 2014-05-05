package com.luxurite.mqtt.server;

import com.ibm.micro.client.mqttv3.MqttClient;
import com.ibm.micro.client.mqttv3.MqttDeliveryToken;
import com.ibm.micro.client.mqttv3.MqttMessage;
import com.ibm.micro.client.mqttv3.MqttTopic;
/**
 * 使用 Java 为 MQ Telemetry Transport 创建异步发布程序
 * 
 * 
 * 
 *
 * 消息发布的类的具体的实现
 * 
 * @author longgangbai
 * 
 */
public class WSMQTTServerPubAsync {
	  public static void main(String[] args) {
		    try {
		    	  //创建MqttClient对象
			      MqttClient client = new MqttClient(WSMQTTServerCommon.TCPAddress, WSMQTTServerCommon.clientId);
			     
			      //创建MQTT相关的主题
			      MqttTopic topic = client.getTopic(WSMQTTServerCommon.topicString);
			      
			      //创建MQTT的消息体
			      MqttMessage message = new MqttMessage();
			      //设置消息传输的类型
			      message.setQos(2);
			      
			      //设置是否在服务器中保存消息体
			      message.setRetained(false);
			      
			      //设置消息的内容
			      message.setPayload(WSMQTTServerCommon.publication.getBytes());
			      
			      //创建一个MQTT的回调类
			      WSMQTTServerCallBack callback = new WSMQTTServerCallBack(WSMQTTServerCommon.clientId);
			      
			      //MqttClient绑定
			      client.setCallback(callback);
			      
			      //MqttClient连接
			      client.connect();
			      
			      System.out.println("Publishing \"" + message.toString()
			          + "\" on topic \"" + topic.getName() + "\" with QoS = "
			          + message.getQos());
			      System.out.println("For client instance \"" + client.getClientId()
			          + "\" on address " + client.getServerURI() + "\"");
			      
			      //发送消息并获取回执
			      MqttDeliveryToken token = topic.publish(message);
			      
			      System.out.println("With delivery token \"" + token.hashCode()
			          + " delivered: " + token.isComplete());
			      Thread.sleep(100000000000000l);
			      
			      //关闭连接
			      if (client.isConnected())
			          client.disconnect(WSMQTTServerCommon.quiesceTimeout);
			      System.out.println("Disconnected: delivery token \"" + token.hashCode()
			          + "\" received: " + token.isComplete());
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
	  }
}

