package utils.other;

import java.util.ArrayList;

/**
 * 855. 考场就座
 */
public class ExamRoom {
    private int n;
    private ArrayList<Integer> address;

    public ExamRoom(int n) {
        this.n = n;
        this.address = new ArrayList<>();
    }

    //--------------------------------------------------------------------
    // 本题采用最原始的模拟方式，每次都要排序，其中比较关键的是 两个边界点的处理
    //--------------------------------------------------------------------

    public int seat() {
        int m = address.size();
        //1、教室内没有学生落座
        if (m == 0) {
            address.add(0);
            return 0;
        }
        //2、教室内已有学生落座
        address.sort((o1, o2) -> o1 - o2);  //按照索引升序排序
        //3.1、左边界（作为初始化的值）
        int ans = 0;    //初始值为 0，默认为左边界
        int maxDist = address.get(0);  //初始距离，无需除以 2，因为当前初始化待插入位置为左边界 0，可以将新插入的位置定位边界
        //3.2、中间间隔
        for (int i = 1; i < m; i++) {
            //--------------------------------------------
            // 多个这样的座位，他会坐在编号最小的座位上，以下除以 2 和 比较大小满足上述逻辑
            //--------------------------------------------
            int currDist = (address.get(i) - address.get(i - 1)) / 2;
            if (currDist > maxDist) {
                maxDist = currDist;
                ans = address.get(i - 1) + currDist;
            }
        }
        //3.3、右边界
        if (n - 1 - address.get(m - 1) > maxDist) {   //距离，无需除以 2，考虑当前待插入位点是右边界，可以将新插入的位置定位边界
            ans = n - 1;
        }

        address.add(ans);
        return ans;
    }

    public void leave(int p) {
        address.remove((Integer) p);
    }

    public ArrayList<Integer> getAddress() {
        return address;
    }
}
