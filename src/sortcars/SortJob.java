package sortcars;

import java.util.LinkedList;

public class SortJob {



    private final LinkedList<Car> list;
    private final int startIndex;
    private final int endIndex;


    public SortJob(LinkedList<Car> list, int startIndex, int endIndex) {
        this.list = list;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }


    public LinkedList<Car> getList() {
        return list;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }
}
