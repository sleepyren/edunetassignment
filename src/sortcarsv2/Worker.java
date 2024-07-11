package sortcarsv2;

public class Worker implements Runnable {
    private final QuicksortEngine engine;
    public Worker(QuicksortEngine engine) {
        this.engine = engine;
    }

    public void run() {
        while(true) {

            synchronized(engine) {
                while (engine.jobPool.isEmpty())
                {
                    try
                    {
                    engine.wait();
                    }
                    catch (InterruptedException e)
                    {
                        Thread.currentThread().interrupt();
                        System.err.println("Thread Interrupted");
                        engine.threadPool.remove(Thread.currentThread());
                        return;
                    }
                }
                SortJob job = engine.jobPool.poll();
                executeJob(job);
            }

        }
    }


    void executeJob(SortJob job)
    {

    }
}
