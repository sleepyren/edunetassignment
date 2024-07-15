package sortcarsv3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class RandomUtility {
    public static final Random random = new Random();
    public static String generateSerialNumber(){
        char[] arr = new char[12];
        for(int i = 0; i < arr.length; i++){

            //if true choose a letter
            if (random.nextBoolean())
                arr[i] = (char) ('A' + random.nextInt(26));

            else
                arr[i] = (char) ('0' + random.nextInt(10));
        }

        return new String(arr);
    }

    private static final String[] locations = {"Los Angeles", "Houston", "New Orleans", "Miami", "New York"};
    public static String generateLocation(){
        return locations[random.nextInt(locations.length)];
    }

    private static int generateColor(){
        return random.nextInt(4);
    }
    public static List<Car> generateNCars(int n) {
        List<Car> cars = new ArrayList<>();
        HashSet<String> set = new HashSet<>();

        for (int i = 0; i < n; i++) {
            String location = generateLocation();
            int color = generateColor();
            String serialNumber = generateSerialNumber();

            while (set.contains(serialNumber)) serialNumber = generateSerialNumber();
            set.add(serialNumber);

            Car newCar = new Car(i, serialNumber, location, color);
            cars.add(newCar);
        }

        return cars;
    }

    public static List<List<Car>> generateKListsOfNCars(int n, int k) {
        List<List<Car>> allCars = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            allCars.add(generateNCars(n));
        }
        return allCars;
    }




}
