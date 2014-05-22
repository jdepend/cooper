package jdepend.webserver.web;

import javax.servlet.http.HttpServletRequest;

import jdepend.framework.exception.JDependException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "record")
public class RecordController {

	private Logger logger = Logger.getLogger(RecordController.class);

	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public void record(HttpServletRequest request) throws JDependException {
		logger.info(request.getRemoteAddr() + "下载了单机版");
	}

}
