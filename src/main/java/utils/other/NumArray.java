package utils.other;

class NumArray {

    private int[] prefixSum = new int[(int) (Math.pow(10, 4) + 1)];

    public NumArray(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }
    }

    public int sumRange(int left, int right) {
        int ans = 0;
        ans = prefixSum[right + 1] - prefixSum[left];
        return ans;
    }
}
