package BankSystem;

public interface IBankAccountActions {
    void withdraw(double amount);
    void addInterest();
    boolean initialDeposit(double amount);
}