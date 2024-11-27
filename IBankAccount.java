package BankSystem;

public interface IBankAccount {
    //String accountName = "";
    //int pin = 1000;
    //BankAccountTypes accountType = BankAccountTypes.DEFAULT_ACCOUNT;
    //int failedLoginAttempts = 0;

    BankAccount setAccountName(String accountName);
    BankAccount setPin(int pin);
    BankAccount setAccountType(BankAccountTypes accountType);

    String getAccountName();
    BankAccountTypes getAccountType();
    double getBalance();
    boolean verifyPin(int inputPin);

    //double getTotalWithdrawnToday();
    //double getDailyWithdrawalLimit();
}

