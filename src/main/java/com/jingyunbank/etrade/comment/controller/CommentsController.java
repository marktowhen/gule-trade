package com.jingyunbank.etrade.comment.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Range;
import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.core.web.ServletBox;
import com.jingyunbank.etrade.api.comment.bo.Comments;
import com.jingyunbank.etrade.api.comment.bo.CommentsImg;
import com.jingyunbank.etrade.api.comment.service.ICommentImgService;
import com.jingyunbank.etrade.api.comment.service.ICommentService;
import com.jingyunbank.etrade.api.order.bo.OrderGoods;
import com.jingyunbank.etrade.api.order.bo.OrderStatusDesc;
import com.jingyunbank.etrade.api.order.service.IOrderGoodsService;
import com.jingyunbank.etrade.api.order.service.IOrderService;
import com.jingyunbank.etrade.api.user.bo.UserInfo;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.user.service.IUserInfoService;
import com.jingyunbank.etrade.api.user.service.IUserService;
import com.jingyunbank.etrade.comment.bean.CommentsImgVO;
import com.jingyunbank.etrade.comment.bean.CommentsVO;
import com.jingyunbank.etrade.user.bean.UserInfoVO;
import com.jingyunbank.etrade.user.bean.UserVO;


@RestController
public class CommentsController {
	@Autowired
	private ICommentService commentService;
	@Autowired
	private ICommentImgService commentImgService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IUserInfoService userInfoService;
	@Autowired
	private IOrderGoodsService orderGoodsService;
	@Autowired
	private IOrderService orderService;
	/**
	 * 保存商品的评论信息和对应的多张图片
	 * @param commentVO
	 * @param commentsImgVO
	 * @param request
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/api/comments",method=RequestMethod.POST)
	@ResponseBody
	public Result saveComments(@RequestParam("oid") String oid,@RequestBody CommentsVO commentVO,CommentsImgVO commentsImgVO,HttpServletRequest request,HttpSession session) throws Exception{
		commentVO.setID(KeyGen.uuid());
		Optional<OrderGoods> optional	=orderGoodsService.singleOrderGoods(oid);
		OrderGoods	orderGoods =optional.get();
		commentVO.setGID(orderGoods.getGID());
		commentVO.setOID(orderGoods.getOID());
		String id = ServletBox.getLoginUID(request);
		commentVO.setUID(id);
		commentVO.setAddtime(new Date());
		commentVO.setCommentStatus(2);
		Comments comments=new Comments();
		BeanUtils.copyProperties(commentVO, comments);

		if(commentService.save(comments)){
			//对保存多张图片的过程！模拟写的！有多张图片的保存
			/*for (int i=0;i<commentVO.getPicture().size();i++){*/
				CommentsImg commentsImg=new CommentsImg();
				commentsImg.setID(KeyGen.uuid());
				commentsImg.setPicture(commentVO.getImgPath());
				commentsImg.setCommentID(commentVO.getID());;
				
				commentImgService.save(commentsImg);
			/*	}*/
				//修改订单商品的状态
				orderGoodsService.refreshGoodStatus(oid, OrderStatusDesc.COMMENTED);
			//修改订单的状态
			if(orderGoodsService.getByOID(orderGoods.getOID(), OrderStatusDesc.RECEIVED)==0){
				List<String> oids=new ArrayList<String>();
				oids.add(orderGoods.getOID());
				orderService.refreshStatus(oids, OrderStatusDesc.COMMENTED);
			}
			return Result.ok("保存成功");
			}
		return Result.fail("保存失败！");
	}
	
	
	/**
	 * 测试通过产品的id查出所有的评论信息，得到评论信息的级别(好评，中评，差评)
	 * @param gid
	 * @param commentGrade
	 * @param request
	 * @param session
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value="/api/comments/grades",method=RequestMethod.GET)
	@ResponseBody
	public Result<List<CommentsVO>> getGradeComments(@RequestParam("gid") String gid,@RequestParam("commentGrade") int commentGrade,@RequestParam("from") int from,@RequestParam("size") int size,HttpServletRequest request,HttpSession session) throws Exception{
		Range range = new Range();
		range.setFrom(from);
		range.setTo(from+size);
		List<Comments> comments=commentService.selectCommentGradeByGid(gid,commentGrade,range);
		List<CommentsVO> commentVOs=convert(comments);
		return Result.ok(commentVOs);
		
		}
	
	private List<CommentsVO> convert(List<Comments> comments){
	
		List<CommentsVO> commentVOs=new ArrayList<CommentsVO>();
		for(int i=0;i<comments.size();i++){
			CommentsVO commentsVO=new CommentsVO();
			UserVO userVO = new UserVO();
			UserInfoVO userinfoVO = new UserInfoVO();
			Users users=userService.getByUID(comments.get(i).getUID()).get();
			UserInfo userInfo=userInfoService.getByUid(comments.get(i).getUID()).get();
			BeanUtils.copyProperties(comments.get(i),commentsVO);
			BeanUtils.copyProperties(users, userVO);
			BeanUtils.copyProperties(userInfo, userinfoVO);
			commentsVO.setUserVO(userVO);
			commentsVO.setUserInfoVO(userinfoVO);
			List<CommentsImg> commentsImgs=	commentImgService.getById(comments.get(i).getID());
			commentsVO.setImgs(commentsImgs);
			commentVOs.add(commentsVO);
		}
		return commentVOs;
	}
	
	/**
	 * 通过gid查出所有的图片信息
	 * @param gid
	 * @param request
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/api/commentImgs/getbygid",method=RequestMethod.GET)
	@ResponseBody
	public Result<List<CommentsVO>> getCommentImgs(@RequestParam("gid") String gid,HttpServletRequest request,HttpSession session) throws Exception{
		List<Comments> comments=commentService.getCommentsByGid(gid);
		List<CommentsVO> commentVOs=convertImg(comments);
		return Result.ok(commentVOs);
		
		}
	private List<CommentsVO> convertImg(List<Comments> comments){
	
		List<CommentsVO> commentVOs=new ArrayList<CommentsVO>();
		for(int i=0;i<comments.size();i++){
			CommentsVO commentsVO=new CommentsVO();
			BeanUtils.copyProperties(comments.get(i),commentsVO);
			List<CommentsImg> commentsImgs=	commentImgService.getById(comments.get(i).getID());
			commentsVO.setImgs(commentsImgs);
			/*for(int j=0;j<commentsImgs.size();j++){
				CommentsImgVO vo = new CommentsImgVO();
				BeanUtils.copyProperties(commentsImgs.get(j), vo);
			}*/
			commentVOs.add(commentsVO);
		}
		return commentVOs;
	}
	/**
	 * 测试通过id删除自己添加的商品评价，别人的评论没有权限删除
	 * @param id
	 * @param request
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/api/comments/delete/{id}",method=RequestMethod.DELETE)
	@ResponseBody
	public Result remove(@PathVariable String id,HttpServletRequest request,HttpSession session) throws Exception{
		String uid = ServletBox.getLoginUID(request);
		Optional<Comments> comments=commentService.getById(id);
		CommentsVO commentsVO=new CommentsVO();
		BeanUtils.copyProperties(comments.get(), commentsVO);
		if(commentsVO.getUID().equals(uid)){
			commentImgService.remove(commentsVO.getID());
			commentService.remove(id);
			return Result.ok("删除成功");
		}
		return Result.fail("没有删除的权限");
	}
	/**
	 * 修改评论的状态
	 * @param commentsVO
	 * @param request
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/api/comments/update/status",method=RequestMethod.POST)
	@ResponseBody
	public Result<CommentsVO> updateStatus(CommentsVO commentsVO,HttpServletRequest request,HttpSession session) throws Exception{
		
		Optional<Comments> optionComments=commentService.getById(commentsVO.getID());
		Comments comments=	optionComments.get();
	
		if(!StringUtils.isEmpty(comments.getGoodsComment())){
			//如果已经评价就把设置为2
			
			comments.setCommentStatus(2);
			commentService.refreshStatus(comments);
			
			BeanUtils.copyProperties(comments, commentsVO);
			return Result.ok(commentsVO);
		}
		return Result.fail("请重试");
		
	}
	/**
	 * 通过gid查出评论商品的总级别
	 * @param gid
	 * @return
	 */
	@RequestMapping(value="/api/comments/goods/grade",method=RequestMethod.GET)
	public Result<CommentsVO> getGoodsGrade(@RequestParam(value="gid") String gid){
		int gradeCount=0;
		float zongjibie =0;
		int personCount=0;
		List<Comments> comments=commentService.getCommentsByGid(gid);
		if(comments.size()==0){
			zongjibie=0;
		}
		personCount=commentService.commentCount(gid);
		if(personCount==0){
			personCount=0;
		}
		for(int i=0;i<comments.size();i++){
			gradeCount+=comments.get(i).getCommentGrade();
		}
		zongjibie=gradeCount/personCount;
		CommentsVO commentsVO=new CommentsVO();
		int allLevel=(int)zongjibie*10;
		commentsVO.setAllLevel(allLevel);
		commentsVO.setPersonCount(personCount);
		commentsVO.setZongjibie(zongjibie);
		return Result.ok(commentsVO);	
		
	}
	/**
	 * 通过gid查出评论商品，评论服务，评论物流结合的总级别
	 * @param gid
	 * @return
	 */
	@RequestMapping(value="/api/comments/overall/grade",method=RequestMethod.GET)
	public Result<CommentsVO> getGrade(@RequestParam(value="gid") String gid){
		int goodsCount=0;
		int serviceCount=0;
		int logisticsCount=0;
		float level = 0;
		int personCount=0;
		
		List<Comments> comments=commentService.getCommentsByGid(gid);
		if(comments.size()==0){
			level=0;
		}
		personCount=commentService.commentCount(gid);
		if(personCount==0){
			personCount=0;
		}
		for (int i=0;i<comments.size();i++) {
			goodsCount+=comments.get(i).getCommentGrade();
			serviceCount+=comments.get(i).getServiceGrade();
			logisticsCount+=comments.get(i).getLogisticsGrade();	
		}
		level=(goodsCount+serviceCount+logisticsCount)/3/personCount;
		int levelGrade=(int)level*10;
		CommentsVO commentsVO=new CommentsVO();
		commentsVO.setLevel(level);
		commentsVO.setPersonCount(personCount);
		commentsVO.setLevelGrade(levelGrade);
		return Result.ok(commentsVO);	
		
	}
}
