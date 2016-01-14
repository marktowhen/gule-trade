package com.jingyunbank.etrade.order.presale.service.context;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.jms.JMSException;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.ReferenceType;
import com.jingyunbank.core.KeyGen;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.goods.service.IGoodsOperationService;
import com.jingyunbank.etrade.api.message.bo.Message;
import com.jingyunbank.etrade.api.message.service.context.ISyncNotifyService;
import com.jingyunbank.etrade.api.order.presale.bo.OrderGoods;
import com.jingyunbank.etrade.api.order.presale.bo.Orders;
import com.jingyunbank.etrade.api.order.presale.service.context.IOrderEventService;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.user.service.IUserService;
import com.jingyunbank.etrade.api.vip.point.service.context.IPointContextService;

@Service("orderEventService")
public class OrderEventService implements IOrderEventService {

	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	private List<ISyncNotifyService> syncNotifyService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IPointContextService pointContextService;
	@Autowired
	private IGoodsOperationService goodsOperationService;
	
	@Override
	public void broadcast(List<Orders> event, String queue){
		jmsTemplate.send(queue, new MessageCreator() {
			@Override
			public javax.jms.Message createMessage(Session session) throws JMSException {
				try {
					return session.createTextMessage(new ObjectMapper().writeValueAsString(event));
				} catch (JsonProcessingException e) {
					logger.error("将支付成功的订单信息放入队列进行广播失败：" + e.toString());
				}
				return session.createTextMessage();
			}
		});
	}
	//计算积分
	@JmsListener(destination=MQ_ORDER_QUEUE_PAYSUCC)
	public void calculatePoint(String content){
		List<Orders> orders = convert(content);
		try {
			pointContextService.addPoint(orders.get(0).getUID(), 
					orders.stream().map(x->x.getPayout())
						.reduce(BigDecimal.ZERO, (x,y)->x.add(y)).divide(BigDecimal.valueOf(100)).intValue(), "消费赚取积分");
		} catch (Exception e) {
			logger.error("计算消费积分失败：" + e.toString());
		}
	}
	//发送消息提醒。
	@JmsListener(destination=MQ_ORDER_QUEUE_PAYSUCC)
	public void paysuccnotify(String content){
		List<Orders> orders = convert(content);
		
    	Optional<Users> ucandidate = userService.single(orders.get(0).getUID());
    	syncNotifyService.forEach(service->{
    		try {
    			com.jingyunbank.etrade.api.message.bo.Message imsg = new com.jingyunbank.etrade.api.message.bo.Message();
    			imsg.setTitle("支付成功提醒");
    			imsg.setID(KeyGen.uuid());
    			imsg.setContent("您好，您有一笔订单支付成功，如有疑问，请登录网站查询详情。");
    			imsg.setReceiveUser(ucandidate.get());
    			imsg.setReceiveUID(ucandidate.get().getID());
    			imsg.setType(Message.TYPE_LETTER);
    			imsg.setStatus(Message.STATUS_SUC);
				service.inform(imsg);
			} catch (Exception e) {
				logger.error("发送支付成功提醒失败：" + e.toString());
			}
    	});
	}
	//更新库存
	@JmsListener(destination=MQ_ORDER_QUEUE_PAYSUCC)
	public void payupdatestock(String content){
		List<Orders> orders = convert(content);
		
    	Orders o = orders.get(0);
		String uid = o.getUID();
		String uname = o.getUname();
		List<OrderGoods> goods = new ArrayList<OrderGoods>();
		for (Orders order : orders) {
			goods.addAll(order.getGoods());
		}
		//更新库存
		for (OrderGoods gs : goods) {
			try {
				goodsOperationService.refreshGoodsVolume(uid, uname, gs.getGID(), gs.getCount());
			} catch (DataSavingException | DataRefreshingException e) {
				logger.error("更新库存失败：" + e.toString());
			}
		}
	}

	private List<Orders> convert(String content) {
		List<Orders> orders = new ArrayList<Orders>();
		try {
			orders = new ObjectMapper().readValue(content, CollectionType.construct(List.class, ReferenceType.construct(Orders.class)));
		} catch (IOException e) {
			logger.error("读取队列中的订单列表对象进行转换时出错：" + e.toString());
		}
		return orders;
	}
	
	private Logger logger = LoggerFactory.getLogger(OrderEventService.class);

}
