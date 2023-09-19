package POS;

/**
 *
 * @author qihong
 */
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Inventory {
    private static final String STOCK_FILE_PATH = "src/stock.txt";
    private static final String PRODUCT_FILE_PATH = "src/product.txt";
    private final List<Product> products;
    private final Map<String, List<Product>> productsByCategory;
    private final Map<Integer, Integer> productStock;
    
    public Inventory(){
        products = new ArrayList<>();
        productsByCategory = new HashMap<>();
        productStock = new HashMap<>();
    }
    
    public void readProductsFromFile(){
        try(BufferedReader reader = new BufferedReader(new FileReader(PRODUCT_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                int productID = Integer.parseInt(data[0]);
                String productName = data[1];
                String ratedAge = data[2];
                String category = data[3];
                double price = Double.parseDouble(data[4]);

                Product product = new Product(productID, productName, ratedAge, category, price);
                products.add(product);
                productsByCategory.putIfAbsent(category, new ArrayList<>());
                productsByCategory.get(category).add(product);
            }
        }
        catch(IOException e){
            System.out.println("Error occured when reading products file");
        }
    }
    
    public void readStockFromFile(){
        try(BufferedReader reader = new BufferedReader(new FileReader(STOCK_FILE_PATH))) {
            String line;
            while((line = reader.readLine()) != null){
                String[] data = line.split(",");
                int productID = Integer.parseInt(data[0]);
                int stock = Integer.parseInt(data[1]);
                
                productStock.put(productID, stock);
            }
        }
        catch(IOException e){
            System.out.println("Error occured when reading stocks file");
        }
    }
    
    public void writeProductsToFile(Product product){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCT_FILE_PATH))){
            writer.write(product.getProductID()
                    + "," + product.getProductName()
                    + "," + product.getRatedAge()
                    + "," + product.getCategory()
                    + "," + product.getPrice());
            writer.newLine();
        }
        catch(IOException e){
            System.out.println("Error occured when writing to products text file");
        }
    }
    
    public void writeStockToFile(int productID,int qty){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(STOCK_FILE_PATH))){
            writer.write(productID + "," + qty);
            writer.newLine();
        }
        catch(IOException e){
            System.out.println("Error occured when writing to stocks text file");
        }
    }
    
    public void addNewProduct(String productName, String ratedAge, String category, double price){
        Product product = new Product(productName,ratedAge,category,price);
        productsByCategory.get(category).add(product);
        Product.writeLastAssignedID();
        writeProductsToFile(product);
    }
    
    public void addStock(int productID, int qty){
        if(productStock.containsKey(productID)){
            int currentStock = productStock.get(productID);
            productStock.put(productID, qty + currentStock);
        }
        else{
            productStock.put(productID, qty);
        }
        
        writeStockToFile(productID,productStock.get(productID));
    }
    
    public List<Product> getProductList(String category){
        return productsByCategory.get(category);
    }
    
    public Set<String> getCategory(){
        return productsByCategory.keySet();
    }
    
    public Product getProduct(int productID){
        for(Product product : products){
            if(productID == product.getProductID()){
                return product;
            }
        }
        //productID not present
        return null;
    }

    public int getStockAmount(int ProductID){
        return productStock.get(ProductID);
    }
    
}
