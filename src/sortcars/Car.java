package sortcars;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class Car implements Comparable<Car>{
    private final long REC_ID;
    private final String SERIAL;
    private final String DESTINATION;
    private COLOR COLORENUM;

    private static final HashMap<String, Integer> map = getDestinationMap();

    protected enum COLOR {RED("Red"), BLUE("Blue"), BLACK("Black"), WHITE("White");

        private final String colorName;

        COLOR(String colorName) {
            this.colorName = colorName;
        }

        public String getColorName()
        {
            return this.colorName;
        }

        public static COLOR fromOrdinal(int i){

            COLOR[] values = values();
            if (i<0 || i>=values.length) throw new IllegalArgumentException("Invalid ordinal");
            return values[i];
        }

    }

    public static HashMap<String, Integer> getDestinationMap() {

        HashMap<String, Integer> map = new HashMap<>();
        map.put("New York", 4);
        map.put("Miami", 3);
        map.put("New Orleans", 2);
        map.put("Houston", 1);
        map.put("Los Angeles", 0);
        return map;

    }


    Car(long rec_id, String serial, String destination, int color) {
        this.REC_ID = rec_id;
        this.SERIAL = serial;
        this.DESTINATION = destination;
        this.COLORENUM = COLOR.fromOrdinal(color);
    }

    @Override
    public String toString() {
        return this.SERIAL + '\t' + Integer.toString(this.COLORENUM.ordinal())
                + '\t' + this.DESTINATION + '\t' + Long.toString(this.REC_ID);
    }


    @Override
    public int compareTo(Car car) {
        int compareDestination = map.get(this.DESTINATION) - map.get(car.DESTINATION);
        if (compareDestination == 0) {
            int compareColorEnum = this.COLORENUM.ordinal() - car.COLORENUM.ordinal();
            if (compareColorEnum == 0) return this.SERIAL.compareTo(car.SERIAL);
            else {
                return compareColorEnum;
            }
        }
        else
            return compareDestination;
    }

    public static void writeCarArraysToFile(List<LinkedList<Car>> carsList, boolean sorted){
        String suffix = sorted ? "-sorted" : "";
        File folder = new File("cars");
        if (!folder.exists()) folder.mkdir();

        //if it still doesn't exist exit
        if (!folder.exists()) {
            System.err.println("Folder does not exist");
            System.exit(1);
        }

        try {
            //for (int i = 0; i < arr.length; i++) {
            int i = 0;
            for (LinkedList<Car> singleCarList : carsList) {


                String filename = "cars" + (i+1) + suffix + ".txt";
                StringBuilder builder = new StringBuilder();
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(
                        new File("cars" + File.separator + filename)));
                //Car[] carFileElements = arr[i];
                int j = 0;
                int length = singleCarList.size();
                for (Car carFileElement : singleCarList) {
                    builder.append(carFileElement.toString());
                    //don't add a newline on last line
                    if (j < length - 1) builder.append("\n");
                    j++;
                }
                bufferedWriter.write(builder.toString());
                bufferedWriter.flush();
                bufferedWriter.close();
                i++;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

        List<LinkedList<Car>> cars = RandomUtility.generateKListsOfNCars(100,3);
        for (Car c : cars.get(0)) System.out.println(c);
        ListIterator<Car> listIterator = cars.get(0).listIterator(0);
        Car car1 = listIterator.next();
        ListIterator<Car> listIterator2 = cars.get(0).listIterator(1);
        Car car2 = listIterator2.next();
       // QuicksortEngine.swap(listIterator, car1, listIterator2, car2);
        QuicksortEngine.quickSort(cars.get(0),0, 99);
        writeCarArraysToFile(cars, false);

        //System.out.println(cars.);
    }


}
