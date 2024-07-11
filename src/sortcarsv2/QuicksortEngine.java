package sortcarsv2;

import sortcarsv2.Worker;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class QuicksortEngine {

    LinkedList<Thread> threadPool = new LinkedList<>();
    LinkedList<SortJob> jobPool = new LinkedList<>();
    final int maxThreads;
    int activeThreads = 0;

    public QuicksortEngine(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    public synchronized void addJobs(SortJob job)
    {
        jobPool.addLast(job);
        if (activeThreads < maxThreads)
        {
            activeThreads++;
            Thread newThread = new Thread(new Worker(this));
            threadPool.add(newThread);
            newThread.start();
        }
    }



    void parallelQuicksort(SortJob job) {

        if (job.startIndex >=  job.endIndex) return;

        if (job.endIndex - job.startIndex  <= 50) {
            normalQuicksort(job.list, job.startIndex, job.endIndex);
        }
        else
        {
            int pivot = partition(job.list, job.startIndex, job.endIndex);
            SortJob leftSide = new SortJob(job.startIndex, )
        }
    }

    static void normalQuicksort(ArrayList<Car> arr, int left, int right) {

        if (left < right)
        {
            int pivot = partition(arr, left, right);
            normalQuicksort(arr, left, pivot - 1);
            normalQuicksort(arr, pivot + 1, right);
        }


    }


    /*I understand that 3 way pivot does not apply here because no 2 cars are the same
    * However, it is my preferred implementation because without it, if there
    * are many identical elements the runtime will always be worst case
    * and I like to remember that.*/
    public static int[] threeWayPartition(ArrayList<Car> arr, int left, int right) {
        //choose pivot randomly to maximize chances of equal workload
        int pivotIndex = new Random().nextInt(right - left + 1) + left;
        //put pivot at 0th index
        swap(arr, pivotIndex, left);
        pivotIndex=left;
        int i = left + 1;


        while (i < right)
        {
            int cmp = arr.get(i).compareTo(arr.get(pivotIndex));
            if (cmp < 0)
            {
                swap(arr, i, left);
                i++; left++;
            }
            else if (cmp > 0)
            {
                swap(arr, i, right);
                right--;
            }
            else
            {
            i++;
            }
        }
        return new int[]{left, right};
    }


    //simple partition
    public static int partition(ArrayList<Car> arr, int left, int right) {
        int pivot = left;
        left++;
        while (left <= right) {
            int cmp = arr.get(left).compareTo(arr.get(pivot));
            if (cmp < 0)
            {
                left++;
            }
            else {
                swap(arr,left,right);
                right--;
            }
        }
        swap(arr,pivot,right);
        return right;
    }

    public static void swap(ArrayList<Car> arr, int i, int j) {
        Car temp = arr.get(i);
        arr.set(i, arr.get(j));
        arr.set(j, temp);
    }


}
