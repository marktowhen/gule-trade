package com.jingyunbank.etrade.order.postsale.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.Range;
import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.etrade.api.order.postsale.bo.Refund;
import com.jingyunbank.etrade.api.order.postsale.service.IRefundService;
import com.jingyunbank.etrade.order.postsale.bean.Refund2ShowVO;

@RestController
public class RefundQueryController {

	@Autowired
	private IRefundService refundService;
	
	/**
	 * get /api/refund/xxxx/0/10?keywords=东阿&status=PAID&fromdate=2015-11-09
	 *	
	 * 查询某用户的退单的从from开始的size条
	 * @param uid
	 * @param session
	 * @return
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/api/refund/{uid}/{from}/{size}", method=RequestMethod.GET)
	public Result<List<Refund2ShowVO>> listUID(
			@PathVariable("uid") String uid, 
			@PathVariable("from") int from, 
			@PathVariable("size") int size,
			@RequestParam(value="keywords", required=false, defaultValue="") String keywords,
			@RequestParam(value="status", required=false, defaultValue="") String statuscode,
			@RequestParam(value="fromdate", required=false, defaultValue="1970-01-01") String fromdate,
			HttpSession session){
		
		return Result.ok(refundService.list(uid, statuscode, fromdate, keywords, new Range(from, size+from))
				.stream().map(bo-> {
					Refund2ShowVO vo = new Refund2ShowVO();
					BeanUtils.copyProperties(bo, vo, "certificates");
					vo.setOrderno(String.valueOf(bo.getOrderno()));
					return vo;
				}).collect(Collectors.toList()));
	}
	
	@AuthBeforeOperation
	@RequestMapping(value="/api/refund/{rid}", method=RequestMethod.GET)
	public Result<Refund2ShowVO> single (@PathVariable String rid) throws Exception{
		Optional<Refund> candidate = refundService.single(rid);
		if(candidate.isPresent()){
			Refund refund = candidate.get();
			Refund2ShowVO vo = new Refund2ShowVO();
			BeanUtils.copyProperties(refund, vo);
			return Result.ok(vo);
		}
		return Result.fail("无记录");
	}
	/**
	 * get /api/refund?ogid=azxcvzxvadzxcvqwer
	 * @param ogid
	 * @return
	 * @throws Exception
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/api/refund", method=RequestMethod.GET)
	public Result<Refund2ShowVO> queryWithOgid (@RequestParam(required=true) String ogid) throws Exception{
		Optional<Refund> candidate = refundService.singleByOGID(ogid);
		if(candidate.isPresent()){
			Refund refund = candidate.get();
			Refund2ShowVO vo = new Refund2ShowVO();
			BeanUtils.copyProperties(refund, vo);
			return Result.ok(vo);
		}
		return Result.fail("无记录");
	}
	
}
