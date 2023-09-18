package utils.other;

/**
 * 2043. 简易银行系统
 */
public class Bank {


    private int nums;
    private long[] balance;

    public Bank(long[] balance) {
        this.nums = balance.length;
        this.balance = balance;
    }

    public boolean transfer(int account1, int account2, long money) {
        if (account1 > nums || account2 > nums) {
            return false;
        }
        if (balance[account1 - 1] < money) {
            return false;
        }
        balance[account1 - 1] -= money;
        balance[account2 - 1] += money;
        return true;
    }

    public boolean deposit(int account, long money) {
        if (account > nums) return false;
        balance[account - 1] += money;
        return true;
    }

    public boolean withdraw(int account, long money) {
        if (account > nums) return false;
        if (balance[account - 1] < money) return false;
        balance[account - 1] -= money;
        return true;
    }


}
