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
 * MQTT moquette ��Client �����ڶ������⣬������������Ϣ
 * 
 * ��������ʽ �������� 
 * 
 * @author You
 */
public class MQTTClient {
	  private static final Logger LOG = LoggerFactory.getLogger(MQTTClient.class);
		private final static String CONNECTION_STRING = "tcp://112.124.109.124:1883";
//	  	private final static String CONNECTION_STRING = "tcp://192.168.1.124:1883";
		private final static boolean CLEAN_START = true;
		private final static short KEEP_ALIVE = 30;// �ͺ����磬��������Ҫ��ʱ��ȡ���ݣ�����30s
		private final static String CLIENT_ID = "publishService";
		public  static Topic[] topics = {
			    		new Topic("outTopic", QoS.EXACTLY_ONCE),
			    		new Topic("outTopic", QoS.AT_LEAST_ONCE),
			    		new Topic("outTopic", QoS.AT_MOST_ONCE)};
		public final  static long RECONNECTION_ATTEMPT_MAX=6;
		public final  static long RECONNECTION_DELAY=2000;
		
		public final static int SEND_BUFFER_SIZE=2*1024*1024;//������󻺳�Ϊ2M
		
		
	  public static void main(String[] args)   {
		//����MQTT����
	    MQTT mqtt = new MQTT();
	    BlockingConnection connection=null;
	    try {
	    	//����mqtt broker��ip�Ͷ˿�
			mqtt.setHost(CONNECTION_STRING);
			//����ǰ��ջỰ��Ϣ
			mqtt.setCleanSession(CLEAN_START);
			//�����������ӵĴ���
			mqtt.setReconnectAttemptsMax(RECONNECTION_ATTEMPT_MAX);
			//���������ļ��ʱ��
			mqtt.setReconnectDelay(RECONNECTION_DELAY);
			//��������ʱ��
			mqtt.setKeepAlive(KEEP_ALIVE);
			//���û���Ĵ�С
			mqtt.setSendBufferSize(SEND_BUFFER_SIZE);
			
			
			//��ȡmqtt�����Ӷ���BlockingConnection
		    connection = mqtt.blockingConnection();
		    //MQTT���ӵĴ��� 
		    connection.connect();
		    //������ص�MQTT �������б� 
//		    Topic[] topics = {new Topic("china/shenzhen", QoS.AT_LEAST_ONCE)};
		    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");//�������ڸ�ʽ
//		    Topic[] topics = {new Topic("sensors/a4/out/debug", QoS.EXACTLY_ONCE)};
		    Topic[] topics = {new Topic("cmdTest", QoS.EXACTLY_ONCE)};
		    //������ص�������Ϣ 
		    byte[] qoses = connection.subscribe(topics);
		    //
		    while(true){
			    //���ն��ĵ���Ϣ����
		    	Message message = connection.receive();
		    	//��ȡ���ĵ���Ϣ���� 
			    byte[] payload = message.getPayload();
			    // process the message then:
			    LOG.info("MQTTClient Message  Topic="+message.getTopic()+" Content :"+new String(payload)+"   ��������ʱ�䣺"+df.format(new Date()));
			    //ǩ����Ϣ�Ļ�ִ
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

