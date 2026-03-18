package bookstore.model;

public class CartSession {

    private static Cart cart = new Cart();

    public static Cart getCart(){
        return cart;
    }

}