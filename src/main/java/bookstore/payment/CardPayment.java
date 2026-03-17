package bookstore.payment;

public class CardPayment extends Payment {
	private String cardNumber;
	
	public CardPayment(String cardNumber, double amount) {
		this.cardNumber = cardNumber;
		this.amount = amount;
	}
	
	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	@Override
	public void Pay() {
		System.out.println("Paid " + amount + "by Card" + cardNumber);
	}
}
