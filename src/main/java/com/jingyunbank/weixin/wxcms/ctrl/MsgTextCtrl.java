package com.jingyunbank.weixin.wxcms.ctrl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.jingyunbank.weixin.wxcms.domain.MsgBase;
import com.jingyunbank.weixin.wxcms.domain.MsgText;
import com.jingyunbank.weixin.wxcms.service.MsgBaseService;
import com.jingyunbank.weixin.wxcms.service.MsgTextService;

/**
 * 
 */

@Controller
@RequestMapping("/msgtext")
public class MsgTextCtrl{

	@Autowired
	private MsgTextService entityService;
	
	@Autowired
	private MsgBaseService baseService;

	@RequestMapping(value = "/getById")
	public ModelAndView getById(String id){
		entityService.getById(id);
		return new ModelAndView();
	}

	@RequestMapping(value = "/list")
	public  ModelAndView list(@ModelAttribute MsgText searchEntity){
		ModelAndView modelAndView = new ModelAndView("wxcms/msgtextList");
		List<MsgText> pageList = entityService.listForPage(searchEntity);
		modelAndView.addObject("pageList", pageList);
		modelAndView.addObject("cur_nav", "text");
		return modelAndView;
	}

	@RequestMapping(value = "/toMerge")
	public ModelAndView toMerge(MsgText entity){
		ModelAndView mv = new ModelAndView("wxcms/msgtextMerge");
		mv.addObject("cur_nav", "text");
		if(entity.getId() != null){
			MsgText text = entityService.getById(entity.getId().toString());
			mv.addObject("entity",text);
			mv.addObject("baseEntity", baseService.getById(text.getBaseId().toString()));
		}else{
			mv.addObject("entity",new MsgText());
			mv.addObject("baseEntity", new MsgBase());
		}
		return mv;
	}
	
	@RequestMapping(value = "/doMerge")
	public ModelAndView doMerge(MsgText entity){
		if(entity.getId() != null){
			entityService.update(entity);
		}else{
			entityService.add(entity);
		}
		return new ModelAndView("redirect:/msgtext/list.html");
	}

	@RequestMapping(value = "/delete")
	public ModelAndView delete(MsgText entity){
		entityService.delete(entity);
		return new ModelAndView("redirect:/msgtext/list.html");
	}



}

