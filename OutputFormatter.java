/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package POS;

/**
 *
 * @author qihong
 */
import java.util.Scanner;

public class OutputFormatter {
    final static String TOP_LEFT_CORNER = "\u2554";
    final static String TOP_RIGHT_CORNER = "\u2557";
    final static String BOTTOM_LEFT_CORNER = "\u255A";
    final static String BOTTOM_RIGHT_CORNER = "\u255D";
    final static String HORIZONTAL_LINE = "\u2550";
    final static String VERTICAL_LINE = "\u2551";
    final static String VERTICAL_MID_CUT_LEFT = "\u2563";
    final static String VERTICAL_MID_CUT_RIGHT = "\u2560";
    
    final static String OUT_OF_RANGE_ERROR_MSG = "! INPUT OUT OF RANGE !";
    final static String INVALID_INPUT_MSG = "! INVALID INPUT !";
    final static String PRESS_TO_CONT = "Press ENTER To Continue...";
    
    public static String printHorizontalLine(int times){
        StringBuilder line = new StringBuilder();
        for(int i = 0; i < times; i++){
            line.append(HORIZONTAL_LINE);
        }
        return line.toString();
    }
    
    public static void PressToCont(){
        System.out.println(PRESS_TO_CONT);
        new Scanner(System.in).nextLine();
    }
    
    //fake clear screen method :D
    public static void clearScreen(){
        for(int i = 0; i < 15; i++){
            System.out.println(" ");
        }
    }
}
