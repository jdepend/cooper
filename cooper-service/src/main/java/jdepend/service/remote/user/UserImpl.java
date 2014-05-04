package jdepend.service.remote.user;

import jdepend.framework.exception.JDependException;

public final class UserImpl implements User {

	private String name;

	private String dept;

	private Integer integral;

	private boolean valid;

	public UserImpl(String name, String dept, Integer integral, boolean valid) {
		super();
		this.name = name;
		this.dept = dept;
		this.integral = integral;
		this.valid = valid;
	}

	public UserImpl(String name, String dept, Integer integral) {
		this(name, dept, integral, true);
	}

	public void save() throws JDependException {
		UserRepository.save(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jdepend.service.remote.user.User#getName()
	 */
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jdepend.service.remote.user.User#getDept()
	 */
	public String getDept() {
		return dept;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jdepend.service.remote.user.User#getIntegral()
	 */
	public Integer getIntegral() {
		return integral;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jdepend.service.remote.user.User#setIntegral(java.lang.Integer)
	 */
	public void setIntegral(Integer integral) {
		this.integral = integral;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jdepend.service.remote.user.User#changeIntegral(java.lang.Integer)
	 */
	public void changeIntegral(Integer change) {
		if (change != null) {
			if (this.integral == null) {
				this.integral = 0;
			}
			this.integral += change;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jdepend.service.remote.user.User#isValid()
	 */
	public boolean isValid() {
		return valid;
	}
}
