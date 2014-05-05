package com.luxurite.mqtt.callback;

import java.net.URISyntaxException;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.Listener;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * MQTT moquette 的Client 段用于订阅主题，并接收主题信息
 * 
 * 采用Callback式 订阅主题 
 * 
 * @author longgangbai
 */
public class MQTTCallbackClient {
	  private static final Logger LOG = LoggerFactory.getLogger(MQTTCallbackClient.class);
		private final static String CONNECTION_STRING = "tcp://localhost:1883";
		private final static boolean CLEAN_START = true;
		private final static short KEEP_ALIVE = 30;// 低耗网络，但是又需要及时获取数据，心跳30s
		public  static Topic[] topics = {
			    		new Topic("china/beijing", QoS.AT_MOST_ONCE),
			    		new Topic("china/tianjin", QoS.AT_LEAST_ONCE),
			    		new Topic("china/henan", QoS.AT_MOST_ONCE)};
		public final  static long RECONNECTION_ATTEMPT_MAX=6;
		public final  static long RECONNECTION_DELAY=2000;
		 final String topic = "china/beijing";
		public final static int SEND_BUFFER_SIZE=2*1024*1024;//发送最大缓冲为2M
		
		
		
	  public static void main(String[] args)   {
		//创建MQTT对象
	    MQTT mqtt = new MQTT();
    	//设置mqtt broker的ip和端口
		try {
			mqtt.setHost(CONNECTION_STRING);
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
		//连接前清空会话信息
		mqtt.setCleanSession(CLEAN_START);
		//设置重新连接的次数
		mqtt.setReconnectAttemptsMax(RECONNECTION_ATTEMPT_MAX);
		//设置重连的间隔时间
		mqtt.setReconnectDelay(RECONNECTION_DELAY);
		//设置心跳时间
		mqtt.setKeepAlive(KEEP_ALIVE);
		//设置缓冲的大小
		mqtt.setSendBufferSize(SEND_BUFFER_SIZE);
	    //获取mqtt的连接对象CallbackConnection
		final CallbackConnection connection= mqtt.callbackConnection();
	    try {
	
		    //添加连接的监听事件
		    connection.listener(new Listener() {
		      
		        public void onDisconnected() {
		        }
		        public void onConnected() {
		        	System.out.println(" 连接成功!");
		        }

		        public void onPublish(UTF8Buffer topic, Buffer payload, Runnable onComplete) {
		        
		        }
		        public void onFailure(Throwable value) {
		        
		        }
		    });
		    //添加连接事件
		    connection.connect(new Callback<Void>() {
		    	/**
		    	 * 连接失败的操作
		    	 */
		        public void onFailure(Throwable value) {
		             // If we could not connect to the server.
		        	System.out.println("MQTTSubscribeClient.CallbackConnection.connect.onFailure  连接失败......"+value.getMessage());
		        	value.printStackTrace();
		        }
		   

		        /**
		         * 连接成功的操作
		         * @param v
		         */
		        public void onSuccess(Void v) {
		          	System.out.println("MQTTSubscribeClient.CallbackConnection.connect.onSuccess 订阅连接成功......");
			          
		            //订阅相关的主题
		            connection.subscribe(topics, new Callback<byte[]>() {
		                public void onSuccess(byte[] qoses) {
		                	System.out.println("MQTTSubscribeClient.CallbackConnection.connect.subscribe.onSuccess 订阅主题成功......");
		                }
		                public void onFailure(Throwable value) {
		                     // subscribe failed.
		                	System.out.println("MQTTSubscribeClient.CallbackConnection.connect.subscribe.onSuccess 订阅主题失败！"+value.getMessage());
		                	value.printStackTrace();
		                }
		            });

		            
		        }
		    });
		    Thread.sleep(100L);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
//            //连接断开
            connection.disconnect(new Callback<Void>() {
                public void onSuccess(Void v) {
                  // called once the connection is disconnected.
                	System.out.println("MQTTSubscribeClient.CallbackConnection.connect.disconnect.onSuccess called once the connection is disconnected.");
                }
                public void onFailure(Throwable value) {
                  // Disconnects never fail.
                	System.out.println("MQTTSubscribeClient.CallbackConnection.connect.disconnect.onFailure  Disconnects never fail."+value.getMessage());
                	value.printStackTrace();
                }
            });
		}
	}
}
