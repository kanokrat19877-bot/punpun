package bookstore.model;

import java.time.LocalDate;
import java.util.*;
public class Order {
	private int orderId;
	private LocalDate date;
	private List<OrderItem> items;
	private double totalPrice;
	
	public Order() {}
	public Order(int orderId, List <OrderItem> items) {
		this.orderId = orderId;
		this.date = LocalDate.now();
		this.items = items;
		this.totalPrice = calculateTotal();
	}
	public int getOrderId() {
		return orderId;
	}
	public List<OrderItem> getItems() {
		return items;
	}
	public void setItems(List<OrderItem> items) {
		this.items = items;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public LocalDate getDate() {
		return date;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public double calculateTotal() {
		double total = 0;
		for(OrderItem item : items) {
			total += item.getSubtotal();
		}
		return total;
	}
	public Collection<Order> getBooks() {
		// TODO Auto-generated method stub
		return null;
	}
}
