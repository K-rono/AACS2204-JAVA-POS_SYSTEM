package POS;

/**
 *
 * @author Qihong
 */
public interface InventoryAPI {
    public ArrayList<Product> getProductList(String category);

    public abstract Product getProduct(int productID);

    public abstract int getStockAmount(int ProductID);
}
