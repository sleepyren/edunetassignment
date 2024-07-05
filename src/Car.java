import java.util.HashMap;

public class Car implements Comparable<Car>{
    private final long REC_ID;
    private final String SERIAL;
    private final String DESTINATION;
    private COLOR COLORENUM;

    private enum COLOR {RED("Red"), BLUE("Blue"), BLACK("Black"), WHITE("White");

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

    private static final HashMap<String, Integer> map = getDestinationMap();

    public static HashMap<String, Integer> getDestinationMap() {

        HashMap<String, Integer> map = new HashMap<>();
        map.put("New York", 0);
        map.put("Miami", 1);
        map.put("New Orleans", 2);
        map.put("Houston", 3);
        map.put("Los Angeles", 4);
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
                + '\t' + this.DESTINATION + '\t' + Long.toString(this.REC_ID) + '\n';
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
}
