package utils.other;



import java.util.HashMap;
import java.util.HashSet;

/**
 * 6417. 频率跟踪器
 */
public class FrequencyTracker {

    private HashMap<Integer, Integer> map;
    private HashMap<Integer, HashSet<Integer>> freqMap;

    public FrequencyTracker() {
        this.map = new HashMap<>();
        this.freqMap = new HashMap<>();
    }

    public void add(int number) {
        if (map.containsKey(number)) {
            freqMap.get(map.get(number)).remove(number);
        }
        map.put(number, map.getOrDefault(number, 0) + 1);
        Integer freq = map.get(number);
        HashSet<Integer> list = freqMap.getOrDefault(freq, new HashSet<>());
        list.add(number);
        freqMap.put(freq, list);
    }

    public void deleteOne(int number) {
        if (map.containsKey(number)) {
            Integer freq = map.get(number);
            freqMap.get(freq).remove(number);
            freq--;
            if (freq == 0) {
                map.remove(number);
            } else {
                map.put(number, freq);
                HashSet<Integer> list = freqMap.getOrDefault(freq, new HashSet<>());
                list.add(number);
                freqMap.put(freq, list);
            }
        }

    }

    public boolean hasFrequency(int frequency) {
        return freqMap.containsKey(frequency) && freqMap.get(frequency).size() > 0;
    }

}
