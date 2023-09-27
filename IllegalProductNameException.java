/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package POS;

/**
 *
 * @author User
 */
public class IllegalProductNameException extends IllegalArgumentException {

    public IllegalProductNameException() {
    }

    public IllegalProductNameException(String s) {
        super(s);
    }

    public IllegalProductNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalProductNameException(Throwable cause) {
        super(cause);
    }
    
}
