package leetcode.important;

import org.omg.PortableInterceptor.INACTIVE;

import java.util.HashMap;
import java.util.HashSet;

public class EveryDay {

    /**
     * 1037. 有效的回旋镖
     */
    public boolean isBoomerang(int[][] points) {
        int[] xy1 = points[0];
        int[] xy2 = points[1];
        int[] xy3 = points[2];
//        return (xy2[1] - xy1[1]) / (xy2[0] - xy1[0]) != (xy3[1] - xy1[1]) / (xy3[0] - xy1[0]);
        return (xy2[1] - xy1[1]) * (xy3[0] - xy1[0]) != (xy3[1] - xy1[1]) * (xy2[0] - xy1[0]);
    }


}
