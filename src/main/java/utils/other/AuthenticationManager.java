package utils.other;

import java.util.HashMap;
import java.util.HashSet;

public class AuthenticationManager {

    //验证码的有效期
    private int timeToLive;
    //记录每个 token的有效终止时间
    private HashMap<String, Integer> tokenAndEndTime;


    public AuthenticationManager(int timeToLive) {
        this.timeToLive = timeToLive;
        this.tokenAndEndTime = new HashMap<>();
    }

    public void generate(String tokenId, int currentTime) {
        tokenAndEndTime.put(tokenId, currentTime + timeToLive);
    }

    public void renew(String tokenId, int currentTime) {
        if (tokenAndEndTime.containsKey(tokenId)) {
            Integer prevEndTime = tokenAndEndTime.get(tokenId);
            if (prevEndTime <= currentTime) {
                tokenAndEndTime.remove(tokenId);
                return;  //停止
            }
            tokenAndEndTime.put(tokenId, currentTime + timeToLive);
        }
    }

    public int countUnexpiredTokens(int currentTime) {
        int ans = 0;
        HashSet<String> remove = new HashSet<>();
        for (String tokenId : tokenAndEndTime.keySet()) {
            Integer endTime = tokenAndEndTime.get(tokenId);
            if (endTime <= currentTime) {
                remove.add(tokenId);
            } else {
                ans++;
            }
        }
        for (String tokenId : remove) {
            tokenAndEndTime.remove(tokenId);
        }
        return ans;
    }
}

