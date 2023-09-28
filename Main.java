package POS;

/**
 *
 * @author qihong
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.InputMismatchException;

public class Main {

    private static Object currentSessionUser;

    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        Cart cart = new Cart(inventory);
        Payment payment = new Payment(cart);
        Scanner input = new Scanner(System.in);
        int choice = 0;

        do {
            OutputFormatter.clearJavaConsoleScreen();
            displayLogo();
            displayLoginMenu();

            try {
                choice = input.nextInt();
                ConsumeCR(input);

                switch (choice) {
                    case 1, 2 -> {
                        int retry;

                        do {
                            boolean LoginSuccess = Login(input, choice);
                            if (LoginSuccess) {
                                retry = 0; //reset 'retry' value if user succesfully log in AFTER failing one or more times
                                MainMenu(input, inventory, cart, payment);
                            } else {
                                retry = IncorrectLogin(input);
                                //To loop LoginMenu() if user chooses to NOT retry
                                if (retry == 2) {
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
                        MainMenu(input, inventory, cart, payment);
                    }
                    case 4 -> {

                    }
                    default -> {
                        throw new IllegalArgumentException(OutputFormatter.OUT_OF_RANGE_ERROR_MSG);
                    }
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

        } while (choice != 4);

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
        int retry = 0;
        do {
            OutputFormatter.clearJavaConsoleScreen();
            System.out.println(OutputFormatter.printHorizontalLine(28));
            System.out.println("Incorrect ID or Passwords");
            System.out.println(OutputFormatter.printHorizontalLine(28));
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
                OutputFormatter.clearJavaConsoleScreen();

            } catch (IllegalArgumentException e) {
                System.out.println(OutputFormatter.OUT_OF_RANGE_ERROR_MSG);
                OutputFormatter.PressToCont();
                OutputFormatter.clearJavaConsoleScreen();
            }
        } while (retry < 1 || retry > 2);

        return retry;
    }

    public static void MainMenu(Scanner input, Inventory inventory, Cart cart, Payment payment) {
        int choice = 0;
        boolean contPurchase = true;

        do {
            OutputFormatter.clearJavaConsoleScreen();
            System.out.println(OutputFormatter.printHorizontalLine(25));
            System.out.printf("%-25s\n", "[MAIN MENU]");
            System.out.printf("%-25s\n", "1| Purchase Book");
            System.out.printf("%-25s\n", "2| View Cart");
            System.out.printf("%-25s\n", "3| Modify Cart");
            System.out.printf("%-25s\n", "4| Check out");
            System.out.printf("%-25s\n", "5| Exit");

            if (currentSessionUser instanceof Staff) {
                System.out.printf("%-25s\n", "6| Product Manager");
                System.out.printf("%-25s\n", "7| Sales Report");
            }

            System.out.println(OutputFormatter.printHorizontalLine(25));
            System.out.print("INPUT >>> ");

            try {
                choice = input.nextInt();
                ConsumeCR(input);

                switch (choice) {
                    case 1 -> {
                        categoryMenu(input, inventory, cart);
                    }
                    case 2 -> {
                        displayCart(cart);
                        OutputFormatter.PressToCont();
                        OutputFormatter.clearJavaConsoleScreen();
                    }
                    case 3 -> {
                        modifyCartMenu(input, cart);
                    }
                    case 4 -> {
                        int userType = 0;
                        if (currentSessionUser instanceof Member) {
                            userType = 1;
                        }
                        if (currentSessionUser instanceof Guess) {
                            userType = 2;
                        }
                        try {
                            contPurchase = checkOut(input, cart, payment, userType);
                        } catch (IllegalStateException e) {
                            System.out.println("No items has been added to cart yet");
                        }
                        OutputFormatter.PressToCont();
                    }
                    case 5 -> {
                        cart.clearCart();
                    }
                    case 6 -> {
                        if (currentSessionUser instanceof Staff) {
                            ProductManagerMenu(input, inventory);
                        } else {
                            throw new IllegalArgumentException(OutputFormatter.OUT_OF_RANGE_ERROR_MSG);
                        }
                    }
                    case 7 -> {
                        if (currentSessionUser instanceof Staff) {
                            SalesReportMenu(input); // SalesReport
                            OutputFormatter.PressToCont();
                        } else {
                            throw new IllegalArgumentException(OutputFormatter.OUT_OF_RANGE_ERROR_MSG);
                        }
                    }
                    default -> {
                        throw new IllegalArgumentException(OutputFormatter.OUT_OF_RANGE_ERROR_MSG);
                    }
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

        } while (choice != 5 && contPurchase);

    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //1. Add [Payment payment = new Payment(cart);] in MainMenu
    //2. And change case 4 in MainMenu

    public static void cashPaymentProcess(Scanner input, Payment payment, int methodPayment) {
        double cashAmount = 0;
        do {
            System.out.print("[INPUT] Amount Paid >>> ");

            try {
                cashAmount = input.nextDouble();
                ConsumeCR(input);

                if (cashAmount >= payment.getDiscountedTotal()) {
                    payment.processCashPayment(cashAmount, methodPayment);
                } else {
                    throw new IllegalArgumentException("Insufficient payment. Please provide enough funds.");
                }

            } catch (InputMismatchException e) {
                System.out.println(OutputFormatter.INVALID_INPUT_MSG);
                OutputFormatter.PressToCont();
                ConsumeCR(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Insufficient payment. Please provide enough funds.");
                OutputFormatter.PressToCont();
            }

        } while (cashAmount < payment.getDiscountedTotal());

    }

    public static void paymentProcess(Scanner input, Payment payment, int userType) {
        int choice = 0;
        do {
            System.out.println("Payment Option: ");
            System.out.println("1| Cash Payment");
            System.out.println("2| Online Payment");
            System.out.println(OutputFormatter.printHorizontalLine(25));
            System.out.print("INPUT >>> ");
            try {
                choice = input.nextInt();
                ConsumeCR(input);

                switch (choice) {
                    case 1 -> {
                        cashPaymentProcess(input, payment, choice);
                    }
                    case 2 -> {
                        payment.processOnlineTransferPayment(choice);
                    }
                    default -> {
                        throw new IllegalArgumentException(OutputFormatter.OUT_OF_RANGE_ERROR_MSG);
                    }
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

        } while (choice < 1 || choice > 2);
    }

    public static boolean checkOut(Scanner input, Cart cart, Payment payment, int userType) {
        int choice = 0;
        boolean contPurchase = true;
        do {
            OutputFormatter.clearJavaConsoleScreen();
            System.out.println(OutputFormatter.printHorizontalLine(110));
            System.out.println("                               CHECKOUT SUMMARY                               ");
            System.out.println(OutputFormatter.printHorizontalLine(110));
            cart.displayCartContents();
            payment.calculateTotal(userType);
            payment.displayAmount();
            System.out.println("1| Proceed Payment");
            System.out.println("2| Exit Payment");
            System.out.println(OutputFormatter.printHorizontalLine(25));
            System.out.print("INPUT >>> ");

            try {
                choice = input.nextInt();
                ConsumeCR(input);

                switch (choice) {
                    case 1 -> {
                        paymentProcess(input, payment, userType);
                        
                        ReceiptController receiptController = new ReceiptController(payment);
                        receiptController.generateReceipt();
                        receiptController.writeToFile();
                        
                        cart.clearCart();
                        OutputFormatter.PressToCont();
                        contPurchase = promptContinuePurchase(input); //Return true if User wants to cont. purchase
                    }
                    case 2 -> {
                        payment.cancelPayment();
                    }
                    default -> {
                        throw new IllegalArgumentException(OutputFormatter.OUT_OF_RANGE_ERROR_MSG);
                    }
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

        } while (choice < 1 || choice > 2);

        return contPurchase;
    }

    public static boolean promptContinuePurchase(Scanner input) {
        int choice = 0;
        boolean contPurchase = false;
        do {
            OutputFormatter.clearJavaConsoleScreen();
            System.out.println(OutputFormatter.printHorizontalLine(25));
            System.out.println("Do you want to continue purchase?");
            System.out.println(OutputFormatter.printHorizontalLine(25));
            System.out.println("1| Yes");
            System.out.println("2| No");
            System.out.println(OutputFormatter.printHorizontalLine(25));
            System.out.print("INPUT >>> ");

            try {
                choice = input.nextInt();
                ConsumeCR(input);

                switch (choice) {
                    case 1 -> {
                        contPurchase = true;
                    }
                    case 2 -> {
                        contPurchase = false;
                    }
                    default -> {
                        throw new IllegalArgumentException(OutputFormatter.OUT_OF_RANGE_ERROR_MSG);
                    }
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

        } while (choice < 1 || choice > 2);

        return contPurchase;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void categoryMenu(Scanner input, Inventory inventory, Cart cart) {
        int choice = 0;

        do {
            OutputFormatter.clearJavaConsoleScreen();
            System.out.println(OutputFormatter.printHorizontalBox(8));
            System.out.println("BOOK CATEGORY");
            System.out.println(OutputFormatter.printHorizontalBox(8));
            System.out.println(OutputFormatter.printHorizontalLine(25));
            System.out.println("1| Horror");
            System.out.println("2| History");
            System.out.println("3| Mathematics");
            System.out.println("4| Programming");
            System.out.println("5| Return");
            System.out.println(OutputFormatter.printHorizontalLine(25));
            System.out.print("Select a category >>> ");

            try {
                choice = input.nextInt();
                ConsumeCR(input);

                switch (choice) {
                    case 1 -> {
                        bookMenu(inventory, cart, "Horror", input);
                    }
                    case 2 -> {
                        bookMenu(inventory, cart, "History", input);
                    }
                    case 3 -> {
                        bookMenu(inventory, cart, "Mathematics", input);
                    }
                    case 4 -> {
                        bookMenu(inventory, cart, "Programming", input);
                    }
                    case 5 -> {

                    }
                    default -> {
                        throw new IllegalArgumentException(OutputFormatter.OUT_OF_RANGE_ERROR_MSG);
                    }
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

        } while (choice != 5);
    }

    public static void bookMenu(Inventory inventory, Cart cart, String category, Scanner input) {

        ArrayList<Product> productsInCategory = inventory.getProductList(category);
        int choice = 0;

        do {
            OutputFormatter.clearJavaConsoleScreen();
            System.out.println(OutputFormatter.printHorizontalLine(84));

            System.out.printf("%-10s %-40s %-12s %-10s\n", "Book ID", "Title", "Age Rating", "Price (RM)");
            for (Product p : productsInCategory) {
                System.out.println(p.toString());
            }
            System.out.println(OutputFormatter.printHorizontalLine(84));
            System.out.println("1| Choose Book");
            System.out.println("2| Return");
            System.out.println(OutputFormatter.printHorizontalLine(25));
            System.out.print("INPUT >>> ");

            try {
                choice = input.nextInt();
                ConsumeCR(input);

                if (choice == 2) {
                    break;
                } else if (choice == 1) {
                    chooseBook(productsInCategory, input, cart);
                } else {
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

        } while (choice != 2);
    }

    public static void chooseBook(ArrayList<Product> productsInCategory, Scanner input, Cart cart) {

        try {
            System.out.print("Product ID >>> ");
            int id = input.nextInt();
            ConsumeCR(input);

            System.out.print("Quantity >>> ");
            int qty = input.nextInt();
            ConsumeCR(input);

            try {
                cart.addItemToCart(id, qty, productsInCategory);
                OutputFormatter.PressToCont();

            } catch (IllegalArgumentException e) {
                System.out.println("! Invalid Book ID entered !");
                OutputFormatter.PressToCont();
                OutputFormatter.clearJavaConsoleScreen();
            }

        } catch (InputMismatchException e) {
            System.out.println(OutputFormatter.INVALID_INPUT_MSG);
            OutputFormatter.PressToCont();
            ConsumeCR(input);
            OutputFormatter.clearJavaConsoleScreen();
        }

    }

    public static void displayCart(Cart cart) {
        System.out.println("                                 Cart List                                    ");
        System.out.println(OutputFormatter.printHorizontalLine(110));
        try {
            cart.displayCartContents();
        } catch (IllegalStateException e) {
            System.out.println("No items has been added to cart yet");
        }
    }

    public static void modifyCartMenu(Scanner input, Cart cart) {
        int choice = 0;
        do {
            OutputFormatter.clearJavaConsoleScreen();
            displayCart(cart);
            System.out.println("MODIFY CART");
            System.out.println(OutputFormatter.printHorizontalLine(25));
            System.out.println("1| Clear cart");
            System.out.println("2| Modify Quantity");
            System.out.println("3| Remove item");
            System.out.println("4| Return");
            System.out.println(OutputFormatter.printHorizontalLine(25));
            System.out.print("INPUT >>> ");

            try {
                choice = input.nextInt();
                ConsumeCR(input);

                switch (choice) {
                    case 1 -> {
                        cart.clearCart();
                        OutputFormatter.PressToCont();
                    }
                    case 2 -> {
                        modifyQuantity(cart, input);
                    }
                    case 3 -> {
                        removeItem(cart, input);
                    }
                    case 4 -> {

                    }
                    default -> {
                        throw new IllegalArgumentException(OutputFormatter.OUT_OF_RANGE_ERROR_MSG);
                    }
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

        } while (choice != 4);
    }

    public static void modifyQuantity(Cart cart, Scanner input) {
        int maxChoice = cart.getItems().size();
        try {
            OutputFormatter.clearJavaConsoleScreen();
            displayCart(cart);
            System.out.print("Choose Number (1/2/3...) >>> ");
            int index = input.nextInt();
            ConsumeCR(input);

            if (index < 1 || index > maxChoice) {
                throw new IndexOutOfBoundsException(OutputFormatter.OUT_OF_RANGE_ERROR_MSG);
            }

            System.out.print("Quantity to add (-1 to decrease) >>> ");
            int quantity = input.nextInt();
            ConsumeCR(input);

            cart.modifyQty(index, quantity);

        } catch (InputMismatchException e) {
            System.out.println(OutputFormatter.INVALID_INPUT_MSG);
            OutputFormatter.PressToCont();
            ConsumeCR(input);
            OutputFormatter.clearJavaConsoleScreen();

        } catch (IndexOutOfBoundsException e) {
            System.out.println(OutputFormatter.OUT_OF_RANGE_ERROR_MSG);
            OutputFormatter.PressToCont();
            OutputFormatter.clearJavaConsoleScreen();

        } catch (QuantityOutOfRangeException e) {
            System.out.println("Quantity entered more than current quantity in cart");
            OutputFormatter.PressToCont();
            OutputFormatter.clearJavaConsoleScreen();

        } catch (QuantityMoreThanStockException e) {
            System.out.println("Quantity entered more than available stock");
            OutputFormatter.PressToCont();
            OutputFormatter.clearJavaConsoleScreen();

        }
    }

    public static void removeItem(Cart cart, Scanner input) {
        int maxChoice = cart.getItems().size();

        try {
            OutputFormatter.clearJavaConsoleScreen();
            displayCart(cart);
            System.out.print("Choose Number (1/2/3...) >>> ");
            int index = input.nextInt();
            ConsumeCR(input);

            if (index < 1 || index > maxChoice) {
                throw new IndexOutOfBoundsException(OutputFormatter.OUT_OF_RANGE_ERROR_MSG);
            }

            cart.removeItem(index);

        } catch (InputMismatchException e) {
            System.out.println(OutputFormatter.INVALID_INPUT_MSG);
            OutputFormatter.PressToCont();
            ConsumeCR(input);
            OutputFormatter.clearJavaConsoleScreen();

        } catch (IndexOutOfBoundsException e) {
            System.out.println(OutputFormatter.OUT_OF_RANGE_ERROR_MSG);
            OutputFormatter.PressToCont();
            OutputFormatter.clearJavaConsoleScreen();
        }
    }

    public static void ProductManagerMenu(Scanner input, Inventory inventory) {
        int choice = 0;
        do {
            OutputFormatter.clearJavaConsoleScreen();
            System.out.println("PRODUCT MANAGER");
            System.out.println(OutputFormatter.printHorizontalLine(25));
            System.out.println("1| Add product");
            System.out.println("2| Edit product");
            System.out.println("3| Return");
            System.out.println(OutputFormatter.printHorizontalLine(25));
            System.out.print("INPUT >>> ");

            try {
                choice = input.nextInt();
                ConsumeCR(input);

                switch (choice) {
                    case 1 -> {
                        addProduct(input, inventory);
                    }
                    case 2 -> {
                        editProduct(input, inventory);
                        OutputFormatter.PressToCont();
                    }
                    case 3 -> {

                    }
                    default -> {
                        throw new IllegalArgumentException(OutputFormatter.OUT_OF_RANGE_ERROR_MSG);
                    }
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
        } while (choice != 3);
    }

    public static void displayCategoryList() {
        String[] category = Product.getCategoryList();
        for (int i = 0; i < category.length; i++) {
            System.out.println(i + 1 + "| " + category[i]);
        }
    }

    public static void displayAgeRatingList() {
        String[] ageRating = Product.getAgeRatingList();
        for (int i = 0; i < ageRating.length; i++) {
            System.out.println(i + 1 + "| " + ageRating[i]);
        }
    }

    public static void addProduct(Scanner input, Inventory inventory) {
        String name = "";
        double price = 0;
        String ageRating = "";
        String category = "";
        int quantity = 0;

        OutputFormatter.clearJavaConsoleScreen();
        System.out.println("ADD PRODUCT");
        System.out.println(OutputFormatter.printHorizontalLine(25));

        //PRODUCT NAME
        do {
            try {

                System.out.print("Product Name >>> ");
                name = input.nextLine();

                if (name.isBlank()) {
                    throw new IllegalProductNameException("      ! Product name cannot be empty !");
                }

            } catch (IllegalProductNameException e) {
                System.out.println("Product name cannot be empty");
                OutputFormatter.PressToCont();
            }
        } while (name.isBlank());

        //PRICE
        do {
            try {

                System.out.print("Price (RM) [-IVES ARE IGNORED] >>> ");
                price = Math.abs(input.nextDouble());
                ConsumeCR(input);

            } catch (InputMismatchException e) {
                System.out.println(OutputFormatter.INVALID_INPUT_MSG);
                OutputFormatter.PressToCont();
                ConsumeCR(input);
            }
        } while (price == 0);

        //AGE RATING
        do {
            try {
                System.out.println(OutputFormatter.printHorizontalLine(10));
                System.out.println("Age Rating");
                System.out.println(OutputFormatter.printHorizontalLine(10));
                displayAgeRatingList();
                System.out.println(OutputFormatter.printHorizontalLine(10));
                System.out.print("Choose >>> ");

                int index = input.nextInt();
                ConsumeCR(input);

                ageRating = Product.getAgeRatingList()[index - 1];

            } catch (InputMismatchException e) {
                System.out.println(OutputFormatter.INVALID_INPUT_MSG);
                OutputFormatter.PressToCont();
                ConsumeCR(input);
            } catch (IndexOutOfBoundsException e) {
                System.out.println(OutputFormatter.OUT_OF_RANGE_ERROR_MSG);
                OutputFormatter.PressToCont();
            }
        } while (ageRating.isEmpty());

        //CATEGORY
        do {
            try {
                System.out.println("\n");
                System.out.println(OutputFormatter.printHorizontalLine(10));
                System.out.println("Category");
                System.out.println(OutputFormatter.printHorizontalLine(10));
                displayCategoryList();
                System.out.println(OutputFormatter.printHorizontalLine(10));
                System.out.print("Choose >>> ");

                int index = input.nextInt();
                ConsumeCR(input);

                category = Product.getCategoryList()[index - 1];

            } catch (InputMismatchException e) {
                System.out.println(OutputFormatter.INVALID_INPUT_MSG);
                OutputFormatter.PressToCont();
                ConsumeCR(input);
            } catch (IndexOutOfBoundsException e) {
                System.out.println(OutputFormatter.OUT_OF_RANGE_ERROR_MSG);
                OutputFormatter.PressToCont();
            }
        } while (category.isEmpty());

        //Initial Stock amount
        do {
            try {
                System.out.print("Stock amount >>> ");
                quantity = input.nextInt();
                ConsumeCR(input);

            } catch (InputMismatchException e) {
                System.out.println(OutputFormatter.INVALID_INPUT_MSG);
                OutputFormatter.PressToCont();
                ConsumeCR(input);
            }
        } while (quantity < 0);

        int choice = 0;
        do {
            System.out.println(OutputFormatter.printHorizontalBox(25));
            System.out.println("\n\nCONFIRM TO ADD PRODUCT?");
            System.out.println(OutputFormatter.printHorizontalBox(25));
            System.out.println("1| Yes");
            System.out.println("2| No");
            System.out.println(OutputFormatter.printHorizontalLine(25));
            System.out.print("INPUT >>> ");

            try {
                choice = input.nextInt();
                ConsumeCR(input);

                switch (choice) {
                    case 1 -> {
                        inventory.addNewProduct(name, ageRating, category, price, quantity);
                        System.out.println("Product has been successfully added");
                    }
                    case 2 -> {

                    }
                    default -> {
                        throw new IllegalArgumentException(OutputFormatter.OUT_OF_RANGE_ERROR_MSG);
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println(OutputFormatter.INVALID_INPUT_MSG);
                OutputFormatter.PressToCont();
                ConsumeCR(input);
            } catch (IllegalArgumentException e) {
                System.out.println(OutputFormatter.OUT_OF_RANGE_ERROR_MSG);
                OutputFormatter.PressToCont();
                OutputFormatter.clearJavaConsoleScreen();
            }

            OutputFormatter.PressToCont();

        } while (choice < 1 || choice > 2);

    }

    public static void displayProductList(Inventory inventory) {
        ArrayList<Product> products = inventory.getAllProduct();

        System.out.printf("%-10s %-40s %-12s %-10s\n", "Book ID", "Title", "Age Rating", "Price (RM)");
        for (Product product : products) {
            System.out.println(product.toString());
        }
    }

    public static void editProduct(Scanner input, Inventory inventory) {
        do {
            OutputFormatter.clearJavaConsoleScreen();
            System.out.println(OutputFormatter.printHorizontalBox(25));
            System.out.println("EDIT PRODUCT");
            System.out.println(OutputFormatter.printHorizontalBox(25));
            System.out.println(OutputFormatter.printHorizontalLine(25));
            System.out.println("PRODUCT LIST");
            System.out.println(OutputFormatter.printHorizontalLine(25));
            displayProductList(inventory);
            System.out.println(OutputFormatter.printHorizontalLine(25));
            System.out.println("Product ID >>> ");

            try {
                int id = input.nextInt();
                ConsumeCR(input);

            } catch (InputMismatchException e) {
                System.out.println(OutputFormatter.INVALID_INPUT_MSG);
                OutputFormatter.PressToCont();
                ConsumeCR(input);
            }

        } while (true);
    }

    // SLAES REPORT
    public static void SalesReportMenu(Scanner input) {
        int choice = 0;
        SalesReport salesReport = new SalesReport();
        do {

            salesReport.calculateProductQuantity();
            salesReport.calculateProductSales();

            OutputFormatter.clearJavaConsoleScreen();
            System.out.printf("%-25s\n", "[SALES REPORT]");
            System.out.printf("%-25s\n", "1| Product Sales Report");
            System.out.printf("%-25s\n", "2| Total Sales Report");
            System.out.printf("%-25s\n", "3| Return");
            System.out.println(OutputFormatter.printHorizontalLine(25));
            System.out.print("INPUT >>> ");

            try {
                choice = input.nextInt();
                ConsumeCR(input);

                switch (choice) {
                    case 1 -> {
                        salesReport.generateProductSalesReport();
                        OutputFormatter.PressToCont();
                        OutputFormatter.clearJavaConsoleScreen();
                    }

                    case 2 -> {
                        salesReport.generateTotalSalesReport();
                        OutputFormatter.PressToCont();
                        OutputFormatter.clearJavaConsoleScreen();
                    }

                    case 3 -> {

                    }
                    default -> {
                        throw new IllegalArgumentException(OutputFormatter.OUT_OF_RANGE_ERROR_MSG);
                    }
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
        } while (choice != 3);

    }

    public static void ConsumeCR(Scanner input) {
        input.nextLine();
    }
}
