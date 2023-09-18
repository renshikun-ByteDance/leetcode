package leetcode.competition.prevPrev;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class subject4 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String[]> ans = new ArrayList<>();
        HashMap<String, String[]> operationSystem = new HashMap<>();     //业务系统数据（去重），主键为业务订单号|交易流水号
        HashMap<String, String[]> paymentSystem = new HashMap<>();       //支付系统数据（去重），主键为业务订单号|交易流水号
        HashSet<String> operation = new HashSet<>();  //业务系统中的去重数据
        HashSet<String> payment = new HashSet<>();    //支付系统中的去重数据
        HashMap<String, Integer> operationRepeatData = new HashMap<>();  //业务系统重复数据
        HashMap<String, Integer> paymentRepeatData = new HashMap<>();    //支付系统重复数据

        //------------------------------------------------------------------
        // 判断各个系统内部重复的情况
        //------------------------------------------------------------------

        //1、业务系统交易数据
        while (scanner.hasNext()) {
            String nextLine = scanner.nextLine();
            if (nextLine.equals("-1")) break;
            if (!operation.contains(nextLine)) {
                operation.add(nextLine);
                String[] split = nextLine.split(",");
                operationSystem.put(split[0] + "|" + split[4], new String[]{split[2], split[3], split[1]});   //value为交易金额、交易时间、姓名
            } else {
                operationRepeatData.put(nextLine, operationRepeatData.getOrDefault(nextLine, 0) + 1);
//                operationRepeatData.put(nextLine, operationRepeatData.getOrDefault(nextLine, 1) + 1);  //默认值为 1，即最小值为 2
            }
        }

        //2、支付系统交易数据
        while (scanner.hasNext()) {
            String nextLine = scanner.nextLine();
            if (nextLine.equals("-1")) break;
            if (!payment.contains(nextLine)) {
                payment.add(nextLine);
                String[] split = nextLine.split(",");
                paymentSystem.put(split[1] + "|" + split[0], new String[]{split[2], split[3], split[4]});    //value为交易金额、交易时间、支付时间
            } else {
                paymentRepeatData.put(nextLine, paymentRepeatData.getOrDefault(nextLine, 0) + 1);
//                paymentRepeatData.put(nextLine, paymentRepeatData.getOrDefault(nextLine, 1) + 1);   //默认值为 1，即最小值为 2
            }
        }

        //-----------------------------------------------------------------------
        // 分别处理系统内重复的情况
        //-----------------------------------------------------------------------
        for (String nextLine : operationRepeatData.keySet()) {  //业务系统内重复的情况
            //"业务订单号,姓名,交易金额,交易时间,支付流水号"
            String[] split = nextLine.split(",");
            String[] errorInfo = new String[7];
            //输出格式为：业务订单号,业务交易金额,业务交易时间,支付流水号,支付交易金额,支付交易时间,差错原因：
            errorInfo[0] = split[0];  //业务订单号
            errorInfo[1] = split[2];  //业务交易金额
            errorInfo[2] = split[3];  //业务交易时间
            errorInfo[3] = split[4];  //支付流水号
            errorInfo[4] = "";
            errorInfo[5] = "";
            errorInfo[6] = "F" + operationRepeatData.get(nextLine);
            ans.add(new String[]{split[3], String.join(",", errorInfo)});   //使用 业务交易时间作为排序元素
        }

        for (String nextLine : paymentRepeatData.keySet()) {  //支付系统内重复的情况
            //"支付流水号,业务订单号,交易金额,交易时间,支付时间"
            String[] split = nextLine.split(",");
            String[] errorInfo = new String[7];
            //输出格式为：业务订单号,业务交易金额,业务交易时间,支付流水号,支付交易金额,支付交易时间,差错原因：
            errorInfo[0] = split[1];   //业务订单号
            errorInfo[1] = "";
            errorInfo[2] = "";
            errorInfo[3] = split[0];   //支付流水号
            errorInfo[4] = split[2];   //支付交易金额
            errorInfo[5] = split[3];   //支付交易时间
            errorInfo[6] = "G" + paymentRepeatData.get(nextLine);
            ans.add(new String[]{split[3], String.join(",", errorInfo)});   //使用 业务交易时间作为排序元素
        }

        //------------------------------------------------------------------
        // 判断各个系统之间数据丢失的情况
        //------------------------------------------------------------------
        //1、业务系统有、支付系统中没有的数据
        for (String key : operationSystem.keySet()) {
            if (!paymentSystem.containsKey(key)) {
                String[] split = key.split("\\|");
                String[] opInfo = operationSystem.get(key);
                String[] errorInfo = new String[7];
                //输出格式为：业务订单号,业务交易金额,业务交易时间,支付流水号,支付交易金额,支付交易时间,差错原因：
                errorInfo[0] = split[0];   //业务订单号
                errorInfo[1] = opInfo[0];  //业务交易金额
                errorInfo[2] = opInfo[1];  //业务交易时间
                errorInfo[3] = split[1];   //支付流水号
                errorInfo[4] = "";
                errorInfo[5] = "";
                errorInfo[6] = "+";        //仅存在业务数据
                ans.add(new String[]{opInfo[1], String.join(",", errorInfo)});   //使用 业务交易时间作为排序元素
            }
        }
        //2、支付系统中有、业务系统中没有的数据
        for (String key : paymentSystem.keySet()) {
            if (!operationSystem.containsKey(key)) {
                String[] split = key.split("\\|");
                String[] opInfo = operationSystem.get(key);
                String[] errorInfo = new String[7];
                //输出格式为：业务订单号,业务交易金额,业务交易时间,支付流水号,支付交易金额,支付交易时间,差错原因：
                errorInfo[0] = split[0];   //业务订单号
                errorInfo[1] = "";
                errorInfo[2] = "";
                errorInfo[3] = split[1];   //支付流水号
                errorInfo[4] = opInfo[0];  //支付交易金额
                errorInfo[5] = opInfo[1];  //支付交易时间
                errorInfo[6] = "-";        //仅存在支付数据
                ans.add(new String[]{opInfo[1], String.join(",", errorInfo)});   //使用 支付交易时间作为排序元素
            } else {   //针对对二者均存在的交易，同一笔交易进行逐项比较
                String[] paymentInfo = paymentSystem.get(key);
                String[] operationInfo = operationSystem.get(key);
                //只对比交易金额和时间
                if (!paymentInfo[0].equals(operationInfo[0]) && !paymentInfo[1].equals(operationInfo[1])) {
                    ans.add(getErrorInfo(operationSystem, paymentSystem, key, "DE"));     //金额和时间均不一致
                } else if (!paymentInfo[1].equals(operationInfo[1])) {
                    ans.add(getErrorInfo(operationSystem, paymentSystem, key, "E"));      //仅时间不一致
                } else if (!paymentInfo[0].equals(operationInfo[0])) {
                    ans.add(getErrorInfo(operationSystem, paymentSystem, key, "D"));      //仅金额不一致
                }
            }
        }
        //按照时间升序排序
        ans.sort((o1, o2) -> o1[0].compareTo(o2[0]));
        //输出最终结果
        for (String[] errorInfo : ans) {
            System.out.println(errorInfo[1]);
        }
    }

    private static String[] getErrorInfo(HashMap<String, String[]> operationSystem, HashMap<String, String[]> paymentSystem, String key, String errorInfo) {
        //输出格式为：业务订单号,业务交易金额,业务交易时间,支付流水号,支付交易金额,支付交易时间,差错原因：
        StringBuilder builder = new StringBuilder();
        String[] opInfo = operationSystem.get(key);
        String[] payInfo = paymentSystem.get(key);
        String[] split = key.split("\\|");
        builder.append(split[0]).append(",").append(opInfo[0]).append(",").append(opInfo[1]).append(",")
                .append(split[1]).append(",").append(payInfo[0]).append(",").append(payInfo[1]).append(",")
                .append(errorInfo);
        return new String[]{opInfo[1].length() != 0 ? opInfo[1] : payInfo[1], builder.toString()};
    }

}
