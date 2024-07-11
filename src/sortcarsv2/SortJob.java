package sortcarsv2;

import java.util.ArrayList;
import java.util.List;

public class SortJob {
    public int startIndex;
    public int endIndex;
    public List<Car> list;


    public SortJob(int startIndex, int endIndex, List<Car> list) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.list = list;
    }

}
