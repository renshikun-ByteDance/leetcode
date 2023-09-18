package leetcode.algorithm;

public class MathMethod {


    /**
     * 1247. 交换字符使得字符串相同
     */
    public int minimumSwap(String s1, String s2) {
        int xy = 0;
        int yx = 0;
        for (int i = 0; i < s1.length(); i++) {
            char aa = s1.charAt(i);
            char bb = s2.charAt(i);
            if (aa == 'x' && bb == 'y') {
                xy++;
            }
            if (aa == 'y' && bb == 'x') {
                yx++;
            }
        }
        //存在无法匹配的情况
        if (((xy + yx) & 1) != 0) {
            return -1;
        }
        //1、优先内部消化，仅需一次交换
        int one = xy / 2 + yx / 2;
        //2、其次穿插消化，需要两次交换
        int two = xy % 2 + yx % 2;
        return one + two;
    }


}
