/* Name: Rafael Angelo B. Catimbang
   Section: BCS112L-OCa */

package BankSystem;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    private static final ArrayList<BankAccount> accounts = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static final AccountHandler accountHandler = new AccountHandler(scanner, accounts);
    private static BankAccount loggedInAccount = null;

    public static void main(String[] args) {
        boolean closed = false;

        while (!closed) {
            if (loggedInAccount == null) {
                startPage();
                int action = getValidatedInt("Invalid input. Please enter a valid choice.", 3);

                switch (action) {
                    case 1: // Register
                        accountHandler.createNewAccount();
                        break;
                    case 2: // Login
                        BankAccount account = accountHandler.login();
                        if (account != null) {
                            loggedInAccount = account;
                            loggedInMenu();
                        }
                        break;
                    case 3: // Exit
                        System.out.println("Exiting...");
                        closed = true;
                        break;
                    default:
                        System.out.println("Invalid action");
                }
            } else {
                loggedInMenu();
            }
        }
    }

    private static void startPage() {
        System.out.println("*********************************************************");
        System.out.println("Welcome to the Multi-Tier Bank Account Management System");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
    }


    // Displays account name and type, and allows the user to check balance, deposit, withdraw, calculate interest, or logout
    private static void loggedInMenu() {
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println(" ");
            System.out.println("*********************************************************");
            System.out.println("Welcome " + loggedInAccount.getAccountName());
            if (loggedInAccount.getAccountType() == BankAccountTypes.SAVINGS_ACCOUNT) {
                System.out.println("Account type: Savings Account");
            } else {
                System.out.println("Account type: Current Account");
            }
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            if (loggedInAccount.getAccountType() == BankAccountTypes.SAVINGS_ACCOUNT) {
                System.out.println("4. Calculate Interest"); }
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");

            int action = getValidatedInt("Invalid input.", 5);

            switch (action) {
                case 1:
                    System.out.println("Your current balance is: " + loggedInAccount.getBalance());
                    break;
                case 2:
                    System.out.print("Enter deposit amount: ");
                    int depositAmount = getValidatedInt("Invalid input. Please enter a valid amount.", Integer.MAX_VALUE);
                    loggedInAccount.deposit(depositAmount);
                    System.out.println("Deposit successful. New balance: " + loggedInAccount.getBalance());
                    break;
                case 3:
                    System.out.print("Enter withdraw amount: ");
                    int withdrawAmount = getValidatedInt("Invalid input. Please enter a valid amount.", Integer.MAX_VALUE);
                    ((IBankAccountActions) loggedInAccount).withdraw(withdrawAmount);
                    break;
                case 4:
                    if (loggedInAccount.getAccountType() == BankAccountTypes.SAVINGS_ACCOUNT) {
                        ((IBankAccountActions) loggedInAccount).addInterest();
                    } else {
                        System.out.println("This feature is not available for Current accounts.");
                    }
                    break;
                case 5:
                    loggedIn = false;
                    loggedInAccount = null;
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid input. Please try again.");
            }
        }
    }

    private static int getValidatedInt(String errorMessage, int max) {
        while (true) {
            try {
                int input = scanner.nextInt();
                if (input >= 1 && input <= max) {
                    return input;
                } else {
                    System.out.println(errorMessage);
                }
            } catch (InputMismatchException e) {
                System.out.println(errorMessage);
                scanner.next();
            }
        }
    }
}