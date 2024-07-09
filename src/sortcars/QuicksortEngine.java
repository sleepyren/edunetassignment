package sortcars;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static sortcars.Car.writeCarArraysToFile;

public class QuicksortEngine {
    private final LinkedList<Worker> threadpool = new LinkedList<>();
    private final int maxThreads;
    private final LinkedList<SortJob> jobs = new LinkedList<>();
    public final Object completionMonitor = new Object();
    public int activeThreads = 0;

    QuicksortEngine(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    public LinkedList<Worker> getThreadPool() {
        return this.threadpool;
    }

    public LinkedList<SortJob> getJobs() {
        return this.jobs;
    }

    public void notifyMainThread() {
        synchronized (completionMonitor) {
            completionMonitor.notify();
        }
    }

    public static void quickSort(List<Car> cars) {
        quickSort(cars, 0, cars.size() - 1);
    }

    public static void quickSort(List<Car> cars, int low, int high) {
        if (low < high) {
            int[] partitions = partition(cars, low, high);
            quickSort(cars, low, partitions[0] - 1);
            quickSort(cars, partitions[1] + 1, high);
        }
    }

    private static int[] partition(List<Car> cars, int low, int high) {
        int pivotIndex = low + new Random().nextInt(high - low + 1);
        Car pivot = cars.get(pivotIndex);
        swap(cars, low, pivotIndex);

        int less = low;
        int current = low + 1;
        int greater = high;

        while (current <= greater) {
            if (cars.get(current).compareTo(pivot) < 0) {
                less++;
                swap(cars, current, less);
                current++;
            } else if (cars.get(current).compareTo(pivot) > 0) {
                swap(cars, current, greater);
                greater--;
            } else {
                current++;
            }
        }

        swap(cars, low, less);
        return new int[]{less, greater};
    }

    private static void swap(List<Car> cars, int index1, int index2) {
        Car temp = cars.get(index1);
        cars.set(index1, cars.get(index2));
        cars.set(index2, temp);
    }

    public void addJobs(List<List<Car>> carLists, List<Thread> threadlist) {
        List<SortJob> jobList = executionStrategy(carLists, this.maxThreads);
        for (SortJob job : jobList) {
            synchronized (this) {
                this.jobs.add(job);
                if (activeThreads >= maxThreads) return;

                if (this.threadpool.isEmpty()) {
                    Thread newThread = new Thread(new Worker(this));
                    threadlist.add(newThread);
                    newThread.start();
                    activeThreads++;
                } else {
                    this.notify();
                }
            }
        }
    }

    public static List<SortJob> executionStrategy(List<List<Car>> carLists, int maxThreads,
    List<List<SortJob>> combineJobs) {
        List<SortJob> sortJobs = new ArrayList<>();
        int numberOfLists = carLists.size();
        double ratio = (double) maxThreads / numberOfLists;

        if (ratio <= 1.0) {
            for (List<Car> list : carLists) {
                sortJobs.add(new SortJob(list));
            }
        } else {
            for (List<Car> list : carLists) {
                List<SortJob> listGrouping = new ArrayList<>();
                int sublistSize = (int) Math.ceil((double) list.size() / ratio);
                int startIndex = 0;
                while (startIndex < list.size()) {
                    int endIndex = Math.min(startIndex + sublistSize, list.size()) - 1;
                    SortJob partialJob = new SortJob(list, startIndex, endIndex);
                    sortJobs.add(partialJob);
                    listGrouping.add(partialJob);
                    startIndex = endIndex + 1;
                }
                combineJobs.add(listGrouping);
            }
        }
        return sortJobs;
    }

    public static List<Car> combineSortedLists(List<List<SortJob>> combineJobs){

    }

    public static void main(String[] args) throws InterruptedException {
        List<List<Car>> cars = RandomUtility.generateKListsOfNCars(1000, 3);
        QuicksortEngine engine = new QuicksortEngine(8);
        List<Thread> threadlist = new ArrayList<>();
        engine.addJobs(cars, threadlist);

        synchronized (engine.completionMonitor) {
            while (engine.getThreadPool().size() < engine.activeThreads) {
                System.out.println("activeThreads: " + engine.activeThreads + " thread pool size: " + engine.getThreadPool().size());
                engine.completionMonitor.wait();
            }
        }
        for (Thread thread : threadlist) {
            thread.interrupt();
            thread.join();
        }

        writeCarArraysToFile(cars, false);
    }
}
