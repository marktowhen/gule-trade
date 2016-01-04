package com.jingyunbank.etrade.order.presale.service.context;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.etrade.api.exception.DataRefreshingException;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.exception.NoticeDispatchException;
import com.jingyunbank.etrade.api.message.bo.Message;
import com.jingyunbank.etrade.api.message.service.context.IAsyncNotifyService;
import com.jingyunbank.etrade.api.order.presale.bo.OrderStatusDesc;
import com.jingyunbank.etrade.api.order.presale.bo.Orders;
import com.jingyunbank.etrade.api.order.presale.service.IOrderService;
import com.jingyunbank.etrade.api.order.presale.service.context.IOrderContextService;
import com.jingyunbank.etrade.api.order.presale.service.context.IOrderRobotService;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.user.service.IUserService;

@Service("orderRobotService")
public class OrderRobotService implements IOrderRobotService {

	//支付超时期限24hr
	public static final long UNPAID_EXPIRE_DURATION_IN_SECOND = 24 * 60 * 60;//24hr
	//确认收货超时期限10d
	public static final long UNRECEIVED_EXPIRE_DURATION_IN_SECOND = 10 *  24 * 60 * 60;//10d
	//确认收货超时提醒期限9d
	public static final long UNRECEIVED_REMINDER_DURATION_IN_SECOND = 9 *  24 * 60 * 60;//9d
	
	@Autowired
	private IOrderContextService orderContextService;
	@Autowired
	private IOrderService orderService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IAsyncNotifyService asyncNotifyService;
	
	@Override
	@Transactional
	public void closeUnpaid() throws DataRefreshingException, DataSavingException {
		System.out.println("===================未支付订单超时关闭任务启动======================");
		Instant now = Instant.now();
		Instant deadline = now.minusSeconds(UNPAID_EXPIRE_DURATION_IN_SECOND);
		List<Orders> unpaid = orderService.listBefore(Date.from(deadline), OrderStatusDesc.NEW);
		List<String> oids = unpaid.stream().map(x->x.getID()).collect(Collectors.toList());
		System.out.println("待关闭订单："+oids);
		if(oids.size() > 0) orderContextService.cancel(oids, "超时关闭");
		System.out.println("===================未支付订单超时关闭任务完成======================");
	}

	@Override
	@Transactional
	public void autoReceive() throws DataRefreshingException, DataSavingException {
		System.out.println("===================未收货订单超时关闭任务启动======================");
		Instant now = Instant.now();
		Instant deadline = now.minusSeconds(UNRECEIVED_EXPIRE_DURATION_IN_SECOND);
		List<Orders> unpaid = orderService.listBefore(Date.from(deadline), OrderStatusDesc.DELIVERED);
		List<String> oids = unpaid.stream().map(x->x.getID()).collect(Collectors.toList());
		System.out.println("待收货订单："+oids);
		if(oids.size() > 0) orderContextService.cancel(oids, "超时自动确认收货");
		System.out.println("===================未收货订单超时关闭任务完成======================");
	}

	@Override
	public void remindReceive() throws NoticeDispatchException {
		System.out.println("===================未收货订单超时关闭提醒任务启动======================");
		
		Instant now = Instant.now();
		Instant from = now.minusSeconds(UNRECEIVED_REMINDER_DURATION_IN_SECOND);
		Instant to = from.plusSeconds(30 * 60);//from = 9d, to = 9d+30minuts
		List<Orders> unpaid = orderService.listBetween(Date.from(from), Date.from(to), OrderStatusDesc.DELIVERED);
		if(unpaid.size() > 0){
			List<String> uids = unpaid.stream().map(x->x.getUID()).collect(Collectors.toList());
			List<Users> users = userService.list(uids);
			System.out.println("待收货提醒用户："+uids);
			if(users.size() > 0){
				for (Users user : users) {
					Message msg = new Message();
					msg.setTitle("确认收货提醒");
					msg.setID(KeyGen.uuid());
					msg.setContent("您好，您有订单需要您确认收货，如24小时候内未操作，则系统会执行自动收货，请登录网站查询详情。");
					msg.setReceiveUser(user);
					asyncNotifyService.dispatch(msg);
				}
			}
		}
		System.out.println("===================未收货订单超时关闭提醒任务完成======================");
	}

}
