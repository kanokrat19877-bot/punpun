package bookstore.service;

import bookstore.model.*;
import java.util.*;
import java.util.stream.*;

public class BookService {
	   private Inventory inventory;
	   
	   public BookService(Inventory inventory) {
	        this.inventory = inventory;
	    }
	    public List<Book> searchByTitle(String title) {
	        return inventory.getAllBooks()
	        		.stream()
	               .filter(b -> b.getTitle().toLowerCase()
	               .contains(title.toLowerCase()))
	               .collect(Collectors.toList());
	    }
	    public List<Book> searchByAuthor(String author) {
	        return inventory.getAllBooks()
	        		.stream()
	               .filter(b -> b.getAuthorName().toLowerCase()
	               .contains(author.toLowerCase()))
	               .collect(Collectors.toList());
	    }

	    public List<Book> searchByCategory(String category) {
	        return inventory.getAllBooks()
	        		.stream()
	               .filter(b -> b.getCategory().equalsIgnoreCase(category))
	               .collect(Collectors.toList());
	    }

	    public List<Book> sortByRating() {
	        return inventory.getAllBooks()
	        		.stream()
	                .sorted(Comparator.comparing(Book::getRating).reversed())
	                .collect(Collectors.toList());
	    }

	    public List<Book> sortByPopularity() {
	        return inventory.getAllBooks()
	        		.stream()
	                .sorted(Comparator.comparing(Book::getSoldCount).reversed())
	                .collect(Collectors.toList());
	    }

	    public List<Book> filterByPrice(double minPrice, double maxPrice) {
	        return inventory.getAllBooks()
	        		.stream()
	                .filter(b -> b.getPrice() >= minPrice && b.getPrice() <= maxPrice)
	                .collect(Collectors.toList());
	    }
	    
	    public List<Book> sortByPriceAsc() {
	        return inventory.getAllBooks()
	                .stream()
	                .sorted(Comparator.comparing(Book::getPrice))
	                .collect(Collectors.toList());
	    }

	    public List<Book> sortByPriceDesc() {
	        return inventory.getAllBooks()
	                .stream()
	                .sorted(Comparator.comparing(Book::getPrice).reversed())
	                .collect(Collectors.toList());
	    }

	    public List<Book> sortByRatingAsc() {
	        return inventory.getAllBooks()
	                .stream()
	                .sorted(Comparator.comparing(Book::getRating))
	                .collect(Collectors.toList());
	    }
	    
	    public Map<String, List<Book>> groupByCategory() {
	        return inventory.getAllBooks()
	        		.stream()
	                .collect(Collectors.groupingBy(Book::getCategory));
	    }
	    public Map<String, List<Book>> groupByAuthor() {
	        return inventory.getAllBooks()
	        		.stream()
	                .collect(Collectors.groupingBy(Book::getAuthorName));
	    }
	    
	    //
	    
	    public int getTotalStock(){
	        return inventory.getAllBooks()
	                .stream()
	                .mapToInt(Book::getStock)
	                .sum();
	    }

	    public String getBestSeller(){
	        return inventory.getAllBooks()
	                .stream()
	                .max((a,b)->a.getSoldCount()-b.getSoldCount())
	                .map(Book::getTitle)
	                .orElse("None");
	    }
	    
		public void addBook(Book newBook) {
			this.inventory.addBook(newBook);
		}
		
}
