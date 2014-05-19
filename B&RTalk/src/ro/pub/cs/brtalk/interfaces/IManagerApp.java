package ro.pub.cs.brtalk.interfaces;

public interface IManagerApp {
	
	public boolean isNetworkConnected();//implemented in IMService
	public boolean isUserAuthenticated();//implemented in IMService
	public void exit();//called when stop service
	/*
	 * Sends request to server to sign up a new user
	 * Return a message(accepted, rejected and why was rejected)
	 * Implemented in IMService
	 */
	public String signUpUser(String username, String password, String email);
	public String signInUser(String username, String password);
	public String friendAdd(String username);
	public String sendMessage(String to, String text);
	
	
}
