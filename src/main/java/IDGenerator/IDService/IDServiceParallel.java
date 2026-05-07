package IDGenerator.IDService;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class IDServiceParallel implements IDServiceInterface{
    private final long upperLimit;
    
    private final AtomicLong nextCandidate;
    private final BlockingQueue<Long> primeIdQueue;
    private final AtomicInteger activeWorkers;

    public IDServiceParallel(long lowerLimit, long upperLimit, int threadCount){
        if(upperLimit < lowerLimit){
            throw IDServiceException.lowerLimitHigherThanUpperLimit();
        }
        this.upperLimit = upperLimit;
        
        this.primeIdQueue = new LinkedBlockingQueue<>();
        this.nextCandidate = new AtomicLong(lowerLimit + 1);
        this.activeWorkers = new AtomicInteger(threadCount);

        for (int i = 0; i < threadCount; i++) {
            Thread workerThread = new Thread(
                new PrimeIdProducer(),
                "prime-id-producer-" + i
            );
            workerThread.setDaemon(true);
            workerThread.start();
        }
    }

    private boolean isPrime(long number) {
        if (number <= 1) return false;
        if (number == 2) return true;
        if (number % 2 == 0) return false;
    
        long limit = (long) Math.sqrt(number);
        for (long i = 3; i <= limit; i += 2) {
            if (number % i == 0) return false;
        }
    
        return true;
    }

    private class PrimeIdProducer implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    long counter = nextCandidate.getAndIncrement();
                    if (counter > upperLimit) {
                        return;
                    }
                    if (isPrime(counter)) {
                        primeIdQueue.put(counter);
                    }
                }
            } catch (InterruptedException interruptedException) {
                Thread.currentThread().interrupt();
            } finally {
                activeWorkers.decrementAndGet();
            }
        } 
    }

    @Override
    public long getUnusedId() {
        try {
            while (true) {
                Long id = primeIdQueue.poll(50, TimeUnit.MILLISECONDS);
                if (id != null) {
                    return id;
                }
                if (activeWorkers.get() == 0 && primeIdQueue.isEmpty()) {
                    return -1;
                }
            }
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
            return -1;
        }
    }
}
