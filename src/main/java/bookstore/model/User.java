package bookstore.model;

public class User {
	protected int userId;
	protected String name;
	protected String email;
	protected String password;
	
	public User() {}
	public User(int userId, String name, String email, String password) {
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
}