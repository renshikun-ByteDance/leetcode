package utils.other;

public class ParkingSystem {

    private static int big;
    private static int medium;
    private static int small;

    public ParkingSystem(int big, int medium, int small) {
        ParkingSystem.big = big;
        ParkingSystem.medium = medium;
        ParkingSystem.small = small;
    }

    public boolean addCar(int carType) {
        if (carType == 1 && big > 0) {
            big--;
            return true;
        } else if (carType == 2 && medium > 0) {
            medium--;
            return true;
        } else if (carType == 3 && small > 0) {
            small--;
            return true;
        }
        return false;
    }


}
