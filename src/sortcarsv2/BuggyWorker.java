package sortcarsv2;

public class BuggyWorker implements Runnable {
    private final BuggyQuicksortEngine engine;
    public BuggyWorker(BuggyQuicksortEngine engine) {
        this.engine = engine;
    }

    public void run() {
        while(!Thread.currentThread().isInterrupted()) {

            SortJob job=null;
            while (engine.jobPool.isEmpty()) {
                synchronized (engine) {

                    try {
                        engine.threadPool.add(Thread.currentThread());
                        engine.notifyAll();
                        engine.wait();
                        engine.threadPool.remove(Thread.currentThread());
                    } catch (InterruptedException e) {
                        engine.activeThreads--;
                        Thread.currentThread().interrupt();
                        System.err.println("Thread Interrupted");
                        engine.threadPool.remove(Thread.currentThread());
                        return;
                    }
                }
            }


            //if
            //NOTE that the READ of jobpool.isempty is not protected. therefore threads may
            //see that there are available jobs and queue up for the monitor meanwhile
            //they are going to poll a null element
            synchronized (engine) {
                if (engine.jobPool.isEmpty()) continue;
                engine.threadPool.remove(Thread.currentThread());
                job = engine.jobPool.pollFirst();

            }

            //process outside of synchronized block
            if (job!=null) executeJob(job);
        }
    }


    void executeJob(SortJob job)
    {
    engine.parallelQuicksort(job);
    }
}
