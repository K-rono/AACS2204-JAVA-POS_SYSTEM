package POS;

/**
 *
 * @author Shek Jun Yi
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart {

    private final List<Product> items;
    private final Map<Product, Integer> quantity;
    private final InventoryAPI inventory;

    public Cart(Inventory inventory) {
        this.quantity = new HashMap<>();
        this.items = new ArrayList<>();
        this.inventory = inventory;
    }

    public void addItemToCart(int productID, int quantity, ArrayList<Product> productsInCategory) {
        Product product = null;

        for (Product p : productsInCategory) {
            if (p.getProductID() == productID) {
                product = p;
            }
        }
        if (product == null) {
            throw new IllegalArgumentException("Invalid product ID entered");
        }

        int stockQty = inventory.getStockAmount(product.getProductID());
        int currentQty = 0;
        if (this.quantity.containsKey(product)) {
            currentQty = this.quantity.get(product);
        }

        if (stockQty >= quantity + currentQty) {

            if (this.quantity.containsKey(product)) {

                this.quantity.put(product, currentQty + quantity);

            } else {
                items.add(product);
                this.quantity.put(product, quantity);
            }
        } else { // (stockQty < bookQty + currentQty) 

            if (this.quantity.containsKey(product)) {
                System.out.println("\nYou currently have " + currentQty + " of this book added  to your cart.");
            }
            System.out.println("\nThere are only " + stockQty + " copies of this book left.");
        }

    }

    public void modifyQty(int index, int quantity) {
        Product product = items.get(index - 1);
        int currentQty = this.quantity.get(product);
        int modifiedQuantity = quantity + currentQty;
        int stockQty = inventory.getStockAmount(product.getProductID());
        
        if (modifiedQuantity == 0) {
            items.remove(product);
            this.quantity.remove(product);
            System.out.println("The Book \"" + product.getProductName() + "\" is removed from Cart.");

        } else if (modifiedQuantity > 0 && modifiedQuantity < stockQty) {
            this.quantity.put(product, modifiedQuantity);

        } else if (modifiedQuantity > stockQty) {
            throw new QuantityMoreThanStockException("! Quantity entered more than available stock !");
            
        } else {
            throw new QuantityOutOfRangeException("! Quantity entered more than current quantity in cart !");
        }
    }

    public void removeItem(int productID, int quantity) {
        Product product;

        try {
            product = inventory.getProduct(productID);

        } catch (IllegalArgumentException e) {
            System.out.println("Invalid Product ID entered");
            return;
        }

        if (this.quantity.containsKey(product)) {

            int currentQty = this.quantity.get(product);

            items.remove(product);
            this.quantity.remove(product);
            System.out.println("The Book \"" + product.getProductName() + "\" is remove from Cart.");

        } else {
            System.out.println("This book has not been added to the Cart.");
        }
    }

    // Used when a user cancels a book purchase
    public void clearCart() {
        items.clear();
        this.quantity.clear();
    }

    // Method Display
    public void displayCartContents() {
        if (items.isEmpty()) {
            throw new IllegalStateException("No items has been added to cart yet");

        } else {
            int i = 1;
            System.out.println("                                 Cart List                                    ");
            System.out.println(OutputFormatter.printHorizontalLine(110));
            System.out.printf("%-5s%-40s%-15s%-15s%-15s%-15s\n",
                    "Bil", "Book Name", "Rated Age", "Category", "Price (RM)", "Quantity");
            System.out.println(OutputFormatter.printHorizontalLine(110));
            for (Product product : items) {
                int qty = this.quantity.get(product);
                System.out.printf("%-5d%-40s%-15s%-15s%-15.2f%-15d\n",
                        i++, product.getProductName(), product.getRatedAge(),
                        product.getCategory(), product.getPrice(), qty);
            }
        }
    }

    // accessor
    public int getTotalItemCount() {
        int totalQty = 0;
        for (int qty : this.quantity.values()) {
            totalQty += qty;
        }
        return totalQty;
    }

    public List<Product> getItems() {
        return items;           // Contain all item (Product) in Cart
    }

    public Map<Product, Integer> getQtyByBookID() {
        return this.quantity;     // Contain all Qty (By Product) in Cart
    }

}
