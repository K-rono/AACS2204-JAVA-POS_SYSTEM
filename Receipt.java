package POS;

public class Receipt {


    private int receiptID;
    private int productID;
    private long second;
    private double price;
    private int quantity;

    public Receipt(int receiptID,long second,int productID,int quantity,double price) {
        this.receiptID = receiptID;
        this.productID = productID;
        this.second = second;
        this.price = price;
        this.quantity = quantity;
    }



    public int getReceiptID() {
        return receiptID;
    }

    public void setReceiptID(int receiptID) {
        this.receiptID = receiptID;
    }

    public long getSecond() {
        return second;
    }

    public void setSecond(long second) {
        this.second = second;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    
}
