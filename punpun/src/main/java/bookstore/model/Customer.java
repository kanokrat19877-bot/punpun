/*package bookstore.model;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
	private Cart cart;
	private List<Order> OrderHistory;
	
	public Customer() {}
	public Customer (String id, String name, String email, String password, Cart cart, List <Order> OrderHistory) {
		super();
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
	
}*/

package bookstore.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
    private Cart cart;
    private List<Order> OrderHistory;
    private final String HISTORY_FILE = "src/main/java/resources/CustomerHistory.txt";

    // Constructor หลัก
    public Customer(String id, String name, String email, String password, Cart cart, List<Order> OrderHistory) {
        super(id, name, email, password); // ส่งค่าไปเก็บที่คลาสแม่ (User)
        this.cart = (cart != null) ? cart : new Cart();
        this.OrderHistory = (OrderHistory != null) ? OrderHistory : new ArrayList<>();
        loadHistoryFromFile(); // โหลดประวัติการซื้อจากไฟล์ทันทีเมื่อ Object ถูกสร้าง
    }

    // ✅ Method: โหลดประวัติการซื้อจากไฟล์ TXT
    private void loadHistoryFromFile() {
        File file = new File(HISTORY_FILE);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] data = line.split(",");
                // Format ในไฟล์: UserID, BookTitle, Price, Quantity
                if (data.length >= 4 && data[0].equals(this.getUserId())) {
                    String title = data[1];
                    double price = Double.parseDouble(data[2]);
                    int qty = Integer.parseInt(data[3]);

                    // สร้างข้อมูลจำลองเพื่อนำไปแสดงผลในหน้า UI
                    Book dummyBook = new Book(0, title, "", "", price, 0, 0, 0);
                    OrderItem item = new OrderItem(dummyBook, qty, price * qty);
                    
                    List<OrderItem> items = new ArrayList<>();
                    items.add(item);
                    OrderHistory.add(new Order(OrderHistory.size() + 1, items));
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading history: " + e.getMessage());
        }
    }

    // ✅ Method: Checkout (เปลี่ยนของในตะกร้าเป็น Order และบันทึกลงไฟล์)
    public Order checkout() {
        if (cart == null || cart.getCartItem().isEmpty()) {
            return null;
        }

        // 1. แปลง CartItem เป็น OrderItem
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem item : cart.getCartItem()) {
            orderItems.add(new OrderItem(
                item.getBook(), // ดึงก้อน Book ออกมา
                item.getQuantity(), 
                item.getSubtotal()
            ));
        }

        // 2. สร้าง Order ใหม่
        Order newOrder = new Order(OrderHistory.size() + 1, orderItems);
        
        // 3. เก็บลง Memory (List) และบันทึกลงไฟล์ TXT
        OrderHistory.add(newOrder);
        saveOrderToFile(this, newOrder);

        // 4. ล้างตะกร้า
        cart.getCartItem().clear();

        return newOrder;
    }

    // ✅ Method: บันทึกข้อมูลลงไฟล์ TXT (เขียนต่อท้ายเสมอ)
    public void saveOrderToFile(Customer customer, Order order) {
        File file = new File(HISTORY_FILE);
        if (file.getParentFile() != null) file.getParentFile().mkdirs();

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)))) {
            for (OrderItem item : order.getItems()) {
                // บันทึก: IDลูกค้า, ชื่อหนังสือ, ราคา, จำนวน
                out.println(customer.getUserId() + "," + 
                            item.getbook().getTitle() + "," + 
                            item.getbook().getPrice() + "," + 
                            item.getQuantity());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- Getter / Setter ---
    public List<Order> viewOrderHistory() {
        return OrderHistory;
    }

    public Cart getCart() {
        return cart;
    }

    public void addToCart(Book book) {
        if (this.cart == null) this.cart = new Cart();
        cart.addBook(book);
    }
}