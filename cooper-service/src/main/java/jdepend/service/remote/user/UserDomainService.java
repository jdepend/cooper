package jdepend.service.remote.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdepend.framework.exception.JDependException;
import jdepend.framework.log.Operation;

public final class UserDomainService {

	private UserStateChangeListener listener;

	private final static String ADMIN = "admin";

	public void processCredits(User user, Operation operation) throws JDependException {
		if (!user.getName().equals(ADMIN) && operationToIntegral.get(operation) != null) {
			user.changeIntegral(operationToIntegral.get(operation));
			user.save();
			if (listener != null) {
				listener.onChange(user);
			}
		}
	}

	public void saveUserAction(List<UserActionItem> items) throws JDependException {
		UserActionRepository.insertUserActions(items);
	}

	public List<UserActionItem> getAllUserActions() throws JDependException {
		return UserActionRepository.getAllUserActions();
	}

	public List<UserActionItem> getTheUserActions(String username) throws JDependException {
		return UserActionRepository.getTheUserActions(username);
	}

	public static final Map<Operation, Integer> operationToIntegral = new HashMap<Operation, Integer>();
	static {
		operationToIntegral.put(Operation.login, 10);

		operationToIntegral.put(Operation.uploadAnalyzer, 500);
		operationToIntegral.put(Operation.downloadAnalyzer, -100);
		operationToIntegral.put(Operation.executeAnalyzer, 10);

		operationToIntegral.put(Operation.executeCommand, 5);
		operationToIntegral.put(Operation.createGroup, 50);
		operationToIntegral.put(Operation.createCommand, 10);

		operationToIntegral.put(Operation.moveToClass, -5);
		operationToIntegral.put(Operation.uniteComponent, -5);
		operationToIntegral.put(Operation.saveTextReport, -10);
	}

	public void setListener(UserStateChangeListener listener) {
		this.listener = listener;
	}
}
