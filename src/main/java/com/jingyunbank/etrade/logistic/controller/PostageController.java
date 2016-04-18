package com.jingyunbank.etrade.logistic.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.Login;
import com.jingyunbank.etrade.api.logistic.bo.Postage;
import com.jingyunbank.etrade.api.logistic.bo.PostageDetail;
import com.jingyunbank.etrade.api.logistic.service.IPostageDetailService;
import com.jingyunbank.etrade.api.logistic.service.IPostageService;
import com.jingyunbank.etrade.api.logistic.service.context.IPostageCalculateService;
import com.jingyunbank.etrade.api.logistic.service.context.IPostageManageService;
import com.jingyunbank.etrade.logistic.bean.PostageDetailVO;
import com.jingyunbank.etrade.logistic.bean.PostageVO;

@RestController
@RequestMapping(value="/api/logistic/postage")
public class PostageController {

	@Autowired
	private IPostageCalculateService postageCalculateService;
	@Autowired
	private IPostageManageService postageManageService;
	@Autowired
	private IPostageService postageService;
	@Autowired
	private IPostageDetailService postageDetailService;
	
	
//	@RequestMapping(value="/api/logistic/postage/calculation", method=RequestMethod.PUT)
//	public Result<BigDecimal> calculate(@RequestBody @Valid List<PostageCalculateVO> postages, BindingResult valid ) throws Exception{
//		if(valid.hasErrors()){
//			return Result.fail("您提交的数据有误，请核实后重新提交。");
//		}
//		if(postages.isEmpty()){
//			return Result.ok(BigDecimal.ZERO);
//		}
//		List<PostageCalculate> postagebo = postages.stream().map(vo -> {
//			PostageCalculate bo = new PostageCalculate();
//			BeanUtils.copyProperties(vo, bo);
//			return bo;
//		}).collect(Collectors.toList());
//		return Result.ok(postageCalculateService.calculateMuti(postagebo, postagebo.get(0).getCity()));
//	}
	
	@RequestMapping(value="/", method=RequestMethod.POST,
			consumes="application/json;charset=UTF-8")
	public Result<String> save(@RequestBody @Valid PostageVO postageVO , BindingResult valid ,HttpSession session) throws Exception{
		if(valid.hasErrors()){
			return Result.fail("您提交的数据有误，请核实后重新提交。");
		}
		Postage postage = new Postage();
		BeanUtils.copyProperties(postageVO, postage);
		postage.setID(KeyGen.uuid());
		postage.setMID(Login.MID(session));
		postage.setValid(true);
		List<PostageDetail> postageDetailList = postageVO.getPostageDetailList().stream().map( vo->{
			PostageDetail bo = new PostageDetail();
			BeanUtils.copyProperties(vo, bo);
			bo.setID(KeyGen.uuid());
			bo.setPostageID(postage.getID());
			bo.setValid(true);
			return bo;
		}).collect(Collectors.toList());
		
		postageManageService.save(postage, postageDetailList);
		return Result.ok();
		
	}	
	
	@RequestMapping(value="/{ID}", method=RequestMethod.PUT)
	public Result<String> refresh(@RequestBody @Valid PostageVO postageVO ,@PathVariable String ID, BindingResult valid ) throws Exception{
		if(valid.hasErrors()){
			return Result.fail("您提交的数据有误，请核实后重新提交。");
		}
		Postage postage = new Postage();
		BeanUtils.copyProperties(postageVO, postage);
		postage.setID(ID);
		
		List<PostageDetail> postageDetailList = postageVO.getPostageDetailList().stream().map( vo->{
			PostageDetail bo = new PostageDetail();
			BeanUtils.copyProperties(vo, bo);
			return bo;
		}).collect(Collectors.toList());
		
		postageManageService.refresh(postage, postageDetailList);
		return Result.ok();
		
	}	
	
	/**
	 * 查詢店鋪运费模板
	 * @return
	 * @throws Exception
	 * 2016年4月14日 qxs
	 */
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public Result<List<PostageVO>> list(HttpSession  session) throws Exception{
		
		return Result.ok(postageService.list(Login.MID(session)).stream().map( postage->{
			return getPostageVOByBo(postage);
		}).collect(Collectors.toList()));
	}
	
	/**
	 * 查詢店鋪运费模板及模板详情
	 * @return
	 * @throws Exception
	 * 2016年4月14日 qxs
	 */
	@RequestMapping(value="/list/detail", method=RequestMethod.GET)
	public Result<List<PostageVO>> listWithDetail(HttpSession  session  ) throws Exception{
		
		return Result.ok(postageManageService.listOneShopWithDetail(Login.MID(session)).stream().map( postage->{
					return getPostageVOByBo(postage);
				}).collect(Collectors.toList()));
		
	}	
	
	@RequestMapping(value="/{ID}", method=RequestMethod.GET)
	public Result<PostageVO> single(@PathVariable String ID ) throws Exception{
		Postage postage = postageService.single(ID);
		if(postage!=null){
			postage.setPostageDetailList(postageDetailService.list(ID));
			return Result.ok(getPostageVOByBo(postage));
		}
		return Result.fail("未找到");
		
	}	
	
	@RequestMapping(value="/{ID}", method=RequestMethod.DELETE)
	public Result<String> remove(@PathVariable String ID ) throws Exception{
		postageManageService.remove(ID);
		return Result.ok();
		
	}
	
	private PostageVO getPostageVOByBo(Postage bo){
		PostageVO postageVO = new PostageVO();
		BeanUtils.copyProperties(bo, postageVO);
		if(bo.getPostageDetailList()!=null){
			postageVO.setPostageDetailList(bo.getPostageDetailList().stream().map( detail->{
				PostageDetailVO detailVO = new PostageDetailVO();
				BeanUtils.copyProperties(detail, postageVO);
				return detailVO;
			}).collect(Collectors.toList()));
		}
		return postageVO;
	}
	
	
	
}
