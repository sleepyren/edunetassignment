package sortcars;

import java.util.HashSet;
import java.util.Random;

public class RandomUtility {
    private static final Random random = new Random();
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

    public static Car[] generateNCars(int n) {

        Car[] cars = new Car[n];
        HashSet<String> set = new HashSet<>();
        for(int i = 0; i < n; i++){
        {
            String location = generateLocation();
            int color = generateColor();
            String serialNumber = generateSerialNumber();

            while (set.contains(serialNumber)) serialNumber = generateSerialNumber();
            set.add(serialNumber);

            cars[i] = new Car(i, serialNumber, location, color);

        }
    }
        return cars;
    }

    public static Car[][] generateKListsOfNCars(int n, int k) {
        Car[][] cars = new Car[k][n];
        for (int i = 0; i < k; i++){
            cars[i] = generateNCars(n);
        }
        return cars;
    }

}