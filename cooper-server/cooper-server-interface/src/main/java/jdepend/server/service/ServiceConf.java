package jdepend.server.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import jdepend.model.result.ProfileFacadeImpl;

/**
 * 服务配置信息
 * 
 * @author user
 *
 */
public class ServiceConf implements Serializable {

	private static final long serialVersionUID = 690708875879257657L;

	private Properties serviceProperties;

	private Map<String, String> parseProperties;

	private List<String> filteredPackages;

	private List<String> notFilteredPackages;
	
	private List<String> commandFilteredPackages;

	private ProfileFacadeImpl profileFacade;

	public Properties getServiceProperties() {
		return serviceProperties;
	}

	public void setServiceProperties(Properties serviceProperties) {
		this.serviceProperties = serviceProperties;
	}

	public Map<String, String> getParseProperties() {
		return parseProperties;
	}

	public void setParseProperties(Map<String, String> parseProperties) {
		this.parseProperties = parseProperties;
	}

	public List<String> getCommandFilteredPackages() {
		return commandFilteredPackages;
	}

	public void setCommandFilteredPackages(List<String> commandFilteredPackages) {
		this.commandFilteredPackages = commandFilteredPackages;
	}

	public List<String> getFilteredPackages() {
		return filteredPackages;
	}

	public void setFilteredPackages(List<String> filteredPackages) {
		this.filteredPackages = filteredPackages;
	}

	public List<String> getNotFilteredPackages() {
		return notFilteredPackages;
	}

	public void setNotFilteredPackages(List<String> notFilteredPackages) {
		this.notFilteredPackages = notFilteredPackages;
	}

	public ProfileFacadeImpl getProfileFacade() {
		return profileFacade;
	}

	public void setProfileFacade(ProfileFacadeImpl profileFacade) {
		this.profileFacade = profileFacade;
	}
}
