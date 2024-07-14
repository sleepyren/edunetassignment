package sortcarsv3;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class QuicksortEngine {
    LinkedList<SortJob> jobs = new LinkedList<SortJob>();
    List<Thread> threadPool = new LinkedList<Thread>();
    final int maxThreads;
    int activeThreads = 0;

    public QuicksortEngine(int maxThreads) {
        this.maxThreads = maxThreads;
    }


}
