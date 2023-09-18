package utils;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommonUtils {

    public static void main(String[] args) {

        HashMap<String, Float> typeAndWeight = new HashMap<>();
        typeAndWeight.put("食品类", (float) 0.110);
        typeAndWeight.put("酒店类", (float) 0.891);
        typeAndWeight.put("出行类", (float) 0.006);
        typeAndWeight.put("其他类", (float) 0.906);
        typeAndWeight.put("", (float) 0.386);

        //1.1、HashMap基于Stream排序，不会改变原始HashMap中数据的顺序
        String sortedBaseOnStream = typeAndWeight.entrySet().stream()
                .filter(t -> !t.getKey().isEmpty())    //过滤Filter
                .filter(t -> t.getValue() > 0.8)       //过滤Filter
                .sorted(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()))  //排序
                .limit(3)     //限制最多返回三个，如果排序，即为Top3
                .map(t -> t.getKey() + ":" + t.getValue())     //转换Map
                .collect(Collectors.joining("~"));   //转换，将多个String用"~"拼接

        //1.2、HashMap基于List自带的sort函数排序，更改了HashMap中数据的顺序
        List<Map.Entry<String, Float>> list = new ArrayList<>(typeAndWeight.entrySet());
        list.sort(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()));
        //获取遍历HashMap的遍历器
        Iterator<Map.Entry<String, Float>> iterator = list.iterator();


        //获取Value最大的值Map
        String maxTuple = list.stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .get().toString();

        //获取Value最小的值Map
        String intTuple = list.stream()
                .min(Comparator.comparing(Map.Entry::getValue))
                .get().toString();

        //求和
        double sum = list.stream()
                .mapToDouble(Map.Entry::getValue)
                .sum();

        //计数
        long count = list.stream()
                .count();

        //获取第一个元素
        Optional<Map.Entry<String, Float>> first = list.stream()
                .findFirst();
        System.out.println(first.get());

        //去重Distinct
        String collect = list.stream()
                .map(Map.Entry::getKey)
                .distinct()  //去重
                .collect(Collectors.joining("~"));   //.forEach(System.out::println);
        System.out.println(collect);



        //针对集合使用Stream，与上大同小异
        List<String> lis = Arrays.asList("aa", "bb", "cc", "", "BB");
        String s = lis.stream()
                .sorted(Comparator.reverseOrder())
                .findFirst().get();
        System.out.println(s);

        List<String> collectList = lis.stream()
                .filter(t -> !t.isEmpty())
                .map(String::toLowerCase)
                .distinct()
                .collect(Collectors.toList());
        System.out.println(collectList);

    }
}
