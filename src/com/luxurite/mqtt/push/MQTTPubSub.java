package com.luxurite.mqtt.push;

import com.ibm.mqtt.MqttAdvancedCallback;
import com.ibm.mqtt.MqttClient;
import com.ibm.mqtt.MqttException;
import com.ibm.mqtt.MqttSimpleCallback;
/**
 * 
 Android推送方案分析（MQTT/XMPP/GCM）
 方案1、 使用GCM服务（Google Cloud Messaging）
简介：Google推出的云消息服务，即第二代的G2DM。
优点：Google提供的服务、原生、简单，无需实现和部署服务端。
缺点：Android版本限制（必须大于2.2版本），该服务在国内不够稳定、需要用户绑定Google帐号，受限于Google。

方案2、 使用XMPP协议（Openfire + Spark + Smack）
简介：基于XML协议的通讯协议，前身是Jabber，目前已由IETF国际标准化组织完成了标准化工作。
优点：协议成熟、强大、可扩展性强、目前主要应用于许多聊天系统中，且已有开源的Java版的开发实例androidpn。
缺点：协议较复杂、冗余（基于XML）、费流量、费电，部署硬件成本高。

方案3、 使用MQTT协议
简介：轻量级的、基于代理的“发布/订阅”模式的消息传输协议。
优点：协议简洁、小巧、可扩展性强、省流量、省电，目前已经应用到企业领域（参考： 
且已有C++版的服务端组件rsmb。
缺点：不够成熟、实现较复杂、服务端组件rsmb不开源，部署硬件成本较高。

方案4、 使用HTTP轮循方式
简介：定时向HTTP服务端接口（Web Service API）获取最新消息。
优点：实现简单、可控性强，部署硬件成本低。
缺点：实时性差。

对各个方案的优缺点的研究和对比，推荐使用MQTT协议的方案进行实现，主要原因是： MQTT最快速，也最省流量
（固定头长度仅为2字节），且极易扩展，适合二次开发 。接下来，我们就来分析使用MQTT方案进行Android消息的原理
和方法，并架设自己的推送服务。


 * 
 */
public class MQTTPubSub {
		 private final static String CONNECTION_STRING = "tcp://192.168.1.124:1883";
		 private final static boolean CLEAN_START = true;
		 private final static short KEEP_ALIVE = 30;//低耗网络，但是又需要及时获取数据，心跳30s
		 private final static String CLIENT_ID = "client1";
		 public String PUBLISH_TOPICS="tokudu/china";
		 private final static String[] TOPICS = {
			  "Test/TestTopics/Topic1",
			  "Test/TestTopics/Topic2",
			  "Test/TestTopics/Topic3",
			  "china/beijing"
		 };
		 private final static int[] QOS_VALUES = {0, 0, 2, 0};
		 
		 //////////////////
		 private MqttClient mqttClient = null;
		 
		 public MQTTPubSub(){
			  try {
				   //创建MqttClient对象
				   mqttClient = new MqttClient(CONNECTION_STRING);
				   //创建回调处理器
				   SimpleCallbackHandler simpleCallbackHandler = new SimpleCallbackHandler();
				   //mqttClient.registerSimpleHandler(simpleCallbackHandler);//注册接收消息方法
				   mqttClient.registerAdvancedHandler(new AdvancedCallbackHandler());//注册接收消息方法
				   //创建连接
				   mqttClient.connect(CLIENT_ID, CLEAN_START, KEEP_ALIVE);
				 //订阅接主题
				   mqttClient.subscribe(TOPICS, QOS_VALUES);
				   /**
				    * 完成订阅后，可以增加心跳，保持网络通畅，也可以发布自己的消息
				    */
				   mqttClient.publish(PUBLISH_TOPICS, "keepalive".getBytes(), QOS_VALUES[0], true);
			  } catch (MqttException e) {
				  e.printStackTrace();
			  }
		 }
		
		 /**
		  * 简单回调函数，处理client接收到的主题消息
		  * @author pig
		  *
		  */
		 class SimpleCallbackHandler implements MqttSimpleCallback{
		
			  /**
			   * 当客户机和broker意外断开时触发
			   * 可以再此处理重新订阅
			   */
			  @Override
			  public void connectionLost() throws Exception {
				   System.out.println("客户机和broker已经断开");
			  }
			
			  /**
			   * 客户端订阅消息后，该方法负责回调接收处理消息
			   */
			  @Override
			  public void publishArrived(String topicName, byte[] payload, int Qos, boolean retained) throws Exception {
				   System.out.println("订阅主题: " + topicName);
				   System.out.println("消息数据: " + new String(payload));
				   System.out.println("消息级别(0,1,2): " + Qos);
				   System.out.println("是否是实时发送的消息(false=实时，true=服务器上保留的最后消息): " + retained);
			  }
		  
		 }
		 
		 /**
		  * 高级回调
		  * @author pig
		  *
		  */
		 class AdvancedCallbackHandler implements MqttAdvancedCallback{

			@Override
			public void connectionLost() throws Exception {
				// TODO Auto-generated method stub
				
			}

			/**
			 * 接收到的消息的信息
			 */
			@Override
			public void publishArrived(String topicName, byte[] payload, int Qos,
					boolean retained) throws Exception {
				   System.out.println("订阅主题: " + topicName);
				   System.out.println("消息数据: " + new String(payload));
				   System.out.println("消息级别(0,1,2): " + Qos);
				   System.out.println("是否是实时发送的消息(false=实时，true=服务器上保留的最后消息): " + retained);
			}

			@Override
			public void published(int arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void subscribed(int Qos, byte[] payload) {
				   System.out.println("消息数据: " + new String(payload));
				   System.out.println("消息级别(0,1,2): " + Qos);
			}

			@Override
			public void unsubscribed(int arg0) {
				// TODO Auto-generated method stub
				
			}
		  
		 }
		 
		 /**
		  * @param args
		  */
		 public static void main(String[] args) {
			   new MQTTPubSub();
		 }

}



