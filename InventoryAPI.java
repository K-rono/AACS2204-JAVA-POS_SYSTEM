package POS;

/**
 *
 * @author Qihong
 */
public interface InventoryAPI {
    public abstract Product getProduct(int productID);

    public abstract int getStockAmount(int ProductID);
}
