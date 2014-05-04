package jdepend.webserver;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import jdepend.framework.context.JDependContext;

public class CooperServletContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// JDependContext.setRunningPath(arg0.getServletContext()
		// .getRealPath("//"));
		JDependContext
				.setRunningPath("C:\\Documents and Settings\\wangdg\\Workspaces\\MyEclipse 8.5\\.metadata\\.me_tcat\\webapps\\cooper\\WEB-INF");

	}

}
