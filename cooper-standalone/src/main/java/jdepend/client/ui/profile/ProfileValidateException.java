package jdepend.client.ui.profile;

import jdepend.model.profile.ProfileException;

public class ProfileValidateException extends ProfileException {

	private int tabIndex;

	public ProfileValidateException(int tabIndex) {
		super();
		this.tabIndex = tabIndex;
	}

	public ProfileValidateException(String arg0, Throwable arg1, int tabIndex) {
		super(arg0, arg1);
		this.tabIndex = tabIndex;
	}

	public ProfileValidateException(String arg0, int tabIndex) {
		super(arg0);
		this.tabIndex = tabIndex;
	}

	public ProfileValidateException(Throwable arg0, int tabIndex) {
		super(arg0);
		this.tabIndex = tabIndex;
	}

	public int getTabIndex() {
		return tabIndex;
	}

	public void setTabIndex(int tabIndex) {
		this.tabIndex = tabIndex;
	}
}
