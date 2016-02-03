package com.jingyunbank.etrade.posts.links.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jingyunbank.core.KeyGen;
import com.jingyunbank.core.Result;

import com.jingyunbank.etrade.api.posts.links.bo.Link;
import com.jingyunbank.etrade.api.posts.links.service.ILinkService;

import com.jingyunbank.etrade.posts.links.bean.LinkVO;

@RestController
@RequestMapping("/api/link")
public class LinkController {
	@Autowired
	private ILinkService linkService;

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public Result<String> save(HttpServletRequest request, @RequestBody @Valid LinkVO vo, BindingResult valid)
			throws Exception {
		if (valid.hasErrors()) {
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream().map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
					.collect(Collectors.joining(" ; ")));
		}
		Link bo = new Link();
		BeanUtils.copyProperties(vo, bo);
		bo.setID(KeyGen.uuid());
		bo.setStatus(true);
		if (linkService.save(bo)) {
			return Result.ok("success");
		}
		return Result.fail("fail");
	}

	@RequestMapping(value = "/updateview/{lid}", method = RequestMethod.GET)
	public Result<LinkVO> queryBrandById(@PathVariable String lid) throws Exception {
		LinkVO link = null;
		Optional<Link> bo = linkService.singByID(lid);
		if (Objects.nonNull(bo)) {
			link = new LinkVO();
			BeanUtils.copyProperties(bo.get(), link);
		}
;		return Result.ok(link);
	}

	@RequestMapping(value = "/update/{lid}", method = RequestMethod.POST)
	public Result<String> update(HttpServletRequest request, @PathVariable("lid") String lid,
			@RequestBody @Valid LinkVO vo, BindingResult valid) throws Exception {
		if (valid.hasErrors()) {
			List<ObjectError> errors = valid.getAllErrors();
			return Result.fail(errors.stream().map(oe -> Arrays.asList(oe.getDefaultMessage()).toString())
					.collect(Collectors.joining(" ; ")));
		}
		Link bo = new Link();
		BeanUtils.copyProperties(vo, bo);
		bo.setID(lid);

		if (linkService.refresh(bo)) {
			return Result.ok("success");
		}
		return Result.fail("fail");
	}

	@RequestMapping(value = "/del/{lid}", method = RequestMethod.PUT)
	public Result<String> delLink(@PathVariable String lid) throws Exception {
		if (linkService.remove(lid)) {
			return Result.ok("success");
		}
		return Result.fail("fail");
	}

	@RequestMapping(value = "/all/list", method = RequestMethod.GET)
	public Result<List<LinkVO>> queryLinks() throws Exception {
		List<LinkVO> list = linkService.listLinks().stream().map(bo -> {
			LinkVO vo = new LinkVO();
			BeanUtils.copyProperties(bo, vo);
			return vo;
		}).collect(Collectors.toList());
		return Result.ok(list);
	}

}
