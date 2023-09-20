/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author laiyo
 */
// library
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SalesReport {
    
    // data field
    int totalQuantity;
    double totalRevenue;
 
    // methods    
    // read from text file
    public static ArrayList<Receipt> readFile(String filePath){
            ArrayList<Receipt> receiptList = new ArrayList<>();

          try{
            
             // open file using FileReader and wrap it using BufferedReader
             FileReader fileReader = new FileReader(filePath);
             BufferedReader bufferedReader = new BufferedReader(fileReader);

             // read the content of file until EOF
             String line;
             while ((line = bufferedReader.readLine()) != null) {
                 
                 // split the line by commas to get values
                 String[] values = line.split(",");
                 
                 // convert the values to appropriate data types
                 int receiptID = Integer.parseInt(values[0]);
                 long second = Long.parseLong(values[1]);
                 
                 // create a list of products
                 List<Product> items = new ArrayList<>();
                 for (int i = 2; i < values.length; i += 3) {
                     String name = values[i];
                     int qty = Integer.parseInt(values[i+1]);
                     double price = Double.parseDouble(values[i+2]);
                     items.add(new Product(name,qty,price));
                 }
                 
                 // create a Receipt object and add to ArrayList
                 Receipt receipt = new Receipt(receiptID, second, items);
                 receiptList.add(receipt);
                 
                }
     
          // close file
           bufferedReader.close();
            } catch (IOException e) {
              System.out.println("Error occured when reading receipt text file!!");
         }
          return receiptList;
        }
    
    // mapping and calculate for productSales
    public static Map<String, SalesReport> calculateProductSales(List<Receipt> receiptList) {
        Map<String, SalesReport> productSalesMap = new HashMap<>();

        for (Receipt receipt : receiptList) {
            for (Product product : receipt.getItems()) {
                String productName = product.getProduct();
                int quantity = product.getQty();
                double price = product.getPrice();

                if (!productSalesMap.containsKey(productName)) {
                    productSalesMap.put(productName, new SalesReport());
                }

                SalesReport salesData = productSalesMap.get(productName);
                salesData.totalQuantity += quantity;
                salesData.totalRevenue += quantity * price;
            }
        }

        return productSalesMap;
    }
    
    // generate Product Sales
    public static void generateProductSalesReport(ArrayList<Receipt> receiptList) {
        Map<String, SalesReport> productSalesMap = calculateProductSales(receiptList);

        // Print the product sales report
        System.out.println("Product Sales Report");
        System.out.println("--------------------");
        System.out.printf("%-20s %-15s %-15s%n", "Product Name", "Total Quantity", "Total Revenue");

        for (Map.Entry<String, SalesReport> entry : productSalesMap.entrySet()) {
            String productName = entry.getKey();
            SalesReport salesData = entry.getValue();

            System.out.printf("%-20s %-15d $%.2f%n", productName, salesData.totalQuantity, salesData.totalRevenue);
        }
    }
    
    // calculate Total Sales
    public static double calculateTotalSales(ArrayList<Receipt> receiptList) {
        double totalSalesRevenue = 0;

        for (Receipt receipt : receiptList) {
            for (Product product : receipt.getItems()) {
                totalSalesRevenue += product.getPrice() * product.getQty();
            }
        }

        return totalSalesRevenue;
    }
    
    // generate Total Sales
    public static void generateTotalSalesReport(ArrayList<Receipt> receiptList) {
        double totalSalesRevenue = calculateTotalSales(receiptList);

        // Print the total sales report
        System.out.println("Total Sales Report");
        System.out.println("------------------");
        System.out.printf("Total Sales Revenue: $%.2f%n", totalSalesRevenue);
        System.out.println();
    }
   
    // nested static class
    public static class Receipt {
        
        // data field
        private int id;
        private List<Product> items;
        private long second;
        
        // constructor
        public Receipt(int id, long second, List<Product> items) {
            this.id = id;
            this.second = second;
            this.items = items;
        }
        
        // getter
        public int getId(){
            return id;
        }
        
        public long getSecond(){
            return second;
        }
        
        public List<Product> getItems(){
            return items;
        }
    }
    
    // nested static class
    public static class Product {
        
        // data field
        private int qty;
        private String product;
        private double price;
        
        // constructor
        public Product(String product, int qty, double price) {
            this.product = product;
            this.qty = qty;
            this.price = price;
        }
        
        // getter
        public String getProduct(){
            return product;
        }
        
        public int getQty(){
            return qty;
        }
        
        public double getPrice(){
            return price;
        }
    }
    
     public static void main(String[] args) {
        String filePath = "src/receipt.txt";
        ArrayList<Receipt> receiptList = readFile(filePath);

        generateTotalSalesReport(receiptList);
        generateProductSalesReport(receiptList);
    }
}

