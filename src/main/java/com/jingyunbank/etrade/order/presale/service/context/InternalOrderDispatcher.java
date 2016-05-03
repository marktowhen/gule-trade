package com.jingyunbank.etrade.order.presale.service.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingyunbank.core.Result;
import com.jingyunbank.core.util.Strings;
import com.jingyunbank.etrade.api.order.presale.bo.Orders;
import com.jingyunbank.etrade.api.order.presale.service.context.IOrderDispatcher;
import com.jingyunbank.etrade.api.order.presale.service.context.IOrderStatusHandler;

@Service("InternalOrderDispatcher")
public class InternalOrderDispatcher implements IOrderDispatcher {

	@Autowired
	private Map<String, IOrderStatusHandler> handlers = new HashMap<String, IOrderStatusHandler>();
	
	@Override
	public List<Result<String>> dispatch(List<Orders> orders) throws Exception {
		Map<String, List<Orders>> grouporders = new HashMap<String, List<Orders>>();
		List<Result<String>> result = new ArrayList<Result<String>>();
		for (Orders order : orders) {
			if(Strings.empty(order.getStatusCode())){
				System.out.println(order +" has no status, skipped.");
				continue;
			}
			List<Orders> storedorders = grouporders.get(order.getStatusCode().toUpperCase());
			if(Objects.isNull(storedorders)){
				storedorders = new ArrayList<Orders>();
				grouporders.put(order.getStatusCode().toUpperCase(), storedorders);
			}
			storedorders.add(order);
		}
		for (Map.Entry<String, List<Orders>> entry : grouporders.entrySet()) {
			IOrderStatusHandler handler = handlers.get(entry.getKey());
			if(handler == null){
				System.out.println(entry.getValue() +" has no correct status, skipped.");
				continue;
			}
			result.add(handler.handle(entry.getValue()));
		}
		return result;
	}
}
