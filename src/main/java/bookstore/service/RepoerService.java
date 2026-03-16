package bookstore.service;

import bookstore.model.*;
import java.util.*;
import java.util.stream.*;

public class RepoerService {
	   private List<Order> orders;
	   
	   public RepoerService(List<Order> orders){
	        this.orders = orders;
	    }
	    public List<Order> bestSellingBooks(){
	    return orders.stream()
	    		.flatMap(o -> o.getBooks().stream())
	            .collect(Collectors.groupingBy(b -> b, Collectors.counting()))
	            .entrySet()
	            .stream()
	            .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
	            .map(Map.Entry::getKey)
	            .collect(Collectors.toList());
	    }
	    public Map<String, Long> salesByCategory() {
	        return orders.stream()
	                .flatMap(order -> order.getItems().stream()) 
	                .map(OrderItem::getbook)                     
	                .collect(Collectors.groupingBy(              
	                        Book::getCategory,
	                        Collectors.counting()));
	    }
}

