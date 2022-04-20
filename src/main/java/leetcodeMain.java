import leetcode.Simulation;
import leetcode.test;

import java.util.Arrays;

public class leetcodeMain {

    public static void main(String[] args) {
        Simulation simulation = new Simulation();

        String path = "/a/./b/../../c/";
//        String path = "/a/../../b/../c//.//";    //官方预期 "/c"，确定？
        System.out.println(simulation.simplifyPath(path));

//        String words = ", , , ,        a, eaefa";
        String words = "";
        System.out.println(simulation.countSegments(words));


        int[][] matrix = new int[][]{{1,2,3,4},{5,6,7,8},{9,10,11,12}};
        System.out.println(simulation.spiralOrder(matrix));


    }

}
