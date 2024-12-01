package BankSystem;

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

        System.out.print("Enter your 4-digit PIN: ");
        int pin = scanner.nextInt();
        while (pin < 1000 || pin > 9999) {
            System.out.print("Invalid PIN. Please enter a 4-digit PIN: ");
            pin = scanner.nextInt();
        }

        System.out.print("Select account type (1 for Savings, 2 for Checking): ");
        int accountType = scanner.nextInt();
        while (accountType != 1 && accountType != 2) {
            System.out.print("Invalid account type. Please enter 1 for Savings or 2 for Checking: ");
            accountType = scanner.nextInt();
        }

        BankAccountTypes accountTyped = accountType == 1 ? BankAccountTypes.SAVINGS_ACCOUNT : BankAccountTypes.CURRENT_ACCOUNT;
        BankAccount newAccount = accountType == 1 ? new SavingsAccount() : new CurrentAccount();
        newAccount.setAccountName(name).setPin(pin).setAccountType(accountTyped);
        accounts.add(newAccount);

        double depositAmount = initialDeposit(newAccount.accountType);
        boolean depositSuccess = ((IBankAccountActions) newAccount).initialDeposit(depositAmount);

        if (!depositSuccess) {
            System.out.println("Initial deposit failed. Please ensure you meet the minimum deposit requirements.");
        } else {
            System.out.println("Registration successful!");
            System.out.println("*********************************************************");
        }
    }


    private double initialDeposit(BankAccountTypes accountType) {
        boolean isValid = false;
        double deposit = 0.0;

        while (!isValid) {
            System.out.print("Enter your initial deposit: ");
            if (scanner.hasNextDouble()) {
                deposit = scanner.nextDouble();
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
        return deposit;
    }


    public BankAccount login() {

        System.out.print("Enter your account name: ");
        scanner.nextLine();
        String accountName = scanner.nextLine();

        BankAccount account = findAccountByName(accountName);

        if (account != null) {
            System.out.print("Enter your 4-digit pin: ");
            if (scanner.hasNextInt()) {
                int pin = scanner.nextInt();

                if (account.verifyPin(pin)) {
                    System.out.println("Login successful!");
                    return account;
                } else {
                    System.out.println("Invalid pin. Please try again.");
                    account.failedLoginAttempts++;
                }
            } else {
                System.out.println("Please enter a 4-digit pin.");
            }
        } else {
            System.out.println("Account not found. Please try again.");
        }

        if (account != null && account.failedLoginAttempts == 3) {
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
}
