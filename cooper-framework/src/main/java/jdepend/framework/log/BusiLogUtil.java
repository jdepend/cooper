package jdepend.framework.log;

import jdepend.framework.context.ClientContext;
import jdepend.framework.exception.JDependException;

/**
 * 业务日志
 * 
 * @author user
 *
 */
public class BusiLogUtil {

	private static BusiLogUtil inst = new BusiLogUtil();

	public static boolean BUSINESSLOG = true;

	private BusinessLogWriter busiWriter;

	private BusiLogUtil() {
		busiWriter = new DBBusinessLogWriter();
	}

	public static BusiLogUtil getInstance() {
		return inst;
	}

	public void businessLog(Operation operation) {
		this.businessLog(ClientContext.getUser(), operation);
	}

	private void businessLog(String userName, Operation operation) {
		if (BUSINESSLOG) {
			try {
				busiWriter.businessLog(userName, operation);
			} catch (JDependException e) {
				e.printStackTrace();
			}
		}
	}

	public void addLogListener(BusiLogListener logListener) {
		busiWriter.addLogListener(logListener);
	}

}
