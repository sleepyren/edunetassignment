package sortcarsv3;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import static sortcarsv3.Car.writeCarArraysToFile;
import static sortcarsv3.RandomUtility.generateKListsOfNCars;

public class QuicksortEngine {
    LinkedList<SortJob> jobs = new LinkedList<SortJob>();
    List<Thread> threadPool = new LinkedList<Thread>();
    final int maxThreads;
    int activeThreads = 0;

    public QuicksortEngine(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    public synchronized void addJob(SortJob job)
    {
        jobs.add(job);
        if (activeThreads < maxThreads)
        {
            activeThreads++;
            Thread newThread = new Thread(new Worker(this));
            newThread.start();
        }
    }

    public synchronized List<Thread> waitForListCompletion()
    {
        while (!jobs.isEmpty() || this.threadPool.size() < this.activeThreads) {
            try{
                wait();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        return new LinkedList<>(this.threadPool); //reference clone
    }

    public void terminateThreads(List<Thread> threads)
    {
        for (Thread thread : threads)
        {
            thread.interrupt();
        }

        for (Thread thread : threads)
        {
            try{
               thread.join();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args){

        if (args.length != 3) {
            System.err.println("Usage: java QuicksortEngine <listCount> <carCount> <maxThreads>");
            return;
        }


        List<List<Car>> list = generateKListsOfNCars(Integer.parseInt(args[1]), Integer.parseInt(args[0]));
        QuicksortEngine engine = new QuicksortEngine(Integer.parseInt(args[2]));
        writeCarArraysToFile(list, false);

        for (int i = 0; i < list.size(); i++)
        {
            engine.addJob( new SortJob(list.get(i), engine) );

            List<Thread> threads = engine.waitForListCompletion();
            if (i == list.size() - 1) engine.terminateThreads(threads);
        }


        writeCarArraysToFile(list, true);


    }


}
