package sortcars;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

public class QuicksortEngine {

    private LinkedList<Worker> threadPool;
    private LinkedList<SortJob> sortJobs;
    private int maxThreads;



    //list iterator must be pointing to the nodes that need to be swapped
    //then just take their values and swap them
    //Collections.swap is O(N) this is O(1)
    public static void swap(ListIterator<Car> node1, Car car1, ListIterator<Car> node2, Car car2) {
    node1.set(car2);
    node2.set(car1);
    }

    public static void quickSort(LinkedList<Car> list, int start, int end) {
        if (start < end) {

        int[] equalRegion = partition(start, end, list);
        quickSort(list, start, equalRegion[0] - 1);
        quickSort(list, equalRegion[1] + 1, end);


        }
    }

    public static int[] partition(int startIndex, int endIndex, LinkedList<Car> list)
    {
        int i = startIndex+1;


        //get the first element of the sublist
        ListIterator<Car> lessThanIterator = list.listIterator(startIndex);
        Car valueAtLess = lessThanIterator.next();

        //swap it with a randomized pivot
        int randomizedPivotIndex = new Random().nextInt(endIndex - startIndex + 1) + startIndex;
        //System.out.println("randomizedPivotIndex: " + randomizedPivotIndex);
        ListIterator<Car> randomizedPivotListIterator = list.listIterator(randomizedPivotIndex);
        Car pivot = randomizedPivotListIterator.next();

       // System.out.println("pivot before" + pivot);
        swap(lessThanIterator, valueAtLess, randomizedPivotListIterator, pivot);

        //reset the lessThanIterator because it is possible that it was the randomized choice
        //therefore the Car value would be wrong

        lessThanIterator = list.listIterator(startIndex);
        valueAtLess = lessThanIterator.next();

        ListIterator<Car> iterator = list.listIterator(i);
        Car valueAtI = iterator.next();
        ListIterator<Car> greaterThanIterator = list.listIterator(endIndex);
        Car valueAtGreater = greaterThanIterator.next();


        //System.out.println("pivot after" + pivot);

        while (i <= endIndex) {
            System.out.println("current" + valueAtI);
            System.out.println("less than" + valueAtLess);
            System.out.println("greater than" + valueAtGreater);
            int comparison = valueAtI.compareTo(pivot);
            if (comparison < 0) {
                swap(iterator, valueAtI, lessThanIterator, valueAtLess);
                valueAtLess = lessThanIterator.next();
                startIndex++;
                if (iterator.hasNext())
                    valueAtI = iterator.next();
                i++;

            } else if (comparison > 0) {
                swap(iterator, valueAtI, greaterThanIterator, valueAtGreater);
                valueAtGreater = greaterThanIterator.previous();
                endIndex--;
            } else {
                if (iterator.hasNext())
                    valueAtI = iterator.next();
                i++;
            }

        }
            int[] equalRegion = new int[2];
            equalRegion[0] = startIndex;
            equalRegion[1] = endIndex;


        return equalRegion;
    }



}
