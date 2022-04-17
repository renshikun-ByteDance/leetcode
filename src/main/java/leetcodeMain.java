import leetcode.Simulation;
import leetcode.test;

public class leetcodeMain {

    public static void main(String[] args) {
        Simulation simulation = new Simulation();

        String path = "/a/./b/../../c/";
//        String path = "/a/../../b/../c//.//";    //官方预期 "/c"，确定？
        System.out.println(simulation.simplifyPath(path));

        test test = new test();

        int[] nums = new int[]{-1};
        int k = 1;
        System.out.println(test.checkInclusion("a","ab"));


    }

}
