package bookstore.model;


import java.util.*;

public class Cart {
private List<CartItem> items = new ArrayList<>();;
	
	public void addBook(Book book) {
		for(CartItem item : items) {
			if(item.getBook().getBookId() == book.getBookId()) {
				item.setQuantity(item.getQuantity() + 1);
				return;
			}
		}
		items.add(new CartItem(book,1));
	}
	public void removeBook(Book book) {
		items.removeIf(item -> item.getBook().getBookId() == book.getBookId());
	}
	public double calculateTotal() {
		double total = 0;
		
		for(CartItem item : items) {
			total += item.getSubtotal();
		}
		return total;
	}
	public List<CartItem> getCartItem() {
		return items;
	}
}
