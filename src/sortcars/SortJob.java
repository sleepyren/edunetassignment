package sortcars;

import java.util.List;

public class SortJob {



    private final List<Car> list;
    private  int startIndex;
    private int endIndex;
    private final boolean sortEntireList;

    public SortJob(List<Car> list) {
        this.list = list;
        this.sortEntireList = true;
    }

    public SortJob(List<Car> list, int startIndex, int endIndex) {
        this.list = list;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.sortEntireList = false;
    }


    public List<Car> getList() {
        return list;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public boolean sortEntireList() { return sortEntireList; }
}
