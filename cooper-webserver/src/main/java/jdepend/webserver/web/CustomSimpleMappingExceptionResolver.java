/**
 * 
 */
package jdepend.webserver.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

/**
 * 扩展支持Ajax的异常处理
 * 
 * @author David Tian
 *
 */
public class CustomSimpleMappingExceptionResolver extends SimpleMappingExceptionResolver {
	private final static Logger logger = LoggerFactory.getLogger(SimpleMappingExceptionResolver.class);

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		
		String viewName = determineViewName(ex, request);
		if (viewName != null) {// JSP格式返回
			if (!(request.getHeader("accept").indexOf("application/json") > -1 
					|| (request.getHeader("X-Requested-With") != null 
						&& request.getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1))) {
				// 如果不是异步请求
				// Apply HTTP status code for error views, if specified.
				// Only apply it if we're processing a top-level request.
				Integer statusCode = determineStatusCode(request, viewName);
				if (statusCode != null) {
					applyStatusCodeIfPossible(request, response, statusCode);
				}
				
				if (logger.isInfoEnabled()) {
					logger.info("Page error:", ex);
				}
				
				return getModelAndView(viewName, ex, request);
			} else {// JSON格式返回
				PrintWriter writer = null;
				try {
					writer = response.getWriter();
				} catch (IOException e) {
					if (logger.isInfoEnabled()) {
						logger.info("Ajax error:", e);
					}
				}
				
				// 告诉客户端异常信息
				JSONObject json = new JSONObject();
				json.put("code", "-1");
				json.put("msg", ex.getMessage());
				
				writer.write(json.toString());
				writer.flush();
				
				if (logger.isInfoEnabled()) {
					logger.info("Ajax error:", ex);
				}
				return null;

			}
		} else {
			return null;
		}
	}
}