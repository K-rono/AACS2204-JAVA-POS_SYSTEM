package POS;

/**
 *
 * @author qihong
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.InputMismatchException;

public class Main {

    private static Object currentSessionUser;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int choice = 0;

        do {
            displayLogo();
            displayLoginMenu();

            try {
                choice = input.nextInt();
                ConsumeCR(input);
                
                if(choice < 1 || choice > 4){
                    throw new IllegalArgumentException(OutputFormatter.OUT_OF_RANGE_ERROR_MSG);
                }
            } catch (InputMismatchException e) {
                System.out.println(OutputFormatter.INVALID_INPUT_MSG);
                OutputFormatter.PressToCont();
                ConsumeCR(input);
                OutputFormatter.clearJavaConsoleScreen();

            } catch (IllegalArgumentException e) {
                System.out.println(OutputFormatter.OUT_OF_RANGE_ERROR_MSG);
                OutputFormatter.PressToCont();
                OutputFormatter.clearJavaConsoleScreen();
            }

            switch (choice) {
                case 1, 2 -> {
                    int retry;

                    do {
                        boolean LoginSuccess = Login(input, choice);
                        if (LoginSuccess) {
                            retry = 0; //reset 'retry' value if user succesfully log in AFTER failing one or more times
                            OutputFormatter.clearJavaConsoleScreen();
                            MainMenu(input);
                        } else {
                            retry = IncorrectLogin(input);
                            //To loop LoginMenu() if user chooses to NOT retry
                            if(retry == 2){
                                choice = 0;
                                OutputFormatter.PressToCont();
                                OutputFormatter.clearJavaConsoleScreen();
                                break;
                            }
                        }
                    } while (retry == 1);

                }
                case 3 -> {
                    currentSessionUser = new Guess();
                    OutputFormatter.clearJavaConsoleScreen();
                    MainMenu(input);
                }
                case 4 -> {

                }
                default -> {
                   //Out of range exception had been handled in TRY CATCH block above.
                   //This was done to reduce nesting as nesting was getting 4 blocks deep :D
                }
            }

        } while (choice < 1 || choice > 4);

    }

    public static void displayLogo() {
        System.out.println(OutputFormatter.printHorizontalBox(15));
        System.out.println(" =====                ");
        System.out.println(" ||                   ");
        System.out.println(" ===\\\\=    //         ");
        System.out.println(" ||  \\\\   //\\\\        ");
        System.out.println(" =====\\\\ //  \\\\       ");
        System.out.println("       \\//====\\\\      ");
        System.out.println("       //      \\\\ .INC");
        System.out.println(OutputFormatter.printHorizontalBox(15));
    }

    public static void displayLoginMenu() {
        System.out.println(OutputFormatter.printHorizontalLine(25));
        System.out.printf("%-25s\n", "[LOGIN MENU]");
        System.out.printf("%-25s\n", "1| Member Login");
        System.out.printf("%-25s\n", "2| Staff Login");
        System.out.printf("%-25s\n", "3| Continue as Guest");
        System.out.printf("%-25s\n", "4| Quit");
        System.out.println(OutputFormatter.printHorizontalLine(25));
        System.out.print("INPUT >>> ");
    }

    //Returns true if login succesful, false otherwise
    public static boolean Login(Scanner input, int choice) {
        System.out.print("\nID :");
        String userID = input.nextLine();
        System.out.print("\nPassword :");
        String userPassword = input.nextLine();

        int userType = (choice == 1) ? 1 : 0; //0 for staff, 1 for member

        try {
            LoginSystem.readCredentialsFile(userType);
        } catch (IOException e) {
            System.out.println("Error occured when reading user credentials file");
        }

        if (LoginSystem.authentication(userID, userPassword)) {
            displayLoginSucess();

            try {
                currentSessionUser = LoginSystem.loadUserData(userType);
            } catch (IOException e) {
                System.out.println("Error occured when reading User's Information file");
            }

            return true;
        } else {
            return false;
        }
    }

    public static void displayLoginSucess() {
        System.out.println("\nLogin success\n\n");
        OutputFormatter.PressToCont();
        OutputFormatter.clearJavaConsoleScreen();
    }

    public static int IncorrectLogin(Scanner input) {
        System.out.println(OutputFormatter.printHorizontalLine(28));
        System.out.println("Incorrect ID or Passwords");
        System.out.println(OutputFormatter.printHorizontalLine(28));

        int retry = 0;
        do {
            System.out.println("1| Retry");
            System.out.println("2| Quit");
            System.out.println(OutputFormatter.printHorizontalLine(10));
            System.out.print("INPUT >>> ");

            try {
                retry = input.nextInt();
                ConsumeCR(input);

                if (retry < 1 || retry > 2) {
                    throw new IllegalArgumentException(OutputFormatter.OUT_OF_RANGE_ERROR_MSG);
                }

            } catch (InputMismatchException e) {
                System.out.println(OutputFormatter.INVALID_INPUT_MSG);
                OutputFormatter.PressToCont();
                input.nextLine();
                OutputFormatter.clearScreen();

            } catch (IllegalArgumentException e) {
                System.out.println(OutputFormatter.OUT_OF_RANGE_ERROR_MSG);
                OutputFormatter.PressToCont();
                OutputFormatter.clearScreen();
            }
        } while (retry < 1 || retry > 2);

        return retry;
    }

    public static void MainMenu(Scanner input) {
        int choice = 0;
        int maxChoice = (currentSessionUser instanceof Staff) ? 7 : 5; // Max choice depends on staff or non-staff

        do {
            System.out.println(OutputFormatter.printHorizontalLine(25));
            System.out.printf("%-25s\n", "[MAIN MENU]");
            System.out.printf("%-25s\n", "1| Purchase Book");
            System.out.printf("%-25s\n", "2| View Cart");
            System.out.printf("%-25s\n", "3| Modify Cart");
            System.out.printf("%-25s\n", "4| Check out");
            System.out.printf("%-25s\n", "5| Exit");

            if (currentSessionUser instanceof Staff) {
                System.out.printf("%-25s\n", "6| Edit Product Information");
                System.out.printf("%-25s\n", "7| Sales Report");
            }

            System.out.println(OutputFormatter.printHorizontalLine(25));
            System.out.print("INPUT >>> ");

            try {
                choice = input.nextInt();
                ConsumeCR(input);
            } catch (InputMismatchException e) {
                System.out.println(OutputFormatter.INVALID_INPUT_MSG);
                OutputFormatter.PressToCont();
                ConsumeCR(input);
                OutputFormatter.clearJavaConsoleScreen();
            } catch (IllegalArgumentException e) {
                System.out.println(OutputFormatter.OUT_OF_RANGE_ERROR_MSG);
                OutputFormatter.PressToCont();
                OutputFormatter.clearJavaConsoleScreen();
            }
            
            switch (choice) {
                case 1 -> {
                    
                }
                case 2 -> {
                    System.out.println("YET TO BE IMPLEMENTED");
                }
                case 3 -> {
                    System.out.println("YET TO BE IMPLEMENTED");
                }
                case 4 -> {
                    System.out.println("YET TO BE IMPLEMENTED");
                }
                case 5 -> {
                    System.out.println("YET TO BE IMPLEMENTED");
                }
                case 6 -> {
                    System.out.println("YET TO BE IMPLEMENTED");
                }
                case 7 -> {
                    System.out.println("YET TO BE IMPLEMENTED");
                }
                default -> {
                    //Out of range exception had been handled in TRY CATCH block above TO reduce nesting
                }
            }

        } while (choice < 1 || choice > maxChoice);
    }

    public static void bookMenu() {
        Inventory inventory = new Inventory();
        inventory.readProductsFromFile();
        inventory.readStockFromFile();

        Set<String> categorySet = inventory.getCategory();
        List<String> categoryList = new ArrayList<>(categorySet);

        int i = 1;
        for (String category : categoryList) {
            System.out.print(i++ + " ");
            System.out.println(category);
        }

        System.out.print("Select a category >>");
        int categoryIndex = new Scanner(System.in).nextInt();
        List<Product> productsInCategory = inventory.getProductList(categoryList.get(categoryIndex - 1));

        System.out.printf("%-10s %-30s %-5s %-10s\n", "Book ID", "Title", "Age", "Price (RM)");
        for (Product p : productsInCategory) {
            System.out.println(p.toString());
        }
    }

    public static void ConsumeCR(Scanner input) {
        input.nextLine();
    }
}
