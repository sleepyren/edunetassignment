package sortcars;

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
            if (job.sortEntireList()) {
                QuicksortEngine.quickSort(job.getList());
            } else {
                QuicksortEngine.quickSort(job.getList(), job.getStartIndex(), job.getEndIndex());
            }
        }
    }
}
