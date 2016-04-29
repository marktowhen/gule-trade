package com.jingyunbank.etrade.marketing.group.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jingyunbank.etrade.api.marketing.group.service.context.IGroupRobotService;

@Component
public class GroupSchedule {
	
	@Autowired
	private IGroupRobotService groupRobotService;
	//团长未支付成功 组团失败
	@Scheduled(fixedDelay=(60*1000))//
	public void closeUnstartGroup() {
		groupRobotService.closeUnstartGroup();
	}
	
	//团长支付成功 开团成功
//	@Scheduled(fixedDelay=(60*1000))//
//	public void startSuccess() {
//		groupRobotService.startSuccess();
//	}
	
	//指定时间段内未组团成功的 退还押金
	@Scheduled(fixedDelay=(60*1000))//
	public void closeConveneFailGroup(){
		groupRobotService.closeConveneFailGroup();
	}
	
	//逾期未支付 退团
	@Scheduled(fixedDelay=(60*1000))//
	public void payTimeout() {
		groupRobotService.payTimeout();
	}
	
	//团购到期 满团组团成功  没满团解散
	@Scheduled(fixedDelay=(60*1000))//
	public void expire() {
		groupRobotService.expire();
	}
	
	//组团成功的
	@Scheduled(fixedDelay=(60*1000))//
	public void finish() {
		groupRobotService.finish();
	}
	
	//人数不足的快到期时提醒
	
	//到货之后计算团长佣金
	
}
