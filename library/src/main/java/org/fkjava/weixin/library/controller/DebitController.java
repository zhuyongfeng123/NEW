package org.fkjava.weixin.library.controller;

import org.fkjava.weixin.library.domain.DebitList;
import org.fkjava.weixin.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/ZYF/library/debit")
@SessionAttributes(names = { "debitList" })
public class DebitController {
	@Autowired
	private LibraryService libraryService;
	@RequestMapping
	public ModelAndView debit(@RequestParam("bookId") String bookId, 
	WebRequest request) {
	ModelAndView mav = new ModelAndView();
	DebitList list = (DebitList) request.getAttribute("debitList", WebRequest.SCOPE_SESSION);
	if (list == null) {
	list = new DebitList();
	request.setAttribute("debitList", list, WebRequest.SCOPE_SESSION);}
	libraryService.add(list, bookId);
    mav.setViewName("redirect:/kemao_3/library/debit/list");
		return mav;
	}
	@RequestMapping("list")
	public String list() {
	return "/WEB-INF/views/library/debit/list.jsp";}
	@RequestMapping("remove/{id}")
	public String remove(@PathVariable("id") String id, @SessionAttribute(name = "debitList") DebitList list) {
	libraryService.remove(list, id);
	return "redirect:/kemao_3/library/debit/list";}}
