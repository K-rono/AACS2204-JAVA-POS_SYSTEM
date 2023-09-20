package POS;

/**
 *
 * @author qihong
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {
    
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int choice = 0;
        
        do{
            displayLogo();
            displayLoginMenu();
            
            try{
                choice = input.nextInt();
                input.nextLine(); //Consume CR

                switch (choice) {
                    case 1 -> {
                        promptID();
                        String userID = input.nextLine();
                        promptPassword();
                        String userPassword = input.nextLine();
                        
                        LoginSystem.readCredentialsFile(1);
                        if(LoginSystem.authentication(userID, userPassword)){
                            System.out.println("success");
                        }
                        else{
                            System.out.println("fail");
                        }
                    }
                    case 2 -> {
                        promptID();
                        String userID = input.nextLine();
                        promptPassword();
                        String userPassword = input.nextLine();
                        
                        LoginSystem.readCredentialsFile(0);
                        if(LoginSystem.authentication(userID, userPassword)){
                            System.out.println("success");
                        }
                        else{
                            System.out.println("fail");
                        }
                    }
                    case 3 -> {
                        
                    }
                    case 4 -> {
                    }
                    default -> {
                        System.out.println(OutputFormatter.OUT_OF_RANGE_ERROR_MSG);
                        OutputFormatter.PressToCont();
                        OutputFormatter.clearScreen();
                    }
                }
            }catch(Exception e){
                System.out.println(OutputFormatter.INVALID_INPUT_MSG);
                OutputFormatter.PressToCont();
                input.nextLine();
                OutputFormatter.clearScreen();
            }
                
        }while(choice < 1 || choice > 4);
        
    }
    
    public static void displayLogo(){
        System.out.println(OutputFormatter.printHorizontalLine(15));
        System.out.println("EVA BookStore");
        System.out.println(OutputFormatter.printHorizontalLine(15));
    }
    
    public static void displayLoginMenu(){
        System.out.println(OutputFormatter.printHorizontalLine(25));
        System.out.printf("%-25s\n", "1. Member Login");
        System.out.printf("%-25s\n", "2. Staff Login");
        System.out.printf("%-25s\n", "3. Continue as Guest");
        System.out.printf("%-25s\n", "4. Quit");
        System.out.println(OutputFormatter.printHorizontalLine(25));
        System.out.print("INPUT >>> ");
    }
    
    public static void promptID(){
        System.out.print("\nID :");
    }
    
    public static void promptPassword(){
        System.out.print("\nPassword :");
    }
    
    public void draft_menu(){
        Inventory inventory = new Inventory();
        inventory.readProductsFromFile();
        inventory.readStockFromFile();
        
        Set<String> categorySet = inventory.getCategory();
        List<String> categoryList = new ArrayList<>(categorySet);
        
        int i = 1;
        for(String category : categoryList){        
            System.out.print(i++ + " ");
            System.out.println(category);
        }
        
        System.out.print("Select a category >>");
        int categoryIndex = new Scanner(System.in).nextInt();
         List<Product> productsInCategory = inventory.getProductList(categoryList.get(categoryIndex - 1));
        
        System.out.printf("%-10s %-30s %-5s %-10s\n","Book ID","Title","Age","Price (RM)");
        for(Product p : productsInCategory){
            System.out.println(p.toString());
        }
    }
}
