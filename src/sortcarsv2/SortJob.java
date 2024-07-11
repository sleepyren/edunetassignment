package sortcarsv2;

import java.util.ArrayList;

public class SortJob {
    public int startIndex;
    public int endIndex;
    public ArrayList<Car> list;


    public SortJob(int startIndex, int endIndex, ArrayList<Car> list) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.list = list;
    }

}
