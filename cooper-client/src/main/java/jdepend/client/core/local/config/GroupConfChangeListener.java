package jdepend.client.core.local.config;


public interface GroupConfChangeListener {

	public void onRefresh() throws CommandConfException;

	public void onDelete(String group) throws CommandConfException;

	public void onCreate(String group) throws CommandConfException;

	public void onUpdate(String group) throws CommandConfException;

}
