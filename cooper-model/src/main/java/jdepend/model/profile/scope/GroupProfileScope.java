package jdepend.model.profile.scope;


public class GroupProfileScope extends AbstractProfileScope{
	
	private static final long serialVersionUID = 1442085783868320831L;
	
	private String group;

	@Override
	protected boolean isSelf(String group, String command) {
		return this.group.equals(group);
	}
}
