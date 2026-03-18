package bookstore.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class Inventory {
	private HashMap<Integer, Book> books = new HashMap<>();
	private TreeMap<Double, List<Book>> priceIndex = new TreeMap<>();

	public void addBook(Book book) {
		books.put(book.getBookId(), book);
		priceIndex.putIfAbsent(book.getPrice(), new ArrayList<>());
		priceIndex.get(book.getPrice()).add(book);
	}
	public Book searchBook(String title) {
		for(Book b : books.values()) {
			if (b.getTitle().equalsIgnoreCase(title)) {
				return b;
			}
		}
		return null;
	}
	public void updateStock(int bookId , int amount) {
		Book book = books.get(bookId);
		
		if( book != null) {
			book.setStock(book.getStock() + amount);
		}
	}
	public Collection<Book> getAllBooks() {
		return books.values();
	}
	//public void removeBook(Book book) {
		
	//}

}
