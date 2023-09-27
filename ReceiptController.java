
package POS;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;


public class ReceiptController {

    private static int receiptID = 30000;
    private Payment payment;
    private long timeInSecond;
    private final Map<Product, Integer> quantity;
    
    //constructer
    public ReceiptController(Payment payment) {
        this.payment = payment;
        Date date = new Date();
        timeInSecond = date.getTime(); 
        quantity = payment.getQuantity();
        receiptID++;
    }
    public ReceiptController(){
        quantity = null;
    }

    public static int getReceiptID() {
        return receiptID;
    }

    public static void setReceiptID(int receiptID) {
        ReceiptController.receiptID = receiptID;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public long getTimeInSecond() {
        return timeInSecond;
    }

    public void setTimeInSecond(long timeInSecond) {
        this.timeInSecond = timeInSecond;
    }
 
    
    //method 
    
    //display the receipt
    public void generateReceipt() {
        // Get the current date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // Display the receipt header
        System.out.println("Receipt");
        System.out.println("===============================");
        System.out.println("Receipt ID: " + receiptID);
        System.out.println("Date: " + dateFormat.format(timeInSecond));
        System.out.println("------------------------------");

        // Display cart items and quantities
        System.out.println("Item\t\tQuantity\tPrice");

        for (Product product : payment.getItems()) {
            double price = product.getPrice()* quantity.get(product);
            System.out.printf("%-15s %-10d $%.2f%n", product.getProductName(),quantity.get(product),price);
        }

        // Display receipt totals
        System.out.println("------------------------------");
        System.out.printf("Subtotal:            $%.2f%n", payment.getSubTotal());
        System.out.printf("Discount:            $%.2f%n", payment.getDiscountAmount());
        System.out.println("------------------------------");
        System.out.printf("Total:               $%.2f%n", payment.getDiscountedTotal());
        System.out.println("===============================");
    }
    
    //record down the receipt in a text file
    public void writeToFile() {
        String filename = "src/Data/receipt.txt"; //file path
        //writting
        try {
            FileWriter fr = new FileWriter(filename, true);
            BufferedWriter br = new BufferedWriter(fr);
            PrintWriter pr = new PrintWriter(br);
            String IDDate = String.format("%d,%d", receiptID,timeInSecond);
            pr.print(IDDate);
            for (Product product : payment.getItems()) {
                int productID = product.getProductID();
                double price = product.getPrice()* quantity.get(product);
                String receiptData = String.format(",%s,%d,%lf",productID,quantity.get(product),price);
                pr.print(receiptData);
            } 
            pr.print("\n");
            pr.close();
            br.close();
            fr.close();

            System.out.println("Receipt saved to " + filename);
        } catch (IOException e) { 
            System.out.println("An error occurred while writing to the file.");
        }
    }
    
public static ArrayList<Receipt> readFile(){
            String filename = "src/Data/receipt.txt"; //file path
            ArrayList<Receipt> receiptList = new ArrayList<>();

          try{
            
             // open file using FileReader and wrap it using BufferedReader
             FileReader fileReader = new FileReader(filename);
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
                
                 for (int i = 2; i < values.length; i += 3) {
                     int itemID = Integer.parseInt(values[i]);
                     int qty = Integer.parseInt(values[i+1]);
                     double price = Double.parseDouble(values[i+2]);
                     receiptList.add(new Receipt(receiptID,second,itemID,qty,price));
                 }
                
                }
     
          // close file
           bufferedReader.close();
            } catch (IOException e) {
              System.out.println("Error occured when reading receipt text file!!");
         }
          return receiptList;
    }
}
