package sortcars;

import java.util.ArrayList;
import java.util.List;

public class SortJob {



    private List<Car> list;
    private  int startIndex;
    private int endIndex;
    private boolean sortEntireList;
    private boolean isCombineJob = false;
    private List<SortJob> combineJob;
    public int partialSortGroup;
    public long partialSortCompletionTime;
    public List<Long> partialSortCompletionTimeList;
    public int sortId;

    public SortJob(List<Car> list, int sortId) {
        this.list = list;
        this.sortEntireList = true;
        this.sortId = sortId;
    }

    public SortJob(List<Car> list, int startIndex, int endIndex, int partialSortGroup) {
        this.list = list;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.sortEntireList = false;
        this.partialSortGroup = partialSortGroup;
    }

    //special combineJobs constructor
    public SortJob(List<SortJob> combineJob, boolean isCombineJob)
    {
    this.isCombineJob = true;
    this.combineJob = combineJob;
    partialSortCompletionTimeList = new ArrayList<>();
    for (SortJob sortJob : combineJob)
    {
        partialSortCompletionTimeList.add(sortJob.partialSortCompletionTime);
    }
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

    public List<SortJob> getCombineJob() { return combineJob; }
    public boolean isCombineJob() { return isCombineJob; }
}
