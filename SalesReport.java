/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package POS;

/**
 *
 * @author laiyo
 */
import java.util.*;

public class SalesReport{ 
    
    // data field
    private int totalQty;
    private double totalRevenue;
    private final List<Receipt> receiptList;
    
    //constructor    
        public SalesReport(){
            this.receiptList = ReceiptController.readFile();
        }
        
        
 
    // method
    // calculate for product qty
        public Map<Integer, Integer> calculateProductQuantity(){
           
            Map<Integer, Integer> qtyMap = new HashMap<>();
            
            for (Receipt receipt : receiptList) {
            if (qtyMap.containsKey(receipt.getProductID())) {
                int buffer = qtyMap.get(receipt.getProductID());
                buffer += receipt.getQuantity();
                
                qtyMap.put(receipt.getProductID(), buffer);
            }
                
            else{
                qtyMap.put(receipt.getProductID(), receipt.getQuantity());
            }
            }
            return qtyMap;
        }
        
    // calculate for productSales
    public Map<Integer, Double> calculateProductSales() {
        
        Map<Integer, Double> productSalesMap = new HashMap<>();

        for (Receipt receipt : receiptList) {
            if (productSalesMap.containsKey(receipt.getProductID())) {
                double buffer = productSalesMap.get(receipt.getProductID());
                buffer += receipt.getPrice();
                
                productSalesMap.put(receipt.getProductID(), buffer);
            }
                
            else{
                productSalesMap.put(receipt.getProductID(), receipt.getPrice());
            }
        }
         return productSalesMap;
    }
    
    // total quantity
    public int getTotalQuantity() {
    Map<Integer, Integer> qtyMap = calculateProductQuantity();
    totalQty = 0;
    for (int quantity : qtyMap.values()) {
        totalQty += quantity;
    }
    
    return totalQty;
}
    
    // total revenue
    public double getTotalRevenue() {
    Map<Integer, Double> productSalesMap = calculateProductSales();
    totalRevenue = 0.0;
    for (double revenue : productSalesMap.values()) {
        totalRevenue += revenue;
    }
    
    return totalRevenue;
    }
    
    // generate Product Sales
    public void generateProductSalesReport() {
    InventoryAPI inventoryAPI = new Inventory();
    Map<Integer, Double> productSalesMap = calculateProductSales();
    Map<Integer, Integer> productQuantityMap = calculateProductQuantity();

    // Print the product sales report
    System.out.println("\n\nProduct Sales Report");
    System.out.println("--------------------");
    System.out.printf("%-10s %-20s %-15s %-15s%n", "Product ID", "Product Name", "Total Quantity", "Total Revenue");
    
    for (Map.Entry<Integer, Integer> entry : productQuantityMap.entrySet()) {
        int productID = entry.getKey();
        int productQty = entry.getValue();
        
        // Check if productSalesMap contains the productID
        if (productSalesMap.containsKey(productID)) {
            double productSales = productSalesMap.get(productID);
           
            String productName = inventoryAPI.getProduct(entry.getKey()).getProductName();
            System.out.printf("%-10d %-20s %-15d $%.2f\n", productID, productName, productQty, productSales);
        } 
        else {
            System.out.println("Product with ID " + productID + " not found in productSalesMap.");
        }
        System.out.println();
    }
}
    
    // generate Total Sales
    public void generateTotalSalesReport() {

        // Print the total sales report
        System.out.println("\n\nTotal Sales Report");
        System.out.println("------------------");
        System.out.printf("Total Sales Revenue: $%.2f\n", getTotalRevenue());
        System.out.printf("Total Item Sold: %d\n", getTotalQuantity());
        System.out.println();
    }
    
}
