package BankSystem;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static ArrayList<BankAccount> accounts = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static BankAccount loggedInAccount = null;

    public static void main(String[] args) {
        boolean closed = false;

        while (!closed) {
            if (loggedInAccount == null) {
                startPage();
                int action = promptAction();

                switch (action) {
                    case 1:
                        createNewAccount();
                        break;
                    case 2:
                        if (login()) {
                            loggedinMenu();
                        }
                        break;
                    case 3:
                        System.out.println("Exiting...");
                        closed = true;
                        break;
                    default:
                        System.out.println("Invalid action");
                }
            }else {
                
                loggedinMenu();
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

    private static int promptAction() {
        boolean isValid = false;
        int action = 0;

        while (!isValid) {
            if (scanner.hasNextInt()) {
                action = scanner.nextInt();
                if (action >= 1 && action <= 3) {
                    isValid = true;
                } else {
                    System.out.println("Invalid input");
                    scanner.next();
                }
            } else {
                System.out.println("Invalid input");
                scanner.next();
            }
        }
        return action;
    }


    private static void createNewAccount() {
        BankAccountTypes accountTyped;

        System.out.print("Enter account name: ");
        scanner.nextLine();
        String name = scanner.nextLine();

        System.out.print("Enter your 4-digit PIN: ");
        int pin = scanner.nextInt();

        while (pin < 1000 || pin > 9999) {
            System.out.print("Invalid PIN. Enter a 4-digit PIN: ");
            pin = scanner.nextInt();
        }

        System.out.print("Select account type (1 for Savings, 2 for Checking): ");
        int accountType = scanner.nextInt();

        while (accountType != 1 && accountType != 2) {
            System.out.print("Invalid type. Select account type (1 for Savings, 2 for Checking): ");
            accountType = scanner.nextInt();
            accountTyped = accountType == 1 ? BankAccountTypes.SAVINGS_ACCOUNT : BankAccountTypes.CURRENT_ACCOUNT;
        }

        accountTyped = accountType == 1 ? BankAccountTypes.SAVINGS_ACCOUNT : BankAccountTypes.CURRENT_ACCOUNT;

        BankAccount newAccount = accountType == 1 ? new SavingsAccount() : new CurrentAccount();
        newAccount.setAccountName(name)
                .setPin(pin)
                .setAccountType(accountTyped);
        accounts.add(newAccount);

        double depositAmount = initialDeposit(newAccount.accountType);

        boolean depositSuccess;
        if (newAccount instanceof SavingsAccount) {
            depositSuccess = ((SavingsAccount) newAccount).initialDeposit(depositAmount);
        } else if (newAccount instanceof CurrentAccount) {
            depositSuccess = ((CurrentAccount) newAccount).initialDeposit(depositAmount);
        } else {
            throw new IllegalArgumentException("Unsupported account type.");
        }

        if (!depositSuccess) {
            System.out.println("Initial deposit failed. Please ensure you meet the minimum deposit requirements.");
            return;
        }


        System.out.println("Registration successful!");
        System.out.println("*********************************************************");
    }


    private static double initialDeposit(BankAccountTypes accountType) {
        boolean isValid = false;
        double deposit = 0;

        while (!isValid) {
            System.out.print("Enter your initial deposit: ");
            if (scanner.hasNextDouble()) {
                deposit = scanner.nextDouble();
                if (accountType == BankAccountTypes.SAVINGS_ACCOUNT && deposit >= 1000) {
                    isValid = true;
                } else if (accountType == BankAccountTypes.CURRENT_ACCOUNT && deposit >= 5000) {
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


    private static boolean login() {
        int pin = 0;

        System.out.print("Enter your account name: ");
        scanner.nextLine();
        String accountName = scanner.nextLine();

        BankAccount account = findAccountByName(accountName);

        if (account != null) {
            System.out.print("Enter your 4-digit pin: ");
            pin = scanner.nextInt();

            if (account.verifyPin(pin)) {
                System.out.println("Login successful!");
                loggedInAccount = account;
                return true;
            } else {
                System.out.println("Invalid pin. Please try again.");
                account.failedLoginAttempts++;
            }
        } else {
            System.out.println("Invalid account. Please try again.");
        }

        if (account != null && account.failedLoginAttempts == 3) {
            System.out.println("Account locked due to multiple failed login attempts.");
        }
        return false;
    }


    private static BankAccount findAccountByName(String accountName) {
        for (BankAccount account : accounts) {
            if (account.getAccountName().equalsIgnoreCase(accountName)) {
                return account;
            }
        }
        return null;
    }


    private static void loggedInMenu() {
        boolean loggedIn = true;
        BankAccount bankAccount;


        if (Main.loggedInAccount instanceof SavingsAccount) {
            bankAccount = Main.loggedInAccount;
        } else if (Main.loggedInAccount instanceof CurrentAccount) {
            bankAccount = Main.loggedInAccount;
        } else {
            System.out.println("Invalid account type.");
            return;
        }



        while (loggedIn) {
            System.out.println(" ");
            System.out.println("*********************************************************");
            System.out.println("Welcome " + bankAccount.getAccountName());
            System.out.println("Account type: " + bankAccount.getAccountType());
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            if (loggedInAccount.getAccountType() == BankAccountTypes.SAVINGS_ACCOUNT) {
                System.out.println("4. Calculate Interest"); }
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");

            int action = scanner.nextInt();

            switch (action) {
                case 1:
                    System.out.println("Your current balance is: " + bankAccount.getBalance());
                    break;
                case 2:
                    System.out.print("Enter deposit amount: ");
                    double depositAmount = scanner.nextDouble();
                    bankAccount.deposit(depositAmount);
                    System.out.println("Deposit successful. New balance: " + bankAccount.getBalance());
                    break;
                case 3:
                    System.out.print("Enter withdraw amount: ");
                    double withdrawAmount = scanner.nextDouble();
                    ((IBankAccountActions) bankAccount).withdraw(withdrawAmount);
                    break;
                case 4:
                    if (bankAccount.getAccountType() == BankAccountTypes.SAVINGS_ACCOUNT) {
                        ((IBankAccountActions) bankAccount).addInterest();
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
}