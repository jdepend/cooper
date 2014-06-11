/**
 * <pre>
 * Copyright:		Copyright(C) 2011-2012, ketayao.com
 * Filename:		com.ketayao.ketacustom.controller.LoginController.java
 * Class:			LoginController
 * Date:			2012-8-2
 * Author:			<a href="mailto:snap@neusoft.com">SaCa-SNAP</a>
 * Version          1.1.0
 * Description:		
 *
 * </pre>
 **/

package jdepend.webserver.web;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

	private Logger logger = Logger.getLogger(LoginController.class);

	private static final String LOGIN_PAGE = "account/login";
	private static final String UNAUTHORIZED_PAGE = "account/unauthorized";

	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String login() {
		return LOGIN_PAGE;
	}
	
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String fail1(Model model) {
		model.addAttribute("msg", "登录失败！");
		return LOGIN_PAGE;
	}

	@RequestMapping(value = "login/success", method = RequestMethod.GET)
	public String success() {
		return "redirect:/admin/result/list";
	}
	
	@RequestMapping(value = "unauthorized", method = RequestMethod.GET)
	public String unauthorized(Model model) {
		model.addAttribute("msg", "您不能访问该资源！");
		return UNAUTHORIZED_PAGE;
	}

}
