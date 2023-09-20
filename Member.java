package POS;

     
/**
 *
 * @author qihong
 */
public class Member extends User {
    private int loyaltyPoints;

    public Member(int loyaltyPoints, String username, String userID, String userPassword) {
        super(username, userID, userPassword);
        this.loyaltyPoints = loyaltyPoints;
    }
    
    //50% of billAmount rounded up = Loyalty points acquired
    public void addLoyaltyPoints(double billAmount){
        this.loyaltyPoints += Math.ceil(billAmount * 0.5);
    }

    //Setters and Getters
    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }
    
}
