# Sorting Cars
### Overview

This project implements a multithreaded Java program to sort Car objects using the Quicksort algorithm, utilizing a thread pool for efficient concurrent sorting.
Car Class (Car.java)

### Attributes:

    REC_ID (long): Sequential production number.
    Serial (String): Unique 12-character alphanumeric string.
    Color (enum): RED, BLUE, BLACK, WHITE.
    Destination (String): Los Angeles, Houston, New Orleans, Miami, New York.

### Implements Comparable for sorting.
Sort Criteria

    Destination: Los Angeles, Houston, New Orleans, Miami, New York.
    Color: RED, BLUE, BLACK, WHITE.
    Serial: Alphabetical order.

### Program Parameters

    Number of lists
    Number of cars in each list
    maxThreads

## Functionality

Create Lists: Generate car lists and save to files (cars-[NUMBER].txt).
Initialize Quicksort Engine:

Maintain a thread pool, initially empty.
Assign sort jobs to threads in a FIFO manner without exceeding maxThreads.
Sort Lists:
Sort each list concurrently using the thread pool.
Save sorted lists to files (cars-[NUMBER]-sorted.txt).
Print sorting time for each list.
Terminate Engine: Wait for all jobs to finish and terminate threads.

### Example

To create 3 lists of 100,000 cars each and sort using up to 10 threads:

```
java QuicksortEngine listCount carCount maxThreads
```