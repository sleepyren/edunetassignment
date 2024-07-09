package sortcars;

import java.util.List;

public class Worker implements Runnable {
    private final QuicksortEngine engine;

    public Worker(QuicksortEngine engine) {
        this.engine = engine;
    }

    @Override
    public void run() {
        while (true) {
            SortJob job;
            synchronized (engine) {
                while (engine.getJobs().isEmpty()) {
                    try {
                        engine.getThreadPool().add(this);
                        engine.notifyMainThread();
                        engine.wait();
                        engine.getThreadPool().remove(this);
                    } catch (InterruptedException e) {
                        engine.getThreadPool().remove(this);
                        engine.activeThreads--;
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                job = engine.getJobs().pollLast();
            }
            executeJob(job);
        }
    }

    private void executeJob(SortJob job) {
        if (job != null) {
            if (job.isCombineJob()) {
                long startTime = System.currentTimeMillis();
                List<Car> sortedList = QuicksortEngine.combineSortedSublists(job.getCombineJob());
                long endTime = System.currentTimeMillis();
                synchronized (engine)
                {
                    engine.getFinalSortedList().add(job.partialSortGroup, sortedList);
                    job.partialSortCompletionTimeList.add(endTime - startTime);
                    engine.partialSortCompletionTimeList.add(job.partialSortGroup, job.partialSortCompletionTimeList);
                }
            }
            else if (job.sortEntireList()) {
                long startTime = System.currentTimeMillis();
                QuicksortEngine.quickSort(job.getList());
                long endTime = System.currentTimeMillis();
                //job.partialSortCompletionTime = endTime - startTime;
                synchronized (engine) {
                    engine.sortCompletionTimeList.set(job.sortId, endTime - startTime);
                }
            } else {
                long startTime = System.currentTimeMillis();
                QuicksortEngine.quickSort(job.getList(), job.getStartIndex(), job.getEndIndex());
                long endTime = System.currentTimeMillis();
                job.partialSortCompletionTime = endTime - startTime;
            }
        }
    }
}
