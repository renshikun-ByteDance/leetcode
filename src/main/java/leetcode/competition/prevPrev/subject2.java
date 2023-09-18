package leetcode.competition.prevPrev;

import java.util.ArrayList;
import java.util.Scanner;

public class subject2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String information = scanner.nextLine();
        String[] split = information.split("\\s+");
        int nums = Integer.parseInt(split[0]);
        int[] attack = new int[split.length - 1];
        System.arraycopy(split, 1, attack, 0, attack.length);
        int ans = 0;


        System.out.println(ans);
    }


}
