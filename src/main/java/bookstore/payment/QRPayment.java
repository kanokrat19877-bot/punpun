package bookstore.payment;

public class QRPayment extends Payment {
	public QRPayment (double amount) {
		this.amount = amount;
	}
	@Override
	public void Pay() {
		System.out.println("Paid " + amount + " by QR Code.");
	}
}
