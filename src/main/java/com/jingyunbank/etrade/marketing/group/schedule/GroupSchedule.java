package com.jingyunbank.etrade.marketing.group.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class GroupSchedule {
	
	@Scheduled(fixedDelay=(1*60*1000))//1 minute
	public void check(){
		
	}
	
	/*Optional<Group> group = groupService.single(gu.get().getGroupID());
	if(GroupOrder.TYPE_DEPOSIT.equals(go.get().getType()) ){
		//建团成功 开始召集
		if(group.get().getLeaderUID().equals( gu.get().getUID())
				//组团成功
				||groupService.full(group.get().getID())){
			Optional<FlowStatus> groupFlowStatus = flowStatusService.single(Group.FLOW_TYPE, group.get().getStatus(), order.getStatusCode());
			if(groupFlowStatus.isPresent()){
				groupService.refreshStatus(group.get().getID(), groupFlowStatus.get().getNextStatus());
			}
		}
	}*/
	
	//到期未组团成功的
	
	//定金逾期未支付
	
	//尾款逾期未支付
	
	

}
