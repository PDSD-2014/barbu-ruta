package ro.pub.cs.brtalk.tools;

public class FriendInfo {
	
	private String username;
	private String email;
	private int id;
	
	public FriendInfo(String username, String email,int id){
		this.username = username;
		this.email = email;
		this.id = id;
	}

	public FriendInfo(){
		
	}
	
	public FriendInfo(String username){
		this.username = username;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	

}
