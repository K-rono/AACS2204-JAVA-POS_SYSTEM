package POS;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author qihong
 */
public class Product {
    private static final String[] categories = {"Horror","History","Mathematics","Programming"};
    private static final String[] ageRatings = {"G","PG-13","R"};
    public static final String filePath = "src/lastAssignedID.txt";
    public static int lastAssignedID = 1001;
    private int productID;
    private String productName;
    private String ratedAge;
    private String category;
    private double price;

    //Constructor for products that already exist in file
    public Product(int productID, String productName, String ratedAge, String category, double price) {
        this.productID = productID;
        this.productName = productName;
        this.ratedAge = ratedAge;
        this.category = category;
        this.price = price;
    }

    //Constructor for newly added products
    public Product(String productName, String ratedAge, String category, double price) {
        this.productName = productName;
        this.ratedAge = ratedAge;
        this.category = category;
        this.price = price;
        this.productID = lastAssignedID;
        lastAssignedID++;
    }

    //Setters
    public void setProductID(int productID) {
        this.productID = productID;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setRatedAge(String ratedAge) {
        this.ratedAge = ratedAge;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    //Getters
    public int getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }

    public String getRatedAge() {
        return ratedAge;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }
    
    public static String[] getCategoryList(){
        return categories;
    }
    
    public static String[] getAgeRatingList(){
        return ageRatings;
    }

    //Read LastAssignedID
    public static void readLastAssignedID() {
        final String filePath = "src/lastAssignedID.txt";
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            String line = reader.readLine();
            Product.lastAssignedID = Integer.parseInt(line);    
        }
        catch (IOException e){
            System.out.println("Error occured when reading lastAssignedID text file");
        }
    }
    
    //Write LastAssignedID
    public static void writeLastAssignedID(){
        
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))){
            writer.write(lastAssignedID);    
        }
        catch (IOException e){
            System.out.println("Error occured when writing to lastAssignedID text file");
        }
    }
    
    @Override
    public String toString() {
        return String.format("%-10s %-30s %-5s %10s", productID, productName, ratedAge, price);
    }

}
