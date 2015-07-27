package jdepend.server.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import jdepend.model.profile.ProfileFacade;

public class ServiceConf implements Serializable {

	private static final long serialVersionUID = 690708875879257657L;

	private Properties serviceProperties;

	private Properties parseProperties;

	private List<String> commandFilteredPackages;

	private Collection<String> filteredPackages;

	private Collection<String> notFilteredPackages;

	private ProfileFacade profileFacade;

	public Properties getServiceProperties() {
		return serviceProperties;
	}

	public void setServiceProperties(Properties serviceProperties) {
		this.serviceProperties = serviceProperties;
	}

	public Properties getParseProperties() {
		return parseProperties;
	}

	public void setParseProperties(Properties parseProperties) {
		this.parseProperties = parseProperties;
	}

	public List<String> getCommandFilteredPackages() {
		return commandFilteredPackages;
	}

	public void setCommandFilteredPackages(List<String> commandFilteredPackages) {
		this.commandFilteredPackages = commandFilteredPackages;
	}

	public Collection<String> getFilteredPackages() {
		return filteredPackages;
	}

	public void setFilteredPackages(Collection<String> filteredPackages) {
		this.filteredPackages = filteredPackages;
	}

	public Collection<String> getNotFilteredPackages() {
		return notFilteredPackages;
	}

	public void setNotFilteredPackages(Collection<String> notFilteredPackages) {
		this.notFilteredPackages = notFilteredPackages;
	}

	public ProfileFacade getProfileFacade() {
		return profileFacade;
	}

	public void setProfileFacade(ProfileFacade profileFacade) {
		this.profileFacade = profileFacade;
	}
}
