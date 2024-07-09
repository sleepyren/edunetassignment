package sortcars;

import java.util.*;
import java.util.stream.LongStream;

import static sortcars.Car.writeCarArraysToFile;

public class QuicksortEngine {
    private final LinkedList<Worker> threadpool = new LinkedList<>();
    private final int maxThreads;
    private final LinkedList<SortJob> jobs = new LinkedList<>();
    public final Object completionMonitor = new Object();
    public int activeThreads = 0;
    private boolean isPartialSort = false;
    private int partialGroupCount;
    private ArrayList<List<Car>> finalSortedList;
    public List<Long> sortCompletionTimeList;
    public List<List<Long>> partialSortCompletionTimeList;



    QuicksortEngine(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    public ArrayList<List<Car>> getFinalSortedList() {
        return finalSortedList;
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

    public void addJobs(List<List<Car>> carLists, List<Thread> threadlist, List<List<SortJob>> combineJobs) {
        List<SortJob> jobList = executionStrategy(carLists, this.maxThreads, combineJobs);
        //SortJob[] jobReferences = jobList.toArray(new SortJob[0]);

        for (SortJob job : jobList) {
            synchronized (this) {
                this.jobs.add(job);
                if (activeThreads >= maxThreads) continue; //jobReferences;

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
        return;
    }

    public List<SortJob> executionStrategy(List<List<Car>> carLists, int maxThreads,
                                           List<List<SortJob>> combineJobs) {
        this.partialGroupCount = carLists.size();
        this.finalSortedList = new ArrayList<>();
        List<SortJob> sortJobs = new ArrayList<>();
        int numberOfLists = carLists.size();
        double ratio = (double) maxThreads / numberOfLists;

        if (ratio <= 1.0) {

            this.sortCompletionTimeList = new ArrayList<>(Arrays.asList(new Long[carLists.size()]));
            for (int i = 0; i < carLists.size(); i++) {
                List<Car> list = carLists.get(i);
                sortJobs.add(new SortJob(list, i));


            }
        } else {
            this.isPartialSort = true;
            for (int i = 0; i < carLists.size(); i++) {
                List<Car> list = carLists.get(i);
                List<SortJob> listGrouping = new ArrayList<>();
                int sublistSize = (int) Math.ceil((double) list.size() / ratio);
                int startIndex = 0;
                while (startIndex < list.size()) {
                    int endIndex = Math.min(startIndex + sublistSize, list.size()) - 1;
                    SortJob partialJob = new SortJob(list, startIndex, endIndex,i);
                    sortJobs.add(partialJob);
                    listGrouping.add(partialJob);
                    startIndex = endIndex + 1;
                }
                combineJobs.add(listGrouping);
            }
        }
        return sortJobs;
    }

    public void addCombineJobs(List<List<SortJob>> combineJobs) {
        for (List<SortJob> combineJob : combineJobs) {
            synchronized (this) {
                this.jobs.add(new SortJob(combineJob, true));
                this.notify();
            }
        }
    }

    public static List<Car> combineSortedSublists(List<SortJob> jobList) {
        PriorityQueue<CarWithIndex> minHeap = new PriorityQueue<>(Comparator.comparing(carWithIndex -> carWithIndex.car));
        List<Car> combinedList = new ArrayList<>();
        List<Iterator<Car>> iterators = new ArrayList<>();

        // Initialize the iterators and add the first element of each sorted sublist to the heap
        for (SortJob job : jobList) {
            List<Car> sortedSublist = job.getList().subList(job.getStartIndex(), job.getEndIndex() + 1);
            if (!sortedSublist.isEmpty()) {
                Iterator<Car> iterator = sortedSublist.iterator();
                iterators.add(iterator);
                if (iterator.hasNext()) {
                    minHeap.add(new CarWithIndex(iterator.next(), iterators.size() - 1));
                }
            }
        }

        // Merge the sorted lists
        while (!minHeap.isEmpty()) {
            CarWithIndex smallestCarWithIndex = minHeap.poll();
            combinedList.add(smallestCarWithIndex.car);

            // Add the next element from the same iterator to the heap
            Iterator<Car> iterator = iterators.get(smallestCarWithIndex.index);
            if (iterator.hasNext()) {
                minHeap.add(new CarWithIndex(iterator.next(), smallestCarWithIndex.index));
            }
        }

        return combinedList;
    }

    // Helper class to keep track of the car and its iterator index
    private static class CarWithIndex {
        Car car;
        int index;

        CarWithIndex(Car car, int index) {
            this.car = car;
            this.index = index;
        }
    }


    public static void main(String[] args) throws InterruptedException {
        if (args.length != 3) {
            System.err.println("Usage: java QuicksortEngine <listCount> <carCount> <maxThreads>");
        }

        List<List<Car>> cars = RandomUtility.generateKListsOfNCars(Integer.parseInt(args[1]), Integer.parseInt(args[0]));
        writeCarArraysToFile(cars, false);
        QuicksortEngine engine = new QuicksortEngine(Integer.parseInt(args[2]));
        List<Thread> threadlist = new ArrayList<>();
        List<List<SortJob>> combineJobs = new ArrayList<>();


        engine.addJobs(cars, threadlist, combineJobs);


        if (engine.isPartialSort) {
            engine.partialSortCompletionTimeList = new ArrayList<>(cars.size());
            synchronized (engine.completionMonitor) {
                while (engine.getThreadPool().size() < engine.activeThreads) {
                    engine.completionMonitor.wait();
                }
            }
            engine.addCombineJobs(combineJobs);



        }



        synchronized (engine.completionMonitor) {
            while (engine.getThreadPool().size() < engine.activeThreads || !engine.jobs.isEmpty()) {
                engine.completionMonitor.wait();
            }
        }
        for (Thread thread : threadlist) {
            thread.interrupt();
            thread.join();
        }

        if (!engine.isPartialSort) {
            System.out.println("ONE THREAD PER LIST\n");
            for (int i = 0; i < cars.size(); i++) {
                System.out.println("List " + (i+1) + " Time : " + engine.sortCompletionTimeList.get(i) + " ms\n");
                writeCarArraysToFile(cars, true);
            }
            return;
        }


        System.out.println("MULTIPLE THREADS PER LIST\n");
        cars = engine.finalSortedList;
        for (int i = 0; i < cars.size(); i++)
        {
            List<Long> timeList = engine.partialSortCompletionTimeList.get(i);
            long max = Collections.max(timeList.subList(0, timeList.size()-1) );
            long sum = LongStream.range(0, timeList.size()-1).map(ele -> timeList.get((int) ele)).sum();

            System.out.println("List " + (i+1) + " Real Time: " +
                    (max + timeList.get(timeList.size()-1)) + "ms");

            System.out.println("CPU TIME (ms) : ");
            for (int j =0; j < timeList.size(); j++) {
                System.out.print(timeList.get(j));
                if (j != timeList.size()-1) System.out.print(" + ");
            }
            System.out.print( " = " + sum + "ms\n\n");
        }
        writeCarArraysToFile(cars, true);
        //cars.set(0,combineSortedSublists(combineJobs.get(0)));

    }
}