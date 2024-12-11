package BankSystem;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class AccountHandler {
    private final Scanner scanner;
    private final List<BankAccount> accounts;

    // constructor that takes a scanner and a list of BankAccount objects as input
    public AccountHandler(Scanner scanner, List<BankAccount> accounts) {
        this.scanner = scanner;
        this.accounts = accounts;
    }

    // method to Register
    public void createNewAccount() {
        System.out.print("Enter account name: ");
        scanner.nextLine();
        String name = scanner.nextLine();
        if (name.isEmpty()) {
            System.out.println("Input cannot be empty.");
            return;
        }

        if (findAccountByName(name) != null) {
            System.out.println("Account name already exists. Please choose a different name.");
            System.out.println(" ");
            return;
        }

        int pin = getValidatedInt("Enter a 4-digit pin: ", "Invalid pin. Please enter a 4-digit pin.", 1000, 9999);
        int accountType = getValidatedInt("Select account type (1. Savings, 2. Checking): ", "Invalid account type. Please enter 1 for Savings or 2 for Checking.", 1, 2);

        // accountTyped is just a local variable, read this as
        // IF accountType is 1, THEN accountTyped is SAVINGS_ACCOUNT, ELSE accountTyped is CURRENT_ACCOUNT
        // then it creates a new account based on the account type, adds the specified values to the specific account object and then adds it to the list

        BankAccountTypes accountTyped = accountType == 1 ? BankAccountTypes.SAVINGS_ACCOUNT : BankAccountTypes.CURRENT_ACCOUNT;
        BankAccount newAccount = accountType == 1 ? new SavingsAccount() : new CurrentAccount();
        newAccount.setAccountName(name).setPin(pin).setAccountType(accountTyped);
        accounts.add(newAccount);

        // read the minimum as 1000 if accountType is 1, else 5000
        double amount = getValidatedInt("Enter your initial deposit: ",
                "Invalid deposit. Minimum balance is: " + (accountType == 1 ? 1000 : 5000),
                accountType == 1 ? 1000 : 5000,
                Integer.MAX_VALUE);

        // initialDeposit is a boolean method in the IBankAccountActions interface, so it needs to be cast to that interface to be called
        // it returns true if the deposit was successful, then deposits the specified amount in the new account
        if (((IBankAccountActions) newAccount).initialDeposit(amount)) {
            System.out.println("Deposit successful. New balance: " + newAccount.getBalance());
        } else {
            System.out.println("Deposit failed. Minimum balance of " + (accountType == 1 ? 1000 : 5000) + " must be maintained.");
            accounts.remove(newAccount);
            return;
        }

        System.out.println("Registration successful!");
        System.out.println("*********************************************************");
    }

    // login method that takes an account name and pin as input and returns the account object if it exists in the list
    // failed login attempts are tracked individually for each account
    public BankAccount login() {

        System.out.print("Enter your account name: ");
        scanner.nextLine();
        String accountName = scanner.nextLine();

        BankAccount account = findAccountByName(accountName);

        if (account == null) {
            System.out.println("Account not found. Please try again.");
            return null;
        }

        int pin = getValidatedInt("Enter your 4-digit pin: ", "Invalid pin. Please enter a 4-digit pin.", 1000, 9999);

        if (account.verifyPin(pin)) {
            System.out.println("Login successful!");
            return account;
        } else {
            System.out.println("Invalid pin. Please try again.");
            account.failedLoginAttempts++;
        }


        if (account.failedLoginAttempts == 3) {
            System.out.println("Account locked due to multiple failed login attempts.");
            accounts.remove(account);
        }
        return null;
    }


    // takes a string input and returns the account object if it exists in the list
    private BankAccount findAccountByName(String accountName) {
        for (BankAccount account : accounts) {
            if (account.getAccountName().equalsIgnoreCase(accountName)) {
                return account;
            }
        }
        return null;
    }


    private int getValidatedInt(String prompt, String errorMessage, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                int input = scanner.nextInt();
                if (input >= min && input <= max) {
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