package utils.other;

import java.util.HashMap;

public class MyHashMap {

    private HashMap<Integer, Integer> myMap;

    public MyHashMap() {
        this.myMap = new HashMap<>();
    }

    public void put(int key, int value) {
        myMap.put(key, value);
    }

    public int get(int key) {
        if (myMap.containsKey(key)) return myMap.get(key);
        return -1;
    }

    public void remove(int key) {
        if (myMap.containsKey(key)) myMap.remove(key);
    }

}
