package bookstore.model;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
	private Cart cart;
	private List<Order> OrderHistory;
	
	public Customer() {}
	public Customer (int id, String name, String email, String password, Cart cart, List <Order> OrderHistory) {
		super(id, name, email, password);
		this.cart = new Cart();
		OrderHistory = new ArrayList<>();
	}
	public void addToCart(Book book) {
		cart.addBook(book);
	}
	public Order checkout() {
		List<OrderItem> orderItem = new ArrayList<>();
		
		for(CartItem item : cart.getCartItem()) {
			orderItem.add(new OrderItem(item.getBook(), item.getQuantity(), item.getSubtotal())
			);
		}
	
		Order order = new Order(OrderHistory.size() + 1 , orderItem);
		OrderHistory.add(order);
		cart.getCartItem().clear();	
		return order;
	}
	public List<Order> viewOrderHistory() {
		return OrderHistory;
	}
	public Cart getCart() {
		return cart;
	}
}
