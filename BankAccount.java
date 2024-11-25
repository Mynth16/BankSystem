package BankSystem;

public class BankAccount implements IBankAccount
{
    private String accountName;
    private int pin;
    protected BankAccountTypes accountType;
    protected double balance = 0.0;
    protected double dailyWithdrawalLimit = 0.0;
    protected double totalWithdrawnToday;
    protected int failedLoginAttempts = 0;

    public BankAccount setAccountName(String accountName) {
        this.accountName = accountName;
        return this;
    }

    public BankAccount setPin(int pin) {
        this.pin = pin;
        return this;
    }

    public BankAccount setAccountType(BankAccountTypes accountType) {
        this.accountType = accountType;
        return this;
    }

    public String getAccountName() {
        return accountName;
    }

    public BankAccountTypes getAccountType() {
        return accountType;
    }

    public double getBalance() {
        return balance;
    }

    public boolean verifyPin(int inputPin) {
        return this.pin == inputPin;
    }

    public void deposit(double amount) {
        this.balance += amount;
    }

    public double getTotalWithdrawnToday() {
        return totalWithdrawnToday;
    }

    public double getDailyWithdrawalLimit() {
        return dailyWithdrawalLimit;
    }

    public boolean checkIfHasValidBalance(double amount) {
        return this.balance >= amount;
    }
}