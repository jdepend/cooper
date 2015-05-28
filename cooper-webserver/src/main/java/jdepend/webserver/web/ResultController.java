package jdepend.webserver.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import jdepend.framework.exception.JDependException;
import jdepend.util.todolist.TODOItem;
import jdepend.webserver.model.WebAnalysisResult;

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
		model.addAttribute("component", result.getTheComponent(componentId));
		return "class_list";
	}

	@RequestMapping(value = "/component/{componentId}/ca/view.ajax", method = RequestMethod.GET)
	public String componentCa(Model model, @PathVariable String componentId, HttpServletRequest request)
			throws JDependException {

		WebAnalysisResult result = (WebAnalysisResult) request.getSession().getAttribute(WebConstants.SESSION_RESULT);
		if (result == null) {
			throw new JDependException("Session 过期，或者非法进入该页。");
		}
		model.addAttribute("components", result.getTheComponent(componentId).getAfferents());

		return "component_list";
	}

	@RequestMapping(value = "/component/{componentId}/ce/view.ajax", method = RequestMethod.GET)
	public String componentCe(Model model, @PathVariable String componentId, HttpServletRequest request)
			throws JDependException {

		WebAnalysisResult result = (WebAnalysisResult) request.getSession().getAttribute(WebConstants.SESSION_RESULT);
		if (result == null) {
			throw new JDependException("Session 过期，或者非法进入该页。");
		}
		model.addAttribute("components", result.getTheComponent(componentId).getEfferents());

		return "component_list";
	}

	@RequestMapping(value = "/relation/{current}/{depend}/view.ajax", method = RequestMethod.GET)
	public String relationDetails(Model model, @PathVariable String current, @PathVariable String depend,
			HttpServletRequest request) throws JDependException {

		WebAnalysisResult result = (WebAnalysisResult) request.getSession().getAttribute(WebConstants.SESSION_RESULT);
		if (result == null) {
			throw new JDependException("Session 过期，或者非法进入该页。");
		}
		model.addAttribute("relation", result.getTheRelation(current, depend));

		return "relation_details";
	}

	@RequestMapping(value = "/todoItem/{id}/view.ajax", method = RequestMethod.GET)
	public String todoItemDetails(Model model, @PathVariable String id, HttpServletRequest request)
			throws JDependException {

		List<TODOItem> todoList = (List<TODOItem>) request.getSession().getAttribute(
				WebConstants.SESSION_RESULT_TODOLIST);
		if (todoList == null) {
			throw new JDependException("Session 过期，或者非法进入该页。");
		}
		for (TODOItem item : todoList) {
			if (item.getId().equals(id)) {
				model.addAttribute("todoItem", item);
			}
		}

		return "todo_item";
	}

}
