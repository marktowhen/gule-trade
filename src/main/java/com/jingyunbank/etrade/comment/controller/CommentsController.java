package com.jingyunbank.etrade.comment.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Range;
import com.jingyunbank.core.Result;
import com.jingyunbank.core.web.AuthBeforeOperation;
import com.jingyunbank.core.web.Login;
import com.jingyunbank.etrade.api.comment.bo.Comments;
import com.jingyunbank.etrade.api.comment.bo.CommentsImg;
import com.jingyunbank.etrade.api.comment.service.ICommentImgService;
import com.jingyunbank.etrade.api.comment.service.ICommentService;
import com.jingyunbank.etrade.api.order.presale.bo.OrderGoods;
import com.jingyunbank.etrade.api.order.presale.service.IOrderGoodsService;
import com.jingyunbank.etrade.api.user.bo.UserInfo;
import com.jingyunbank.etrade.api.user.bo.Users;
import com.jingyunbank.etrade.api.user.service.IUserInfoService;
import com.jingyunbank.etrade.api.user.service.IUserService;
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
	public Result<String> saveComments(@RequestParam("ogid") String ogid,@RequestBody CommentsVO commentVO,HttpServletRequest request) throws Exception{
		commentVO.setID(KeyGen.uuid());
		Optional<OrderGoods> optional = orderGoodsService.singleOrderGoods(ogid);
		OrderGoods	orderGoods =optional.get();
		commentVO.setGID(orderGoods.getGID());
		commentVO.setOID(orderGoods.getOID());
		commentVO.setUID(Login.UID(request));
		commentVO.setAddtime(new Date());
		Comments comments=new Comments();
		BeanUtils.copyProperties(commentVO, comments);

		List<CommentsImg> imgList = null;
		if(commentVO.getPicture().size()!=0){
			imgList = new ArrayList<CommentsImg>();
			for(int i=0;i<commentVO.getPicture().size();i++){
				CommentsImg commentsImg=new CommentsImg();
				commentsImg.setID(KeyGen.uuid());
				commentsImg.setPicture(commentVO.getPicture().get(i));
				commentsImg.setCommentID(commentVO.getID());
				imgList.add(commentsImg);
			}
		}
		commentService.save(comments ,imgList);
		
			
		return Result.ok("保存成功");
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
	public Result<List<CommentsVO>> getGradeComments(@RequestParam("gid") String gid,@RequestParam("commentGrade") int commentGrade,@RequestParam("picture") int picture,@RequestParam("from") int from,@RequestParam("size") int size,HttpServletRequest request,HttpSession session) throws Exception{
		boolean existsImg = picture!=0?true:false;
		List<Comments> comments=commentService.list(gid, commentGrade, existsImg, new Range(from, from+size));
		List<CommentsVO> commentVOs=convert(comments);
		return Result.ok(commentVOs);
		
	}
	
	private List<CommentsVO> convert(List<Comments> comments){
	
		List<CommentsVO> commentVOs=new ArrayList<CommentsVO>();
		for(int i=0;i<comments.size();i++){
			CommentsVO commentsVO=new CommentsVO();
			UserVO userVO = new UserVO();
			UserInfoVO userinfoVO = new UserInfoVO();
			if(userService.single(comments.get(i).getUID()).isPresent()){
				Users users=userService.single(comments.get(i).getUID()).get();
				UserInfo userInfo=userInfoService.getByUid(comments.get(i).getUID()).get();
				BeanUtils.copyProperties(comments.get(i),commentsVO);
				BeanUtils.copyProperties(users, userVO);
				BeanUtils.copyProperties(userInfo, userinfoVO);
				commentsVO.setUserVO(userVO);
				commentsVO.setUserInfoVO(userinfoVO);
				List<CommentsImg> commentsImgs=	commentImgService.list(comments.get(i).getID());
				commentsVO.setImgs(commentsImgs);
				commentVOs.add(commentsVO);
			}
			
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
	public Result<String> remove(@PathVariable String id,HttpServletRequest request,HttpSession session) throws Exception{
		commentService.remove(id);
		return Result.ok("删除成功");
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
		int gradeCounts=0;
		List<Comments> comments=commentService.list(gid);
		if(comments.size()==0){
			zongjibie=10;
		}
		personCount=commentService.commentCount(gid);
		if(personCount==0){
			personCount=0;
		}
		for(int i=0;i<comments.size();i++){
			if(comments.get(i).getCommentGrade()<8){
				gradeCounts=8;
			}else{
				gradeCounts=comments.get(i).getCommentGrade();
			}
			gradeCount+=gradeCounts;
		}
		if(personCount!=0){
			zongjibie=gradeCount/personCount;
		}
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
		int levelGrade=0;
		int goodsCount=0;
		int serviceCount=0;
		int logisticsCount=0;
		double level = 0.0;
		int personCount=0;
		int serviceCounts=0;
		int goodsCounts=0;
		int logisticsCounts=0;
		List<Comments> comments=commentService.list(gid);
		if(comments.size()==0){
			level=0;
		}
		personCount=commentService.commentCount(gid);
		if(personCount==0){
			personCount=0;
		}
		for (int i=0;i<comments.size();i++) {
			if(comments.get(i).getCommentGrade()<6){
				goodsCounts = 8;
			}else{
				goodsCounts=comments.get(i).getCommentGrade();
			}
			goodsCount+=goodsCounts;
			if(comments.get(i).getServiceGrade()<6){
				serviceCounts = 8;
			}else{
				serviceCounts=comments.get(i).getServiceGrade();
			}
			serviceCount+=serviceCounts;
			if(comments.get(i).getLogisticsGrade()<6){
				logisticsCounts = 8;
			}else{
				logisticsCounts = comments.get(i).getLogisticsGrade();
			}
			logisticsCount+=logisticsCounts;	
		}
		if(personCount!=0){
			double  d1  =  goodsCount+serviceCount+logisticsCount; 
			double d2 = (double)personCount;
			double d3=d1/(3.0)/d2;
			level=((int)(d3*10))/10.0;   
			
		}
		levelGrade=(int)(level*10);
		CommentsVO commentsVO=new CommentsVO();
		commentsVO.setLevel(level);
		commentsVO.setPersonCount(personCount);
		commentsVO.setLevelGrade(levelGrade);
		return Result.ok(commentsVO);	
		
		
	}
	/**
	 * 通过oid和gid查出详情
	 * @param oid
	 * @return
	 */
	@AuthBeforeOperation
	@RequestMapping(value="/api/comments/details/{oid}/{gid}",method=RequestMethod.GET)
	public Result<CommentsVO> getCommentDetails(@PathVariable String oid,@PathVariable String gid){
		float personalGrade=0;
		CommentsVO commentsVO=new CommentsVO();
		Optional<Comments> optional=commentService.singleByOid(oid,gid);
			if(optional.isPresent()){
				if(optional.get().getCommentGrade()==0&&optional.get().getServiceGrade()==0&&optional.get().getLogisticsGrade()==0){
					 personalGrade=0;
				}
				BeanUtils.copyProperties(optional.get(), commentsVO);
				List<CommentsImg> commentsImgs=	commentImgService.list(optional.get().getID());
				if(commentsImgs.size()!=0){
					commentsVO.setImgs(commentsImgs);
				}
				 personalGrade=(optional.get().getCommentGrade()+optional.get().getServiceGrade()+optional.get().getLogisticsGrade())/3;
				commentsVO.setPersonalGrade(personalGrade);
				return Result.ok(commentsVO);		
			}
			return Result.fail("没添加任何评价信息");
			
	}
	
	@RequestMapping(value="/api/allcomments",method=RequestMethod.GET)
	public Result<List<CommentsVO>> getComment(){
		
		return Result.ok(commentService.list().stream().map(bo ->{
			CommentsVO vo=new CommentsVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList()));
		
	}
}
