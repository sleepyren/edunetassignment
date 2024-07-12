package sortcarsv2;

public class Worker implements Runnable{
    final QuicksortEngine engine;
    public Worker(QuicksortEngine engine) {
        this.engine = engine;
    }

    @Override
    public void run() {

        while (true) {

            while (engine.jobPool.isEmpty()) {
            synchronized (engine) {




                    try {
                        engine.threadPool.add(Thread.currentThread());
                        engine.notifyAll();
                        engine.wait();
                        engine.threadPool.remove(Thread.currentThread());
                    } catch (InterruptedException e) {
                        engine.activeThreads--;
                        engine.threadPool.remove(Thread.currentThread());
                        engine.notifyAll();
                        //e.printStackTrace();
                        return;
                    }
                }
            }
                executeJob(engine.getSortJob());


        }


    }


void executeJob(SortJob job)
{
    if (job !=null)
    {
        engine.parallelQuicksort(job);
        synchronized (engine) {
            engine.notifyAll();
        }
    }
}


}
