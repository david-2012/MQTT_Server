package com.luxurite.mqtt.server;

import java.util.UUID;
/**
 * 
 * 消息发布消息的常量字段
 * 
 * @author longgangbai
 */
public final class WSMQTTServerCommon {
  //发布broker的ip和端口
  public static final String  TCPAddress =System.getProperty("TCPAddress", "tcp://192.168.1.124:1883");
  //客户端的Id
  public static String clientId =String.format("%-23.23s",  System.getProperty("clientId", (UUID.randomUUID().toString())).trim()).replace('-', '_');
  //发布消息的主题
  public static final String topicString = System.getProperty("topicString", "china/beijing");
  //发布的消息
  public static final String publication =System.getProperty("publication", "Hello World " + String.format("%tc", System.currentTimeMillis()));
  //超时时间
  public static final int quiesceTimeout = Integer.parseInt(System.getProperty("timeout", "10000"));
  
  public static final int  sleepTimeout = Integer.parseInt(System.getProperty("timeout", "10000"));
  
  public static final boolean cleanSession =Boolean.parseBoolean(System.getProperty("cleanSession", "false"));
  
  public static final int QoS =Integer.parseInt(System.getProperty("QoS", "1"));
  
  public static final boolean retained =Boolean.parseBoolean(System.getProperty("retained", "false"));
}
