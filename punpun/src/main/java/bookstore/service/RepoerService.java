/*package bookstore.service;

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
}*/

package bookstore.service;

import bookstore.model.*;
import java.util.*;
import java.util.stream.*;

public class RepoerService {

    private List<Order> orders;
    public RepoerService(List<Order> orders) {
        //ถ้าส่ง null มา ให้กลายเป็น List ว่างแทน จะได้ไม่พัง
        this.orders = (orders != null) ? orders : new ArrayList<>();
    }
    
    //TOP SELLING BOOKS
    public List<Book> bestSellingBooks(){
        return orders.stream()
                .flatMap(o -> o.getItems().stream())
                .collect(Collectors.groupingBy(
                        OrderItem::getbook,
                        Collectors.summingInt(OrderItem::getQuantity)
                ))
                .entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue() - e1.getValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    //SALES BY CATEGORY (ใช้ทำกราฟ)
    public Map<String, Long> salesByCategory() {
        return orders.stream()
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(
                        item -> item.getbook().getCategory(),
                        Collectors.summingLong(OrderItem::getQuantity)
                ));
    }

    //TOTAL SALES
    public double getTotalSales(){
        return orders.stream()
                .flatMap(o -> o.getItems().stream())
                .mapToDouble(item ->
                        item.getbook().getPrice() * item.getQuantity()
                )
                .sum();
    }

    //TOP 3
    public List<Map.Entry<Book, Integer>> getTop3Books(){

        Map<Book, Integer> map = orders.stream()
                .flatMap(o -> o.getItems().stream())
                .collect(Collectors.groupingBy(
                        OrderItem::getbook,
                        Collectors.summingInt(OrderItem::getQuantity)
                ));

        return map.entrySet()
                .stream()
                .sorted((a,b) -> b.getValue() - a.getValue())
                .limit(3)
                .collect(Collectors.toList());
    }
}

