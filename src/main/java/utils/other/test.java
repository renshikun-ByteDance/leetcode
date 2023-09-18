package utils.other;

public class test {
    public static void main(String[] args) {


        String abc = null;
        System.out.println(abc instanceof String);
        System.out.println(String.valueOf(abc) instanceof String);

        StringBuilder appendKey = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            appendKey.append(i).append(",");
        }
        System.out.println(appendKey.toString());
        System.out.println(appendKey.deleteCharAt(appendKey.lastIndexOf(",")));

        int regionNum = Math.abs("13526408999".hashCode()) % 50;
        System.out.println(regionNum);

        System.out.println(String.format("%0" + 4 + "d", Math.abs("13526408999".hashCode()) % 50));


    }

}
