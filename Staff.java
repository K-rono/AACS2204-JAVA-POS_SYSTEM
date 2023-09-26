/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package POS;

/**
 *
 * @author User
 */
public class Staff extends User {
    private String position;

    public Staff(String position, String username, String userID, String userPassword) {
        super(username, userID, userPassword);
        this.position = position;
    }

    //Getter and Setter
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
   
}
