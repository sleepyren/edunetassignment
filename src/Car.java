public class Car {
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

}
