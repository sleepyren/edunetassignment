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
                        engine.threadPool.add(Thread.currentThread());
                    engine.wait();
                    }
                    catch (InterruptedException e)
                    {
                        engine.activeThreads--;
                        Thread.currentThread().interrupt();
                        System.err.println("Thread Interrupted");
                        engine.threadPool.remove(Thread.currentThread());
                        return;
                    }
                }
                engine.threadPool.remove(Thread.currentThread());
                SortJob job = engine.jobPool.pollFirst();
                System.out.println("Executing job: \n");
                executeJob(job);
            }

        }
    }


    void executeJob(SortJob job)
    {
    engine.parallelQuicksort(job);
    }
}
