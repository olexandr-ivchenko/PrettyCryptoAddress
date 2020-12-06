package com.olexandrivchenko.pca.controller;

import com.olexandrivchenko.pca.addressgenerator.AddressGenerator;
import com.olexandrivchenko.pca.checker.AddressChecker;
import com.olexandrivchenko.pca.controller.dto.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Service
public class MainBruteForceExecutor {

    public static final BigInteger JOB_SIZE = BigInteger.valueOf(100000L);

    @Autowired
    private ResultProcessor resultProcessor;

    private BigInteger startPoint;
    private BigInteger currentPoint;
    private boolean finished;
    private int threadCount;

    private long lastJobRequestTime;

    public void startThreads(BigInteger startPoint,
                             int threadCount,
                             AddressGenerator addressGenerators,
                             List<AddressChecker> addressCheckers,
                             Consumer<Double> performanceReportConsumer) {
        this.startPoint = startPoint;
        this.currentPoint = startPoint;
        this.threadCount = threadCount;
        ExecutorService service = Executors.newFixedThreadPool(threadCount);
        ThreadExecutor thread = new ThreadExecutor(addressGenerators,
                addressCheckers,
                getJobSupplier(),
                getResultConsumer(),
                performanceReportConsumer);
        for (int i = 0; i < threadCount; i++) {
            service.execute(thread);
        }
    }

    private Supplier<Job> getJobSupplier() {
        return () -> {
            if (finished) {
                return null;
            }
            lastJobRequestTime = System.currentTimeMillis();
            synchronized (this) {
                Job job = new Job(currentPoint, JOB_SIZE.longValue());
                currentPoint = currentPoint.add(JOB_SIZE);
                return job;
            }
        };
    }

    private BiConsumer<BigInteger, AddressGenerator> getResultConsumer() {
        return (result, generator) -> {
            finished = true;
            resultProcessor.processResult(result, generator);
        };
    }

    public boolean isFinished() {
        return finished;
    }

    public BigInteger getStartPoint() {
        return startPoint;
    }

    public BigInteger getCurrentPoint() {
        return currentPoint;
    }

    public long getLastJobRequestTime() {
        return lastJobRequestTime;
    }

    public int getThreadCount() {
        return threadCount;
    }
}
