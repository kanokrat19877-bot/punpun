package bookstore.payment;

public class CashPayment extends Payment {
	
	public CashPayment (double amount) {
		this.amount = amount;
	}
	@Override
	public void Pay() {
		System.out.println("Paid " + amount + " with cash.");
	}
	
}
