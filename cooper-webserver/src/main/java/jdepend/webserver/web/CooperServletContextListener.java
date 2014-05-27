package jdepend.webserver.web;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import jdepend.framework.context.JDependContext;
import jdepend.framework.persistent.ConnectionFactory;
import jdepend.model.util.ClassSearchUtil;
import jdepend.parse.util.SearchUtil;
import jdepend.service.persistent.ServerConnectionProvider;

import org.apache.log4j.Logger;

public class CooperServletContextListener implements ServletContextListener {
	
	private Logger logger = Logger.getLogger(CooperServletContextListener.class);

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {

		String path = arg0.getServletContext().getRealPath("//");
		JDependContext.setRunningPath(path);

		String workspacePath = path + File.separator + "WEB-INF";
		JDependContext.setWorkspacePath(workspacePath);

		logger.info("WorkspacePath:" + workspacePath);

		// 设置ConnectionProvider
		ConnectionFactory.setProvider(new ServerConnectionProvider());

		String classPath = path + File.separator + "WEB-INF" + File.separator + "classes";
		SearchUtil search = new SearchUtil();
		search.addPath(classPath);
		ClassSearchUtil.getInstance().setClassList(search.getClasses());
	}

}
