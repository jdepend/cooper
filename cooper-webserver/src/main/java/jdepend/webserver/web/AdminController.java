package jdepend.webserver.web;

import javax.servlet.http.HttpServletRequest;

import jdepend.framework.exception.JDependException;
import jdepend.knowledge.database.AnalysisResultRepository;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "admin")
public class AdminController {

	private Logger logger = Logger.getLogger(AdminController.class);

	@RequestMapping(value = "/result/list", method = RequestMethod.GET)
	public String upload(Model model, HttpServletRequest request) throws JDependException {
		logger.info(request.getRemoteAddr() + " enter into admin result page");

		model.addAttribute("resultSummrys",
				AnalysisResultRepository.getResultSummrys(WebConstants.DEFLAUT_GROUP, WebConstants.DEFLAUT_COMMAND));
		
		
		return "results";
	}

}
