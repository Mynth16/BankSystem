package BankSystem;

public class CurrentAccount extends BankAccount implements IBankAccountActions {
    final double minimumBalanceCurrent = 5000;

    public CurrentAccount() {
        accountType = BankAccountTypes.CURRENT_ACCOUNT;
    }

    @Override
    public void withdraw(double amount) {
        if (this.balance - amount < minimumBalanceCurrent) {
            System.out.println("Withdrawal failed. Minimum balance of " + minimumBalanceCurrent + " must be maintained.");
        } else if (checkIfHasValidBalance(amount)) {
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
        if (amount >= minimumBalanceCurrent) {
            this.balance += amount;
            return true;
        }
        return false;
    }
}