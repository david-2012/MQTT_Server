package com.luxurite.mqtt.push;

import com.ibm.mqtt.MqttAdvancedCallback;
import com.ibm.mqtt.MqttClient;
import com.ibm.mqtt.MqttException;
import com.ibm.mqtt.MqttSimpleCallback;
/**
 * 
 Android���ͷ���������MQTT/XMPP/GCM��
 ����1�� ʹ��GCM����Google Cloud Messaging��
��飺Google�Ƴ�������Ϣ���񣬼��ڶ�����G2DM��
�ŵ㣺Google�ṩ�ķ���ԭ�����򵥣�����ʵ�ֺͲ������ˡ�
ȱ�㣺Android�汾���ƣ��������2.2�汾�����÷����ڹ��ڲ����ȶ�����Ҫ�û���Google�ʺţ�������Google��

����2�� ʹ��XMPPЭ�飨Openfire + Spark + Smack��
��飺����XMLЭ���ͨѶЭ�飬ǰ����Jabber��Ŀǰ����IETF���ʱ�׼����֯����˱�׼��������
�ŵ㣺Э����졢ǿ�󡢿���չ��ǿ��Ŀǰ��ҪӦ�����������ϵͳ�У������п�Դ��Java��Ŀ���ʵ��androidpn��
ȱ�㣺Э��ϸ��ӡ����ࣨ����XML�������������ѵ磬����Ӳ���ɱ��ߡ�

����3�� ʹ��MQTTЭ��
��飺�������ġ����ڴ���ġ�����/���ġ�ģʽ����Ϣ����Э�顣
�ŵ㣺Э���ࡢС�ɡ�����չ��ǿ��ʡ������ʡ�磬Ŀǰ�Ѿ�Ӧ�õ���ҵ���򣨲ο��� 
������C++��ķ�������rsmb��
ȱ�㣺�������졢ʵ�ֽϸ��ӡ���������rsmb����Դ������Ӳ���ɱ��ϸߡ�

����4�� ʹ��HTTP��ѭ��ʽ
��飺��ʱ��HTTP����˽ӿڣ�Web Service API����ȡ������Ϣ��
�ŵ㣺ʵ�ּ򵥡��ɿ���ǿ������Ӳ���ɱ��͡�
ȱ�㣺ʵʱ�Բ

�Ը�����������ȱ����о��ͶԱȣ��Ƽ�ʹ��MQTTЭ��ķ�������ʵ�֣���Ҫԭ���ǣ� MQTT����٣�Ҳ��ʡ����
���̶�ͷ���Ƚ�Ϊ2�ֽڣ����Ҽ�����չ���ʺ϶��ο��� �������������Ǿ�������ʹ��MQTT��������Android��Ϣ��ԭ��
�ͷ������������Լ������ͷ���


 * 
 */
public class MQTTPubSub {
		 private final static String CONNECTION_STRING = "tcp://192.168.1.124:1883";
		 private final static boolean CLEAN_START = true;
		 private final static short KEEP_ALIVE = 30;//�ͺ����磬��������Ҫ��ʱ��ȡ���ݣ�����30s
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
				   //����MqttClient����
				   mqttClient = new MqttClient(CONNECTION_STRING);
				   //�����ص�������
				   SimpleCallbackHandler simpleCallbackHandler = new SimpleCallbackHandler();
				   //mqttClient.registerSimpleHandler(simpleCallbackHandler);//ע�������Ϣ����
				   mqttClient.registerAdvancedHandler(new AdvancedCallbackHandler());//ע�������Ϣ����
				   //��������
				   mqttClient.connect(CLIENT_ID, CLEAN_START, KEEP_ALIVE);
				 //���Ľ�����
				   mqttClient.subscribe(TOPICS, QOS_VALUES);
				   /**
				    * ��ɶ��ĺ󣬿���������������������ͨ����Ҳ���Է����Լ�����Ϣ
				    */
				   mqttClient.publish(PUBLISH_TOPICS, "keepalive".getBytes(), QOS_VALUES[0], true);
			  } catch (MqttException e) {
				  e.printStackTrace();
			  }
		 }
		
		 /**
		  * �򵥻ص�����������client���յ���������Ϣ
		  * @author pig
		  *
		  */
		 class SimpleCallbackHandler implements MqttSimpleCallback{
		
			  /**
			   * ���ͻ�����broker����Ͽ�ʱ����
			   * �����ٴ˴������¶���
			   */
			  @Override
			  public void connectionLost() throws Exception {
				   System.out.println("�ͻ�����broker�Ѿ��Ͽ�");
			  }
			
			  /**
			   * �ͻ��˶�����Ϣ�󣬸÷�������ص����մ�����Ϣ
			   */
			  @Override
			  public void publishArrived(String topicName, byte[] payload, int Qos, boolean retained) throws Exception {
				   System.out.println("��������: " + topicName);
				   System.out.println("��Ϣ����: " + new String(payload));
				   System.out.println("��Ϣ����(0,1,2): " + Qos);
				   System.out.println("�Ƿ���ʵʱ���͵���Ϣ(false=ʵʱ��true=�������ϱ����������Ϣ): " + retained);
			  }
		  
		 }
		 
		 /**
		  * �߼��ص�
		  * @author pig
		  *
		  */
		 class AdvancedCallbackHandler implements MqttAdvancedCallback{

			@Override
			public void connectionLost() throws Exception {
				// TODO Auto-generated method stub
				
			}

			/**
			 * ���յ�����Ϣ����Ϣ
			 */
			@Override
			public void publishArrived(String topicName, byte[] payload, int Qos,
					boolean retained) throws Exception {
				   System.out.println("��������: " + topicName);
				   System.out.println("��Ϣ����: " + new String(payload));
				   System.out.println("��Ϣ����(0,1,2): " + Qos);
				   System.out.println("�Ƿ���ʵʱ���͵���Ϣ(false=ʵʱ��true=�������ϱ����������Ϣ): " + retained);
			}

			@Override
			public void published(int arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void subscribed(int Qos, byte[] payload) {
				   System.out.println("��Ϣ����: " + new String(payload));
				   System.out.println("��Ϣ����(0,1,2): " + Qos);
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



