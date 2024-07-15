package sortcarsv3;


import java.util.ArrayList;
import java.util.List;
import sortcarsv3.Car;



public class SortJob {
    public int startIndex;
    public int endIndex;
    public List<Car> list;
    public int pivot;
    private final QuicksortEngine engine;

    //mainJob
    public SortJob(List<Car> list, QuicksortEngine engine) {
        this.startIndex = 0;
        this.endIndex = list.size()-1;
        this.list = list;
        this.engine = engine;
    }

    public SortJob(int startIndex, int endIndex, SortJob parent) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.engine = parent.engine;
        this.list = parent.list;
    }

    public static void swap(List<Car> arr, int i, int j) {
        Car temp = arr.get(i);
        arr.set(i, arr.get(j));
        arr.set(j, temp);
    }

    private void addChildJobs() {
        SortJob LeftSort = new SortJob(this.startIndex, this.pivot-1, this);
        SortJob RightSort = new SortJob(this.pivot+1, this.endIndex, this);

        this.engine.addJob(LeftSort);
        this.engine.addJob(RightSort);
    }

    private void partition() {
        this.pivot = this.startIndex;
        int left = this.startIndex+1;
        int right = this.endIndex;
        while (left <= right) {

            int comparison = this.list.get(left).compareTo(this.list.get(this.pivot));

            if (comparison < 0)
            {
                left++;
            }
            else
            {
                swap(this.list, left, right);
                right--;
            }
        }

        swap(this.list, this.pivot, right);
        this.pivot = right;

    }

    //partition current job and add two more jobs to list
    private void quickSort() {
        if (this.startIndex < this.endIndex) {
            this.partition();
            this.addChildJobs();
        }

    }

    public void executeJob()
    {
        quickSort();
    }



}
