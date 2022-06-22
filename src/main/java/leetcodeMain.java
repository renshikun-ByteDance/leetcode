import com.sun.jmx.remote.internal.ArrayQueue;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import leetcode.algorithm.*;
import leetcode.important.Working;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class leetcodeMain {

    private final static Logger logger = LoggerFactory.getLogger(leetcodeMain.class);

    public static void main(String[] args) throws InterruptedException {
        greedy greedy = new greedy();
        HashTable hashTable = new HashTable();
        BackTrack backTrack = new BackTrack();
        Working work = new Working();
        Simulation simulation = new Simulation();
        prefixSum prefixSum = new prefixSum();
        Bucket bucket = new Bucket();
        queue queue = new queue();
        String path = "/a/./b/../../c/";
//        String path = "/a/../../b/../c//.//";    //官方预期 "/c"，确定？
        //        new ArrayQueue<>()
        System.out.println(simulation.simplifyPath(path));

        int[][] properties = {{7, 7}, {1, 2}, {9, 7}, {7, 3}, {3, 10}, {9, 8}, {8, 10}, {4, 3}, {1, 5}, {1, 5}};

        System.out.println(work.numberOfWeakCharacters02(properties));


    }


}

