package BankSystem;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class AccountHandler {
    private final Scanner scanner;
    private final List<BankAccount> accounts;

    public AccountHandler(Scanner scanner, List<BankAccount> accounts) {
        this.scanner = scanner;
        this.accounts = accounts;
    }

    public void createNewAccount() {
        System.out.print("Enter account name: ");
        scanner.nextLine();
        String name = scanner.nextLine();

        if (findAccountByName(name) != null) {
            System.out.println("Account name already exists. Please choose a different name.");
            System.out.println(" ");
            return;
        }

        int pin = getValidatedInt("Enter a 4-digit pin: ", "Invalid pin. Please enter a 4-digit pin.", 1000, 9999);
        int accountType = getValidatedInt("Select account type (1. Savings, 2. Checking): ", "Invalid account type. Please enter 1 for Savings or 2 for Checking.", 1, 2);

        BankAccountTypes accountTyped = accountType == 1 ? BankAccountTypes.SAVINGS_ACCOUNT : BankAccountTypes.CURRENT_ACCOUNT;
        BankAccount newAccount = accountType == 1 ? new SavingsAccount() : new CurrentAccount();
        newAccount.setAccountName(name).setPin(pin).setAccountType(accountTyped);
        accounts.add(newAccount);

        initialDeposit(newAccount.accountType);

        System.out.println("Registration successful!");
        System.out.println("*********************************************************");
    }


    private void initialDeposit(BankAccountTypes accountType) {
        boolean isValid = false;

        while (!isValid) {
            System.out.print("Enter your initial deposit: ");
            if (scanner.hasNextDouble()) {
                double deposit = scanner.nextDouble();
                if (accountType == BankAccountTypes.SAVINGS_ACCOUNT && deposit >= 1000.0) {
                    isValid = true;
                } else if (accountType == BankAccountTypes.CURRENT_ACCOUNT && deposit >= 5000.0) {
                    isValid = true;
                } else {
                    System.out.println("Invalid deposit. Minimum balance is:");
                    System.out.println(" - Savings: 1000");
                    System.out.println(" - Checking: 5000");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid amount.");
                scanner.next();
            }
        }
    }


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