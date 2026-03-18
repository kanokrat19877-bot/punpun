package bookstore.model;


public class Admin extends User {
	private Inventory inventory;
	
	public Admin (String userId, String name, String email, String password, Inventory inventory) {
		super(userId, name, email, password);
		this.inventory = inventory;
	}
	public void addBook(Book book) {
		inventory.addBook(book);
	}
	public void undateStock(Book book, int stock) {
		book.setStock(stock);
	}
}
