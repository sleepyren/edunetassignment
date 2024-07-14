package sortcarsv3;

import sortcarsv3.QuicksortEngine;

public class Worker implements Runnable{

    QuicksortEngine engine;
    SortJob currentJob = null;
    public Worker(QuicksortEngine engine)
    {
        this.engine = engine;
    }



    @Override
    public void run() {

        while (true)
        {

            while (engine.jobs.isEmpty()) {
                synchronized (engine) {




                    try {
                        engine.threadPool.add(Thread.currentThread());
                        engine.notifyAll();
                        engine.wait();
                        engine.threadPool.remove(Thread.currentThread());
                    } catch (InterruptedException e) {
                        threadInterrupted();
                        return;
                    }
                }
            }


            synchronized (engine)
            {
                currentJob = engine.jobs.pollFirst();
            }

            if (currentJob != null) {
                currentJob.executeJob();
            }


        }

    }



    private void threadInterrupted(){
        //System.err.println("Thread interrupted");
        engine.activeThreads--;
        engine.threadPool.remove(Thread.currentThread());
        engine.notifyAll();
    }
}
