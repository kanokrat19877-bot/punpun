package bookstore.model;

public class User {
	protected String userId;
	protected String name;
	protected String email;
	protected String password;
	
	public User() {}
	public User(String userId, String name, String email, String password) {
		this.userId = userId;
		this.name = name;
		this.email = email;
		this.password = password;
	}
	public void login(){
        System.out.println(name + " login");
    }
    public void logout(){
        System.out.println(name + " logout");
    }
    
    public boolean login (String email, String password) {
		return this.email.equals(email) && this.password.equals(password);
	}
	public String getEmail() {
		return email;
	}
	public String getName() {
		return name;
	}
    
}