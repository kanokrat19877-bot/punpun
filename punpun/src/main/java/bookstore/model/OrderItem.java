package bookstore.model;

public class OrderItem {
	private Book book;
	private int quantity;
	private double price;
	
	public OrderItem() {}
	public OrderItem(Book book, int quantity, double price) {
		this.book = book;
		this.quantity = quantity;
		this.price = price;
	}
	public Book getbook() {
		return book;
	}
	public int getQuantity() {
		return quantity;
	}
	public double getPrice() {
		return price;
	}
	public double getSubtotal() {
		return price * quantity;
	}
}
