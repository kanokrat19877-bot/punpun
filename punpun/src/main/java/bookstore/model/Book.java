package bookstore.model;

public class Book {
	private int bookId;
	private String title;
	private String author;
	private String category;
	private double price;
	private double rating = 0;
	private int stock = 0;
	private int soldCount = 0;
	private String imagePath;
	
	public Book() {
		
	}
	
	
	public Book(int bookId, String title, String author, String category, double price, double rating, int stock, int soldCount, String imgPath) {
		this.bookId = bookId;
		this.title = title;
		this.author = author;
		this.category = category;
		this.price = price;
		this.rating = rating;
		this.stock = stock;
		this.soldCount = soldCount;
		this.imagePath = imgPath;
	}
	public int getBookId() {
		return bookId;
	}
	public String getTitle() {
		return title;
	}
	public String getAuthorName() {
		return author;
	}
	public String getCategory() {
		return category;
	}
	public double getPrice() {
		return price;
	}
	public double getRating() {
		return rating;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public int getSoldCount() {
		return soldCount;
	}
	public void increaseSold(int qty) {
		soldCount += qty;
	}
	public String getInfo() {
		return title + "by" + author + "$" + price;
 	}
	public String getImagePath() {
		return "/resources/" + imagePath;
	}

}
