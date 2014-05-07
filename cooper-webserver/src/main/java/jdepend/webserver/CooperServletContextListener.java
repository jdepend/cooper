package jdepend.webserver;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import jdepend.framework.context.JDependContext;
import jdepend.model.util.ClassSearchUtil;
import jdepend.parse.util.SearchUtil;

public class CooperServletContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {

		String path = arg0.getServletContext().getRealPath("//");
		JDependContext.setRunningPath(path);
		
		String workspacePath = path + "//WEB-INF";
		JDependContext.setWorkspacePath(workspacePath);

		String classPath = path + "//WEB-INF//classes";
		SearchUtil search = new SearchUtil();
		search.addPath(classPath);
		ClassSearchUtil.getInstance().setClassList(search.getClasses());
	}

}
