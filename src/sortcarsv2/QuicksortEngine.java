package sortcarsv2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class QuicksortEngine {


    LinkedList<Thread> threadPool = new LinkedList<>();
    LinkedList<SortJob> jobPool = new LinkedList<>();
    final int maxThreads;
    int activeThreads = 0;

    public QuicksortEngine(int maxThreads) {
        this.maxThreads = maxThreads;
    }


    synchronized void addJob(SortJob job) {
        this.jobPool.add(job);
        this.notifyAll();
        if (this.activeThreads < this.maxThreads) {
            activeThreads++;
            Thread newThread = new Thread(new Worker(this));
            newThread.start();
        }
    }

    public synchronized SortJob getSortJob()
    {
        if (this.jobPool.isEmpty()) return null;
        else return this.jobPool.poll();
    }

    static void normalQuicksort(List<Car> arr, int left, int right) {

        if (left < right)
        {
            int pivot = partition(arr, left, right);
            normalQuicksort(arr, left, pivot - 1);
            normalQuicksort(arr, pivot + 1, right);
        }


    }

    //simple partition
    public static int partition(List<Car> arr, int left, int right) {
        int pivot = left;
        left++;
        while (left <= right)
        {
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

    void waitAndTerminate()
    {
        synchronized (this) {


            while (!this.jobPool.isEmpty() || this.threadPool.size() != this.activeThreads) {
                try{
                    //this.notifyAll();
                    this.wait();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        ArrayList<Thread> threadPool = new ArrayList<>(this.threadPool);

        for (Thread thread : threadPool) {
            try {
                thread.interrupt();
                thread.join();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void parallelQuicksort(SortJob job)
    {

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
            addJob(leftSide);
            addJob(rightSide);
        }

    }

    public static void main(String[] args)
    {
        if (args.length != 3) {
            System.err.println("Usage: java QuicksortEngine <listCount> <carCount> <maxThreads>");
            return;
        }


        QuicksortEngine engine = new QuicksortEngine(Integer.parseInt(args[2]));
        List<List<Car>> list = RandomUtility.generateKListsOfNCars(
                Integer.parseInt(args[1]),Integer.parseInt(args[0]));
        Car.writeCarArraysToFile(list, false);


        int i = 0;
        for (List<Car> arr : list)
        {


            SortJob sortJob = new SortJob(0, arr.size()-1, arr);
            long begin = System.currentTimeMillis();
            engine.addJob(sortJob);
            engine.waitAndTerminate();
            long finish = System.currentTimeMillis();
            System.out.println("List " + i + ": " + (finish - begin) + " ms");
            i++;
        }

        Car.writeCarArraysToFile(list, true);
    }
}
