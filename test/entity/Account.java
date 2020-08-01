package entity;

/**
 * 说明：银行账户
 * 创建人：Ajie
 * 创建时间：2020年7月10日09:49:09
 */
public class Account {

    /**
     * 余额
     */
    protected double balance;

    /**
     * 有参构造方法
     */
    public Account(double init) {
        this.balance = init;
    }

    /**
     * 获取余额
     */
    public double getBalance() {
        return this.balance;
    }

    /**
     * 功能描述：存钱
     *
     * @param amt 数额
     * @author Ajie
     * @date 2020/7/10 0010
     */
    public void deposit(double amt) {
        this.balance += amt;
    }

    /**
     * 功能描述：取钱
     *
     * @param amt 数额
     * @author Ajie
     * @date 2020/7/10 0010
     */
    public void withdraw(double amt) {
        this.balance -= amt;
    }

}
