package com.base.sbc.client.message.utils;

import com.base.sbc.client.message.entity.ModelMessage;
import com.base.sbc.client.message.service.MessagesService;
import com.base.sbc.config.common.IdGen;
import com.base.sbc.config.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


/***
 * 向消息服务器发送消息的工具类
 * @author youkehai
 *
 */
@Component
public class MessageUtils {

	@Autowired
	private MessagesService messagesService;
	

	/**
     * 给需要审核的人发送信息
	 * @param msg
	 */
	public void sendAuditMessage(ModelMessage msg) {
		msg.setModelCode("SJ502");
		messagesService.sendNoticeByModel(msg);
	}
}
