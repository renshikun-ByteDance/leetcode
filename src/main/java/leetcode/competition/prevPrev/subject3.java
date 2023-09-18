package leetcode.competition.prevPrev;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class subject3 {

    public static void main(String[] args) {
        //数据接入和预处理
        Scanner scanner = new Scanner(System.in);
        ArrayList<String[]> exchangeInfo = new ArrayList<>();
        while (scanner.hasNext()) {
            String nextLine = scanner.nextLine();
            if (nextLine.equals("-1")) {
                break;
            }
            exchangeInfo.add(nextLine.split("\\s+"));
        }
        //逐条处理逻辑判断
        HashMap<String, String> hTable = new HashMap<>();
        for (String[] exchange : exchangeInfo) {
            String operator = exchange[1];
            if (exchange[0].startsWith("S")) {
                if (operator.equals("bind")) {               //1、商户注册的场景
                    if (hTable.containsKey(exchange[0]) || hTable.containsKey(exchange[2])) {  //1.1商户或号码已注册
                        System.out.println(exchange[0] + " " + exchange[1] + " fail");
                    } else {                                                                   //1.2商户或号码均为注册
                        hTable.put(exchange[0], exchange[2]);
                        hTable.put(exchange[2], exchange[0]);
                        System.out.println(exchange[0] + " " + exchange[1] + " success");
                    }
                } else if (operator.equals("unbind")) {   //2、商户解绑的场景
                    if (!hTable.containsKey(exchange[0]) || !hTable.containsKey(exchange[2]) || !hTable.get(exchange[0]).equals(exchange[2])) {  //2.1 商户或号码其一未注册，或者商户和号码二者不匹配
                        System.out.println(exchange[0] + " " + exchange[1] + " fail");
                    } else {
                        hTable.remove(exchange[0]);  //移除商户和号码的映射关系
                        hTable.remove(exchange[2]);  //移除号码和商户的映射关系
                        System.out.println(exchange[0] + " " + exchange[1] + " fail");
                    }
                } else {
                    System.out.println("unknown command");
                }
            } else if (exchange[0].startsWith("U")) {
                if (operator.equals("scan")) {      //2、用户扫码的场景
                    if (hTable.containsKey(exchange[2])) {   //2.1 用户扫描的号码是否被注册
                        System.out.println(exchange[0] + " pay to " + hTable.get(exchange[2]));
                    } else {                                 //2.2号码未被注册
                        System.out.println(exchange[0] + " " + exchange[1] + " fail");
                    }
                } else {
                    System.out.println("unknown command");
                }
            } else {
                System.out.println("unknown command");
            }
        }
    }


}
