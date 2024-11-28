package BankSystem;

public class CurrentAccount extends BankAccount implements IBankAccountActions {
    final double minimumBalanceCurrent = 5000;

    public CurrentAccount() {
        dailyWithdrawalLimit = 20000;
        accountType = BankAccountTypes.CURRENT_ACCOUNT;
    }

    @Override
    public void withdraw(double amount) {
        if (checkIfHasValidBalance(amount)) {
            this.balance -= amount;
            System.out.println("Withdrawal successful. New balance: " + balance);
        } else {
            System.out.println("Insufficient funds.");
        }
    }

    @Override
    public void addInterest() {
        System.out.println("This feature is not available for Current accounts.");
    }

    @Override
    public boolean initialDeposit(double amount) {
        if (amount >= 5000) {
            this.balance += amount;
            return true;
        }
        return false;
    }
}
