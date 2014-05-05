package com.luxurite.mqtt.client;
/**
 * 
 * ��Ϣ������Ϣ�ĳ����ֶ�
 * 
 * @author longgangbai
 */
public final class WSMQTTClientConstants {
	
  public static final String TCPAddress = System.getProperty("TCPAddress", "tcp://192.168.1.124:1883");
  public static String  clientId = String.format("%-23.23s",(System.getProperty("user.name") + "_" + (System.getProperty("clientId", "Subscribe."))).trim()).replace('-', '_');
  public static final String  topicString = System.getProperty("topicString", "china/beijing");
  public static final String  publication =System.getProperty("publication", "Hello World " + String.format("%tc", System.currentTimeMillis()));
  public static final int quiesceTimeout = Integer.parseInt(System.getProperty("timeout", "10000"));
  public static final int sleepTimeout =Integer.parseInt(System.getProperty("timeout", "10000000"));
  public static final boolean cleanSession = Boolean.parseBoolean(System.getProperty("cleanSession", "false"));
  public static final int  QoS = Integer.parseInt(System.getProperty("QoS", "1"));
  public static final boolean  retained = Boolean.parseBoolean(System.getProperty("retained", "false"));
}
