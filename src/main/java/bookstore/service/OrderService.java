package bookstore.service;

import bookstore.model.*;
import java.util.*;

public class OrderService {
    private List<Order> orders = new ArrayList<>();

    public Order checkout(Customer customer) {
    	Order order = customer.checkout();
        orders.add(order);
        return order;
    }
    public List<Book> getBestSellingBooks() {
        Map<Book,Integer> count = new HashMap<>();
		for(Order order : orders){
            for(OrderItem item : order.getItems()){
                Book book = item.getbook();
                count.put(book,
                        count.getOrDefault(book,0) + item.getQuantity());
            }
        }
        List<Book> result = new ArrayList<>(count.keySet());
        result.sort((b1,b2) -> count.get(b2) - count.get(b1));
        return result;
    }
}