package BankSystem;

public class SavingsAccount extends BankAccount implements IBankAccountActions {
    final double minimumBalanceSavings = 1000;
    final double interestRate = 0.03;

    public SavingsAccount() {
        dailyWithdrawalLimit = 20000;
        accountType = BankAccountTypes.SAVINGS_ACCOUNT;
    }

    @Override
    public void withdraw(double amount) {
        if (totalWithdrawnToday + amount > dailyWithdrawalLimit) {
            System.out.println("Withdrawal limit exceeded for the day. Max allowed: " + (dailyWithdrawalLimit - totalWithdrawnToday));
        } else if (checkIfHasValidBalance(amount)) {
            this.balance -= amount;
            totalWithdrawnToday += amount;
            System.out.println("Withdrawal successful. New balance: " + balance);
        } else {
            System.out.println("Insufficient funds.");
        }
    }

    @Override
    public void addInterest() {
        double interest = balance * 0.03;
        this.balance += interest;
        System.out.println("Interest: " + interest);
        System.out.println("New balance: " + balance);
    }

    @Override
    public boolean initialDeposit(double amount) {
        if (amount >= 1000) {
            this.balance += amount;
            return true;
        }
        return false;
    }
}
