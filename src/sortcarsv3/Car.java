package sortcarsv3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;

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


    public Car(long rec_id, String serial, String destination, int color) {
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
    public static void writeCarArraysToFile(List<List<Car>> carsList, boolean sorted) {
        String suffix = sorted ? "-sorted" : "";
        File folder = new File("cars");
        if (!folder.exists()) folder.mkdir();

        if (!folder.exists()) {
            System.err.println("Folder does not exist");
            System.exit(1);
        }

        try {
            int i = 0;
            for (List<Car> carList : carsList) {
                String filename = "cars" + (i + 1) + suffix + ".txt";
                StringBuilder builder = new StringBuilder();
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File("cars" + File.separator + filename)));
                for (int j = 0; j < carList.size(); j++) {
                    builder.append(carList.get(j).toString());
                    if (j < carList.size() - 1) builder.append("\n");
                }
                bufferedWriter.write(builder.toString());
                bufferedWriter.flush();
                bufferedWriter.close();
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void swap(List<Car> arr, int i, int j) {
        Car temp = arr.get(i);
        arr.set(i, arr.get(j));
        arr.set(j, temp);
    }



}
