package utils.other;

import java.util.HashMap;
import java.util.PriorityQueue;

public class FoodRatings {

    public static class Element {
        String food;
        String type;
        int rate;

        Element(String food, String type, int rate) {
            this.food = food;
            this.type = type;
            this.rate = rate;
        }

        public void changeRate(int rate) {
            this.rate = rate;
        }
    }

    private HashMap<String, PriorityQueue<Element>> map;
    private HashMap<String, Element> mpp;

    public FoodRatings(String[] foods, String[] cuisines, int[] ratings) {
        this.map = new HashMap<>();
        this.mpp = new HashMap<>();
        for (int i = 0; i < foods.length; i++) {
            Element element = new Element(foods[i], cuisines[i], ratings[i]);
            mpp.put(foods[i], element);
            PriorityQueue<Element> queue = map.getOrDefault(cuisines[i], new PriorityQueue<>((o1, o2) -> o1.rate == o2.rate ? o1.food.compareTo(o2.food) : o2.rate - o1.rate));
            queue.add(element);
            map.put(cuisines[i], queue);
        }
    }

    public void changeRating(String food, int newRating) {
        Element element = mpp.get(food);
        PriorityQueue<Element> queue = map.get(element.type);
        queue.remove(element);
        element.changeRate(newRating);
        queue.add(element);
        map.put(element.type, queue);
    }

    public String highestRated(String cuisine) {
        return map.get(cuisine).peek().food;
    }


}
