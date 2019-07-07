package org.fkjava.commons;

import org.fkjava.commons.domain.AccessToken;
import org.fkjava.commons.domain.InMessage;
import org.fkjava.commons.domain.event.EventInMessage;
import org.fkjava.commons.service.JsonRedisSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

public interface EventListenerConfig extends CommandLineRunner, DisposableBean {

	public static final Logger LOG = LoggerFactory.getLogger(EventListenerConfig.class);
	public final Object runnerMonitor = new Object();
	@Override
	public default void run(String... args) throws Exception {
	Thread t = new Thread(() -> {
	synchronized (runnerMonitor) {
	try {
	runnerMonitor.wait();
	} catch (InterruptedException e) {
	LOG.error("运行时监视器出现问题：" + e.getLocalizedMessage(), e);
	}}});
	t.start();
	}
@Override
	public default void destroy() throws Exception {
	synchronized (runnerMonitor) {
	runnerMonitor.notify();
	}
	}
	@Bean 
	public default RedisTemplate<String, ? extends InMessage> inMessageTemplate(//
	@Autowired RedisConnectionFactory connectionFactory) {
	RedisTemplate<String, ? extends InMessage> template = new RedisTemplate<>();
	template.setConnectionFactory(connectionFactory);
	template.setValueSerializer(jsonRedisSerializer());
	return template;}
	@Bean
	public default RedisTemplate<String, AccessToken> accessTokenTemplate(//
	@Autowired RedisConnectionFactory connectionFactory) {
	RedisTemplate<String, AccessToken> template = new RedisTemplate<>();
	template.setConnectionFactory(connectionFactory);
	template.setValueSerializer(jsonRedisSerializer());
	return template;}
	@Bean
	public default JsonRedisSerializer<InMessage> jsonRedisSerializer() {
	return new JsonRedisSerializer<InMessage>();}
	@Bean
	public default RedisMessageListenerContainer messageListenerContainer(//
	@Autowired RedisConnectionFactory connectionFactory) {
	RedisMessageListenerContainer c = new RedisMessageListenerContainer();
	c.setConnectionFactory(connectionFactory);
	ChannelTopic topic = new ChannelTopic("kemao_3_event");
	MessageListener listener = (message, pattern) -> {
	byte[] body = message.getBody();
	JsonRedisSerializer<InMessage> serializer = jsonRedisSerializer();
	InMessage msg = serializer.deserialize(body);
	EventInMessage event = (EventInMessage) msg;
	handleEvent(event);
//	LOG.trace("事件处理程序收到的消息：{}", msg);
//	String eventType = event.getEvent();// 获取事件类型
//			eventType = eventType.toLowerCase();// 转换为小写
//
//			// 调用消息的处理器，进行具体的消息处理
//			String beanName = eventType + "MessageProcessor";
//			EventMessageProcessor mp = (EventMessageProcessor) ctx.getBean(beanName);
//			if (mp == null) {
//				LOG.error("事件 {} 没有找到对应的处理器", eventType);
//			} else {
//				mp.onMessage(event);
//			}
		};
	c.addMessageListener(listener, topic);
	return c;}
	public void handleEvent(EventInMessage event);}
