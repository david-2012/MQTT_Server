package com.luxurite.mqtt;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoServiceStatistics;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.demux.DemuxingProtocolDecoder;
import org.apache.mina.filter.codec.demux.DemuxingProtocolEncoder;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.dna.mqtt.moquette.messaging.spi.impl.SimpleMessaging;
import org.dna.mqtt.moquette.proto.ConnAckEncoder;
import org.dna.mqtt.moquette.proto.ConnectDecoder;
import org.dna.mqtt.moquette.proto.DisconnectDecoder;
import org.dna.mqtt.moquette.proto.DisconnectEncoder;
import org.dna.mqtt.moquette.proto.MQTTLoggingFilter;
import org.dna.mqtt.moquette.proto.PingReqDecoder;
import org.dna.mqtt.moquette.proto.PingRespEncoder;
import org.dna.mqtt.moquette.proto.PubAckDecoder;
import org.dna.mqtt.moquette.proto.PubAckEncoder;
import org.dna.mqtt.moquette.proto.PubCompDecoder;
import org.dna.mqtt.moquette.proto.PubCompEncoder;
import org.dna.mqtt.moquette.proto.PubCompMessage;
import org.dna.mqtt.moquette.proto.PubRecDecoder;
import org.dna.mqtt.moquette.proto.PubRecEncoder;
import org.dna.mqtt.moquette.proto.PubRelDecoder;
import org.dna.mqtt.moquette.proto.PubRelEncoder;
import org.dna.mqtt.moquette.proto.PublishDecoder;
import org.dna.mqtt.moquette.proto.PublishEncoder;
import org.dna.mqtt.moquette.proto.SubAckEncoder;
import org.dna.mqtt.moquette.proto.SubscribeDecoder;
import org.dna.mqtt.moquette.proto.UnsubAckEncoder;
import org.dna.mqtt.moquette.proto.UnsubscribeDecoder;
import org.dna.mqtt.moquette.proto.messages.ConnAckMessage;
import org.dna.mqtt.moquette.proto.messages.DisconnectMessage;
import org.dna.mqtt.moquette.proto.messages.PingRespMessage;
import org.dna.mqtt.moquette.proto.messages.PubAckMessage;
import org.dna.mqtt.moquette.proto.messages.PubRecMessage;
import org.dna.mqtt.moquette.proto.messages.PubRelMessage;
import org.dna.mqtt.moquette.proto.messages.PublishMessage;
import org.dna.mqtt.moquette.proto.messages.SubAckMessage;
import org.dna.mqtt.moquette.proto.messages.UnsubAckMessage;
import org.dna.mqtt.moquette.server.MQTTHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * 在MQTT moquette 中采用MINA作为底层消息的传递方式 
 * 
 * 本类的目的启动MQTT moquette Broker 的方式，
 *本文的源代码来自  moquette-broker-0.1-jar-with-dependencies.jar 中的server类
 * 如果想直接启动 moquette-broker-0.1-jar-with-dependencies.jar的jar文件方式
 * 可以执行一些命令实现 
 *        java -jar moquette-broker-0.1-jar-with-dependencies.jar
 * 
 * 
 * google code 下载MQTT moquette Broker 地址：
 *    http://code.google.com/p/moquette-mqtt/
 *    
 * GIT 下载MQTT moquette client 地址：
 *  https://github.com/fusesource/mqtt-client 
 *    
 * @author longgangbai
 * 
 * 
 */
public class MQTTBrokerProxyServer {
	  private static final Logger LOG = LoggerFactory.getLogger(MQTTBrokerProxyServer.class);

	  public static final String STORAGE_FILE_PATH = System.getProperty("user.name") + File.separator + "moquette_store.hawtdb";
	  private IoAcceptor m_acceptor;
	  SimpleMessaging messaging;

	  public static void main(String[] args)
	    throws IOException
	  {
	    new MQTTBrokerProxyServer().startServer();
	  }

	  protected void startServer() throws IOException
	  {
		//编码协议类编码器
	    DemuxingProtocolDecoder decoder = new DemuxingProtocolDecoder();
	    decoder.addMessageDecoder(new ConnectDecoder());//连接编码
	    decoder.addMessageDecoder(new PublishDecoder());//发布编码
	    decoder.addMessageDecoder(new PubAckDecoder());//发布回执编码
	    decoder.addMessageDecoder(new PubRelDecoder());
	    decoder.addMessageDecoder(new PubRecDecoder());//接收编码
	    decoder.addMessageDecoder(new PubCompDecoder());
	    decoder.addMessageDecoder(new SubscribeDecoder());//订阅编码
	    decoder.addMessageDecoder(new UnsubscribeDecoder());//取消订阅编码
	    decoder.addMessageDecoder(new DisconnectDecoder());//断开连接编码
	    decoder.addMessageDecoder(new PingReqDecoder());//心跳ping请求编码
	    
        //解码协议类解码器
	    DemuxingProtocolEncoder encoder = new DemuxingProtocolEncoder();

	    encoder.addMessageEncoder(ConnAckMessage.class, new ConnAckEncoder());//连接解码
	    encoder.addMessageEncoder(SubAckMessage.class, new SubAckEncoder());//订阅通知解码
	    encoder.addMessageEncoder(UnsubAckMessage.class, new UnsubAckEncoder());//取消订阅解码
	    encoder.addMessageEncoder(PubAckMessage.class, new PubAckEncoder());//发布回执解码
	    encoder.addMessageEncoder(PubRecMessage.class, new PubRecEncoder());//接收解码
	    encoder.addMessageEncoder(PubCompMessage.class, new PubCompEncoder());
	    encoder.addMessageEncoder(PubRelMessage.class, new PubRelEncoder());
	    encoder.addMessageEncoder(PublishMessage.class, new PublishEncoder());//发布解码
	    encoder.addMessageEncoder(PingRespMessage.class, new PingRespEncoder());//心跳ping相应解码
	    encoder.addMessageEncoder(DisconnectMessage.class,new DisconnectEncoder());//断开连接解码
	    
	    this.m_acceptor = new NioSocketAcceptor();
        //设置日志的过滤链
	    this.m_acceptor.getFilterChain().addLast("logger", new MQTTLoggingFilter("SERVER LOG"));
	    //设置编码的过滤链
	    this.m_acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(encoder, decoder));
        //创建业务处理器类
	    MQTTHandler handler = new MQTTHandler();
	    //创建一个处理消息体的消息
	    this.messaging = SimpleMessaging.getInstance();
	    this.messaging.init();
        //设置消息体
	    handler.setMessaging(this.messaging);
	    //设置业务处理器类
	    this.m_acceptor.setHandler(handler);
	    
	    ((NioSocketAcceptor)this.m_acceptor).setReuseAddress(true);
	    ((NioSocketAcceptor)this.m_acceptor).getSessionConfig().setReuseAddress(true);
	    this.m_acceptor.getSessionConfig().setReadBufferSize(2048);
	    this.m_acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
	    this.m_acceptor.getStatistics().setThroughputCalculationInterval(10);
	    this.m_acceptor.getStatistics().updateThroughput(System.currentTimeMillis());
	    //设置端口号
	    this.m_acceptor.bind(new InetSocketAddress(1883));
	    //获取绑定的本地的ip地址 
	    LOG.info("Server binded"+InetAddress.getLocalHost().getHostAddress());
	    try {
			Thread.sleep(100000000000000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//销毁broker对象的各种信息
	    Runtime.getRuntime().addShutdownHook(new Thread()
	    {
	      public void run() {
	    	  MQTTBrokerProxyServer.this.stopServer();
	      }
	    });
	  }

	  protected void stopServer() {
	    LOG.info("Server stopping...");
        
	    this.messaging.stop();
        //Mina  IO 统计类
	    IoServiceStatistics statistics = this.m_acceptor.getStatistics();
	    statistics.updateThroughput(System.currentTimeMillis());
	    System.out.println(String.format("Total read bytes: %d, read throughtput: %f (b/s)", new Object[] { Long.valueOf(statistics.getReadBytes()), Double.valueOf(statistics.getReadBytesThroughput()) }));
	    System.out.println(String.format("Total read msgs: %d, read msg throughtput: %f (msg/s)", new Object[] { Long.valueOf(statistics.getReadMessages()), Double.valueOf(statistics.getReadMessagesThroughput()) }));
        //关闭相关的会话
	    for (IoSession session : this.m_acceptor.getManagedSessions().values()) {
	      if ((session.isConnected()) && (!session.isClosing())) {
	        session.close(false);
	      }
	    }
        //销毁本地IoAcceptor对象
	    this.m_acceptor.unbind();
	    this.m_acceptor.dispose();
	    LOG.info("Server stopped");
	  }
	}
