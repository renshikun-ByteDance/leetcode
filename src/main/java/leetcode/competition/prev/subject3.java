package leetcode.competition.prev;

import java.util.Scanner;

public class subject3 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();
        String[] split = str.split("\\s+");
        int n = split.length;
        Double[] rate = new Double[n];   //每日的万份收益率
        for (int i = 0; i < n; i++) {
            rate[i] = Double.parseDouble(split[i]);
        }
        Double[] money = new Double[n];
        int currentIndex = 0;
        while (scanner.hasNext()) {
            money[currentIndex] = Double.parseDouble(scanner.nextLine());
            currentIndex++;
            if (currentIndex == n) break;
        }
        double[] dp = new double[n];
        for (int i = 1; i < n; i++) {
            //1、针对昨天的金额进行确认
            double remain = dp[i - 1] + money[i - 1];  //当日确认的金额
            //2、针对当天的金额进行确认
            if (money[i] < 0) {
                //-----------------------------------------------
                // 针对支出的金额，当天确认，参与计算当天息费
                // 针对存储的金额，当前不确认，不参数计算当天息费，第二天才会确认
                //-----------------------------------------------
                remain += money[i];
                money[i] = 0.0;      //针对支付的金额，当日确认后，将其置为 0.0，第二天忽略此支出的交易
            }
            if (i == n - 1) {   //最后一天，不计算利息
                System.out.println(String.format("%.2f", remain));
            }
            dp[i] = remain + remain / 10000.00 * rate[i];
        }
    }
}
