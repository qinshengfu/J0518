import org.junit.Test;

/**
 * 说明：数字相关测试类
 * 创建人：Ajie
 * 创建时间：2020年7月6日10:44:16
 */
public class NumberTest {

    @Test
    public void test() {
        primeTest(100);
    }

    /**
     * 功能描述：寻找0~N内有多少个质数
     * 质数：只能被1和自身整除的数，例如 5只能被 1和5整除，所有5是质数
     *
     * @param num 正整数
     * @author Ajie
     * @date 2020/7/6 0006
     */
    public void primeTest(int num) {
        int primeCount = 0;

        for (int i = 1; i <= num; i++) {
            if (isPrime(i)) {
                primeCount++;
            }
        }
        System.out.println("0~" + num + "共有" + primeCount + "个质数");
    }

    /**
     * 功能描述：判断一个数字是否为质数
     *
     * @param num 数字
     * @return 是：true、否：false
     * @author Ajie
     * @date 2020/7/6 0006
     */
    public boolean isPrime(int num) {
        // 开方
        double openNum = Math.sqrt(num);
        for (int i = 2; i <= openNum; i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

}
