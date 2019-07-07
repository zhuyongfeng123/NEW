package org.fkjava.weixin.processors.impl;

import org.fkjava.commons.domain.User;
import org.fkjava.commons.domain.event.EventInMessage;
import org.fkjava.commons.processors.EventMessageProcessor;
import org.fkjava.commons.repository.UserRepository;
import org.fkjava.commons.service.WeiXinProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("subscribeMessageProcessor")
public class SubscribeEventMessageProcessor implements EventMessageProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(SubscribeEventMessageProcessor.class);

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private WeiXinProxy proxy;

	@Override
	public void onMessage(EventInMessage event) {
		LOG.trace("关注事件处理器被调用，收到的消息:\n {} ", event);

		if (!event.getEvent().equals("subscribe")) {
			return;
		}

		
		String openId = event.getFromUserName();
		User user = this.userRepository.findByOpenId(openId);

	
		if (user != null) {
			if (user.getStatus() == User.Status.IS_SUBSCRIBED) {
				return;
			}

		}

		User wxUser = this.proxy.getWxUser(openId);// 调用远程接口获取用户信息
		if (user != null) {
			wxUser.setId(user.getId());
		}
		wxUser.setStatus(User.Status.IS_SUBSCRIBED);
		this.userRepository.save(wxUser);

		this.proxy.sendText(openId, "欢迎关注公众号，回复\"学习\"可以获得智能菜单。");
	}
}
