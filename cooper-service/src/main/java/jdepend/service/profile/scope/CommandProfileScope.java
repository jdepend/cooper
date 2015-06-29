package jdepend.service.profile.scope;

public class CommandProfileScope extends AbstractProfileScope {

	private static final long serialVersionUID = -1785680808798908968L;

	private String group;

	private String command;

	@Override
	protected boolean isSelf(String group, String command) {
		return this.group.equals(group) && this.command.equals(command);
	}

}
