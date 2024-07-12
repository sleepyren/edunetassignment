package sortcarsv2;

import sortcarsv2.Worker;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
        //this.notify();
        if (activeThreads < maxThreads)
        {
            this.activeThreads++;
            Thread newThread = new Thread(new Worker(this));
            //threadPool.add(newThread);
            newThread.start();
        }
    }

    public synchronized void waitForCompletion()
    {
        //needs to be || not && because thread pool could be full but there are still jobs
        while (activeThreads != this.threadPool.size() || this.jobPool.size() != 0)
        {
            try{
                this.notifyAll();
                this.wait();

            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

    }


    //this function is called after completion
    //the worker threads can still add and remove themselves from the pool
    //suppose there is one thread that wakes up other threads
    //these threads will wake up remove themselves from the pool,
    //then add themselves, threadPoolReference makes a clone of the references
    //before any other thread even edits it, therefore it does not matter
    //if they edit it
    public void terminateThreads()
    {
        List<Thread> threadPoolReference;
        synchronized (this){

            threadPoolReference = new ArrayList<>(this.threadPool);
        }
        for (Thread thread : threadPoolReference)
        {
            try
            {
                thread.interrupt();
                thread.join();
            }

            catch (InterruptedException e2)
            {e2.printStackTrace();
                return;}

        }
    }


      void parallelQuicksort(SortJob job) {

        if (job.startIndex >=  job.endIndex) return;

        //50 elements is small enough to sort in one thread
        if (job.endIndex - job.startIndex  <= 50) {
            normalQuicksort(job.list, job.startIndex, job.endIndex);
        }
        else
        {
            int pivot = partition(job.list, job.startIndex, job.endIndex);
            SortJob leftSide = new SortJob(job.startIndex, pivot - 1, job.list);
            SortJob rightSide = new SortJob(pivot + 1, job.endIndex,  job.list);
            addJobs(leftSide);
            addJobs(rightSide);
        }
    }

    static void normalQuicksort(List<Car> arr, int left, int right) {

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
    public static int partition(List<Car> arr, int left, int right) {
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

    public static void swap(List<Car> arr, int i, int j) {
        Car temp = arr.get(i);
        arr.set(i, arr.get(j));
        arr.set(j, temp);
    }

    public static void main(String[] args) {
        QuicksortEngine engine = new QuicksortEngine(10);
        List<Car> cars = RandomUtility.generateNCars(100000);
        SortJob job = new SortJob(0, cars.size()-1, cars);
       //engine.parallelQuicksort(job);
        normalQuicksort(cars, 0 , cars.size() - 1);
        List<List<Car>> listofLists = new ArrayList<>();
        listofLists.add(cars);
       // engine.waitForCompletion();
        //engine.terminateThreads();
        Car.writeCarArraysToFile(listofLists, true);

    }

}
