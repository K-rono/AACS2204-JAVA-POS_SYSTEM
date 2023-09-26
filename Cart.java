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
    
    public void addItemToCart(int productID,int quantity) {
        Product product = inventory.getProduct(productID);
   
        int stockQty = inventory.getStockAmount(product.getProductID());
        int currentQty = this.quantity.get(product);

        if (stockQty >= quantity + currentQty) {

            if (this.quantity.containsKey(product)) {

                this.quantity.put(product, currentQty + quantity);

            } else {
                items.add(product);
                this.quantity.put(product, quantity);
            }
        } else { // (stockQty < bookQty + currentQty) 

            if (this.quantity.containsKey(product)) {
                System.out.println("You have currently added this book to your cart and have a total of " + currentQty + "books.");
            }
            System.out.println("There are only " + stockQty + "  copies of this book left.");
        }

    }

    public void modifyQty(Product product, int bookQty, int ModifyType) {
        if (this.quantity.containsKey(product)) {

            int currentQty = this.quantity.get(product);

            if (bookQty == currentQty) {
                items.remove(product);
                this.quantity.remove(product);
                System.out.println("The Book \"" + product.getProductName() + "\" is remove from Cart.");
            } else if (bookQty <= currentQty) {
                this.quantity.put(product, currentQty - bookQty);
            } else {
                throw new IllegalArgumentException("! Quantity entered more than current quantity in cart !");
            }
        } else {
            System.out.println("This book has not been added to the Cart.");
        }
    }

    public void removeItem(Product product, int quantity) {

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
            System.out.println(" << Cart is empty. No items added. >> ");

        } else {
            int i = 1;
            System.out.println("                                 Cart List                                    ");
            System.out.println("------------------------------------------------------------------------------");
            System.out.printf("%-5s%-20s%-15s%-15s%-15s%-15s\n",
                    "Bil", "Book Name", "Rated Age", "Category", "Price (RM)", "Quantity");
            System.out.println("------------------------------------------------------------------------------");
            for (Product product : items) {
                int qty = this.quantity.get(product);
                System.out.printf("%-5d%-20s%-15s%-15s%-15.2f%-15d\n",
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