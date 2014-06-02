package jdepend.webserver.web;

import javax.servlet.http.HttpServletRequest;

import jdepend.framework.exception.JDependException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "result")
public class ResultController {

	@RequestMapping(value = "/component/{componentId}/classes/view.ajax", method = RequestMethod.GET)
	public String componentDetails(Model model, @PathVariable String componentId, HttpServletRequest request)
			throws JDependException {

		WebAnalysisResult result = (WebAnalysisResult) request.getSession().getAttribute(WebConstants.SESSION_RESULT);
		if (result == null) {
			throw new JDependException("Session 过期，或者非法进入该页。");
		}
		model.addAttribute("component", result.getComponentForNames().get(componentId));
		return "class_list";
	}
	
	@RequestMapping(value = "/relation/{current}/{depend}/view.ajax", method = RequestMethod.GET)
	public String relationDetails(Model model, @PathVariable String current, @PathVariable String depend, HttpServletRequest request)
			throws JDependException {

		WebAnalysisResult result = (WebAnalysisResult) request.getSession().getAttribute(WebConstants.SESSION_RESULT);
		if (result == null) {
			throw new JDependException("Session 过期，或者非法进入该页。");
		}
		model.addAttribute("relation", result.getTheRelation(current, depend));
		
		return "relation_details";
	}

}
