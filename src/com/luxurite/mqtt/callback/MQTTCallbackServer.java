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
 * MQTT moquette ��Server �����ڲ�����������Ϣ
 * 
 * ����Callbackʽ �������� 
 * 
 * @author longgangbai
 */
public class MQTTCallbackServer {
	  private static final Logger LOG = LoggerFactory.getLogger(MQTTCallbackServer.class);
		private final static String CONNECTION_STRING = "tcp://localhost:1883";
		private final static boolean CLEAN_START = true;
		private final static short KEEP_ALIVE = 30;// �ͺ����磬��������Ҫ��ʱ��ȡ���ݣ�����30s
		public  static Topic[] topics = {
			    		new Topic("china/beijing", QoS.EXACTLY_ONCE),
			    		new Topic("china/tianjin", QoS.AT_LEAST_ONCE),
			    		new Topic("china/henan", QoS.AT_MOST_ONCE)};
		public final  static long RECONNECTION_ATTEMPT_MAX=6;
		public final  static long RECONNECTION_DELAY=2000;
		
		public final static int SEND_BUFFER_SIZE=2*1024*1024;//������󻺳�Ϊ2M
		
		
	  public static void main(String[] args)   {
		//����MQTT����
	    MQTT mqtt = new MQTT();
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
			 final CallbackConnection connection = mqtt.callbackConnection();
			 
			
		    //������ӵļ����¼�
		    connection.listener(new Listener() {
		      
		        public void onDisconnected() {
		        }
		        public void onConnected() {
		        }

		        public void onPublish(UTF8Buffer topic, Buffer payload, Runnable ack) {
		            // You can now process a received message from a topic.
		            // Once process execute the ack runnable.
		            ack.run();
		            System.out.println("topic"+topic.toString()+"="+new String(payload.getData()));
		        }
		        public void onFailure(Throwable value) {
		        }
		    });
		    //��������¼�
		    connection.connect(new Callback<Void>() {
		    	/**
		    	 * ����ʧ�ܵĲ���
		    	 */
		        public void onFailure(Throwable value) {
		             // If we could not connect to the server.
		        	System.out.println("MQTTCallbackServer.CallbackConnection.connect.onFailure"+"����ʧ��......"+value.getMessage());
		        	value.printStackTrace();
		        }
		  
		        /**
		         * ���ӳɹ��Ĳ���
		         * @param v
		         */
		        public void onSuccess(Void v) {
		        
                 int count=1;
                 while(true){
                	count++;
 		            // ���ڷ�����Ϣ��Ŀǰ�ֻ��β���Ҫ�����˷�����Ϣ
                	//���������
                	final String message="hello "+count+"chinese people !";
 		            final String topic = "china/beijing";
 		            System.out.println("MQTTCallbackServer  publish  topic="+topic+" message :"+message);
					connection.publish(topic, message.getBytes(), QoS.AT_LEAST_ONCE, false, new Callback<Void>() {
 		                public void onSuccess(Void v) {
 		                  // the pubish operation completed successfully.
 		                }
 		                public void onFailure(Throwable value) {
 		                	value.printStackTrace();
 		                }
 		            });
 		            try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                 }
		            
//		            //���ӶϿ�
//		            connection.disconnect(new Callback<Void>() {
//		                public void onSuccess(Void v) {
//		                  // called once the connection is disconnected.
//		                	System.out.println("MQTTSubscribeClient.CallbackConnection.connect.disconnect.onSuccess", "called once the connection is disconnected.");
//		                }
//		                public void onFailure(Throwable value) {
//		                  // Disconnects never fail.
//		                	System.out.println("MQTTSubscribeClient.CallbackConnection.connect.disconnect.onFailure", "Disconnects never fail."+value.getMessage());
//		                	value.printStackTrace();
//		                }
//		            });
		            
		            
		        }
		    });
		    Thread.sleep(10000000000L);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
		}
	}
}

