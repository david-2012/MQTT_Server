package com.luxurite.mqtt;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.Message;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * MQTT moquette 的Client 段用于订阅主题，并接收主题信息
 * 
 * 采用阻塞式 订阅主题 
 * 
 * @author You
 */
public class MQTTClient {
	  private static final Logger LOG = LoggerFactory.getLogger(MQTTClient.class);
		private final static String CONNECTION_STRING = "tcp://112.124.109.124:1883";
//	  	private final static String CONNECTION_STRING = "tcp://192.168.1.124:1883";
		private final static boolean CLEAN_START = true;
		private final static short KEEP_ALIVE = 30;// 低耗网络，但是又需要及时获取数据，心跳30s
		private final static String CLIENT_ID = "publishService";
		public  static Topic[] topics = {
			    		new Topic("outTopic", QoS.EXACTLY_ONCE),
			    		new Topic("outTopic", QoS.AT_LEAST_ONCE),
			    		new Topic("outTopic", QoS.AT_MOST_ONCE)};
		public final  static long RECONNECTION_ATTEMPT_MAX=6;
		public final  static long RECONNECTION_DELAY=2000;
		
		public final static int SEND_BUFFER_SIZE=2*1024*1024;//发送最大缓冲为2M
		
		
	  public static void main(String[] args)   {
		//创建MQTT对象
	    MQTT mqtt = new MQTT();
	    BlockingConnection connection=null;
	    try {
	    	//设置mqtt broker的ip和端口
			mqtt.setHost(CONNECTION_STRING);
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
			
			
			//获取mqtt的连接对象BlockingConnection
		    connection = mqtt.blockingConnection();
		    //MQTT连接的创建 
		    connection.connect();
		    //创建相关的MQTT 的主题列表 
//		    Topic[] topics = {new Topic("china/shenzhen", QoS.AT_LEAST_ONCE)};
		    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");//设置日期格式
//		    Topic[] topics = {new Topic("sensors/a4/out/debug", QoS.EXACTLY_ONCE)};
		    Topic[] topics = {new Topic("cmdTest", QoS.EXACTLY_ONCE)};
		    //订阅相关的主题信息 
		    byte[] qoses = connection.subscribe(topics);
		    //
		    while(true){
			    //接收订阅的消息内容
		    	Message message = connection.receive();
		    	//获取订阅的消息内容 
			    byte[] payload = message.getPayload();
			    // process the message then:
			    LOG.info("MQTTClient Message  Topic="+message.getTopic()+" Content :"+new String(payload)+"   接收数据时间："+df.format(new Date()));
			    //签收消息的回执
			    message.ack();
			    
//			    Thread.sleep(2000);
		    }
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				connection.disconnect();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

