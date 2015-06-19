package jdepend.server.service.user;

public interface UserAppService extends UserRemoteService {

	public void setUserStateChangeListener(UserStateChangeListener listener);

}
