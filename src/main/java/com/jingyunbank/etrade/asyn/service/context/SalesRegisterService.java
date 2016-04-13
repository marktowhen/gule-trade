package com.jingyunbank.etrade.asyn.service.context;

import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;
import com.jingyunbank.etrade.api.asyn.bo.AsynSchedule;
import com.jingyunbank.etrade.api.asyn.service.IAsynLogService;
import com.jingyunbank.etrade.api.asyn.service.IAsynParamService;
import com.jingyunbank.etrade.api.asyn.service.IAsynScheduleHistoryService;
import com.jingyunbank.etrade.api.asyn.service.IAsynScheduleService;
import com.jingyunbank.etrade.api.asyn.service.context.IAsynRunService;
import com.jingyunbank.etrade.api.award.bo.SalesUserrelationship;
import com.jingyunbank.etrade.api.award.service.ISalesUserrelationshipService;
import com.jingyunbank.etrade.asyn.util.HttpClientUtil;
import com.jingyunbank.etrade.config.PropsConfig;

@Service("salseRegesterService")
public class SalesRegisterService implements IAsynRunService {

	@Autowired
	private IAsynScheduleService asynScheduleService;
	@Autowired
	private IAsynParamService asynParamService;
	@Autowired
	private IAsynScheduleHistoryService asynScheduleHistoryService;
	@Autowired
	private IAsynLogService asynLogService;
	@Autowired
	private ISalesUserrelationshipService salesUserrelationshipService;
	
	private Logger logger = LoggerFactory.getLogger(SalesRegisterService.class);
	@Override
	@Transactional
	public void run(AsynSchedule schedule)  {
		System.out.println("salseRegesterService----------run");
		try {
			//处理中
			asynScheduleService.refreshStatus(schedule.getID(), AsynSchedule.PROCESSING);
			Map<String, String> params = asynParamService.getMap(schedule.getID());
			Result<String> result = handlerMessage(HttpClientUtil.doPost(PropsConfig.getString(PropsConfig.SALES_REGISTER_URL), params));	
			if(result.isOk()){
				//处理成功
				//将返回的三级分销ID记录
				SalesUserrelationship ship = new SalesUserrelationship();
				ship.setID(KeyGen.uuid());
				ship.setSID(new JSONObject(result.getBody()).getJSONObject("body").getString("id"));
				ship.setUID(params.get("uid"));
				salesUserrelationshipService.save(ship);
				schedule.setStatus(AsynSchedule.SUCCESS);
				asynScheduleService.remove(schedule.getID());
				asynScheduleHistoryService.saveFromAsynSchedule(schedule);
				asynLogService.save(schedule.getID(), AsynSchedule.SUCCESS, "");
			}
			//网络问题
			else if(HttpClientUtil.HTTP_ERROR.equals(result.getCode())){
				//失败
				asynScheduleService.refreshStatus(schedule.getID(), AsynSchedule.ERROR);
				asynLogService.save(schedule.getID(), AsynSchedule.ERROR, result.getMessage());
				logger.error("salseRegesterService:"+result.getMessage());
			}
			//逻辑问题 将任务移除避免重复处理
			else{
				schedule.setStatus(AsynSchedule.ERROR);
				asynScheduleService.remove(schedule.getID());
				asynScheduleHistoryService.saveFromAsynSchedule(schedule);
				asynLogService.save(schedule.getID(), AsynSchedule.ERROR, result.getMessage());
				logger.error("salseRegesterService:"+result.getMessage());
			}
			
		} catch (Exception e) {
			try {
				asynScheduleService.refreshStatus(schedule.getID(), AsynSchedule.ERROR);
				asynLogService.save(schedule.getID(), AsynSchedule.ERROR, e.getMessage());
			} catch (Exception e1) {
				logger.error("salseRegesterService:"+e1.getMessage());
			}
			logger.error("salseRegesterService:"+e.getMessage());
		}
	}
	
	private Result<String> handlerMessage(Map<String, String> resultMap){
		
		//http状态码
		if("200".equals(resultMap.get("statusCode"))){
			String result = resultMap.get("result");
			if(null != result && !"".equals(result)){
				JSONObject json = new JSONObject(result);
				if("200".equals(json.get("code"))){
					//处理成功
					return Result.ok(result);
				}else{
					//失败
					return Result.fail(json.getString("message"));
				}
			}else{
				//失败
				return Result.fail("数据格式错误");
			}
		}else{
			//失败
			Result<String> r = Result.fail(resultMap.get("errorMessage"));
			r.setCode(HttpClientUtil.HTTP_ERROR);
			return r;
		}
	}
	

}
