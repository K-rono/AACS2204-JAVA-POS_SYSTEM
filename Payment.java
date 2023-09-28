package POS;

/**
 *
 * @author Shek Jun Yi
 */
import java.util.List;
import java.util.Map;

public class Payment {

    // Constants for user types
    private static final int MEMBER = 1;
    private static final int GUEST = 2;

    // Constants for discount rates
    private final double DISCOUNT_RATE = 0.1;
    private final double EXTRA_RATE = 0.05;

    private double subTotal;        // total up all
    private double DiscountAmount;
    private double DiscountedTotal; // discounted total

    private final Cart cart;
    private final List<Product> items;
    private final Map<Product, Integer> quantity;

    //constructor
    public Payment(Cart cart) {
        this.cart = cart;
        this.items = cart.getItems(); // Populate items list from the Cart
        this.quantity = cart.getQtyByBookID(); // Populate quantity map from the Cart
    }

    // Method of calculations
    public void calculateTotal(int userType) {
        subTotal = 0;
        for (Product product : items) {
            subTotal += product.getPrice() * this.quantity.get(product);
        }
        calculateDiscount(userType); // Ensure discount calculation is always updated
    }

    private void calculateDiscount(int userType) {
        DiscountAmount = 0;
        DiscountedTotal = 0;
        // UserType = 1 = MEMBER, 2 = GUEST
        if (userType == MEMBER) {
            if (subTotal >= 200) {
                // Member with subtotal >= 200
                DiscountAmount = subTotal * (DISCOUNT_RATE + EXTRA_RATE);
            } else {
                // Member with subtotal < 200
                DiscountAmount = subTotal * DISCOUNT_RATE;
            }
        } else if (userType == GUEST && subTotal >= 200) {
            // Guest with subtotal >= 200
            DiscountAmount = subTotal * DISCOUNT_RATE;
        }
        DiscountedTotal = subTotal - DiscountAmount;
    }

    public void processCashPayment(double amountPaid, int paymentMethod) {
        processPayment(amountPaid, paymentMethod);
    }

    public void processOnlineTransferPayment(int paymentMethod) {
        processPayment(DiscountedTotal, paymentMethod);
    }

    private void processPayment(double amountPaid, int paymentMethod) {  // Payment Method: 1= Cash, 2= Online transfer
        if (paymentMethod == 1) {
            double change = amountPaid - DiscountedTotal;
            System.out.printf("Payment successful. Change: RM %.2f%n", change);
        } else {
            // The E-money must match the FinalTotal Amount
            System.out.println("Payment successful.");
        }
    }

    public void cancelPayment() {
        subTotal = 0;
        DiscountAmount = 0;
        DiscountedTotal = 0;
    }

    // Accessor
    public double getSubTotal() {
        return subTotal;
    }

    public double getDiscountAmount() {
        return DiscountAmount;
    }

    public double getDiscountedTotal() {
        return DiscountedTotal;
    }

    public List<Product> getItems() {
        return items;
    }

    public Map<Product, Integer> getQuantity() {
        return quantity;
    }

    public void displayAmount() {
        System.out.printf("%-20s: RM %.2f\n"
                + "%-20s: RM %.2f\n"
                + "%-20s: RM %.2f\n",
                "Total Amount", subTotal,
                "Discount Amount", DiscountAmount,
                "Final Amount", DiscountedTotal);
        System.out.println(OutputFormatter.printHorizontalLine(110));
    }

}
