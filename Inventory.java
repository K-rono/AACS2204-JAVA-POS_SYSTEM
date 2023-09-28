package POS;

/**
 *
 * @author qihong
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public final class Inventory implements InventoryAPI {

    private static final String STOCK_FILE_PATH = "src/Data/stock.txt";
    private static final String PRODUCT_FILE_PATH = "src/Data/product.txt";
    private final ArrayList<Product> products;
    private final Map<String, ArrayList<Product>> productsByCategory;
    private final Map<Integer, Integer> productStock;

    public Inventory() {
        products = new ArrayList<>();
        productsByCategory = new HashMap<>();
        productStock = new HashMap<>();
        readProductsFromFile();
        readStockFromFile();
    }

    public void readProductsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(PRODUCT_FILE_PATH))) {
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
        } catch (IOException e) {
            System.out.println("Error occured when reading products file");
        }
    }

    public void readStockFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(STOCK_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                int productID = Integer.parseInt(data[0]);
                int stock = Integer.parseInt(data[1]);

                productStock.put(productID, stock);
            }
        } catch (IOException e) {
            System.out.println("Error occured when reading stocks file");
        }
    }

    public void appendProductToFile(Product product) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCT_FILE_PATH, true))) {
            writer.write(product.getProductID()
                    + "," + product.getProductName()
                    + "," + product.getRatedAge()
                    + "," + product.getCategory()
                    + "," + product.getPrice());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error occured when writing to products text file");
        }
    }

    public void appendStockToFile(Product product, int qty) {
        int productID = product.getProductID();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STOCK_FILE_PATH, true))) {
            writer.write(productID + "," + qty);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error occured when writing to stocks text file");
        }
    }

    public void updateProductToFile() {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCT_FILE_PATH))) {
            for (Product product : products) {
                writer.write(product.getProductID()
                        + "," + product.getProductName()
                        + "," + product.getRatedAge()
                        + "," + product.getCategory()
                        + "," + product.getPrice());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error occured when writing to products text file");
        }

    }

    public void updateStockToFile() {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STOCK_FILE_PATH))) {
            for (Product product : products) {
                writer.write(product.getProductID() + "," + productStock.get(product.getProductID()));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error occured when writing to stocks text file");
        }

    }

    public void addNewProduct(String productName, String ratedAge, String category, double price, int qty) {
        Product product = new Product(productName, ratedAge, category, price);
        products.add(product);
        productsByCategory.get(category).add(product);
        productStock.put(product.getProductID(), qty);
        Product.writeLastAssignedID();
        appendProductToFile(product);
        appendStockToFile(product, qty);
    }

    //to add Stock of each individual item
    public void updateStock(int productID, int qty) {
        if (productStock.containsKey(productID)) {
            int currentStock = productStock.get(productID);
            productStock.put(productID, qty + currentStock);
        } else {
            productStock.put(productID, qty);
        }

        updateStockToFile();
    }

    //To deduct stock after payments
    public void updateStock(List<Product> items, Map<Product, Integer> quantity) {
        for (Product product : items) {
            int currentStock = productStock.get(product.getProductID());
            int stockToBeDeducted = quantity.get(product);
            productStock.put(product.getProductID(), currentStock - stockToBeDeducted);
        }

        updateStockToFile();
    }

    public ArrayList<String> getCategory() {
        Set<String> categorySet = productsByCategory.keySet();
        return new ArrayList<>(categorySet);
    }

    public ArrayList<Product> getAllProduct() {
        return products;
    }

    @Override
    public ArrayList<Product> getProductList(String category) {
        return productsByCategory.get(category);
    }

    @Override
    public Product getProduct(int productID) {
        for (Product product : products) {
            if (productID == product.getProductID()) {
                return product;
            }
        }
        //ProductID doesn't exist
        throw new IllegalArgumentException("ProductID doesn't exist");
    }

    @Override
    public int getStockAmount(int ProductID) {
        return productStock.get(ProductID);
    }
}
