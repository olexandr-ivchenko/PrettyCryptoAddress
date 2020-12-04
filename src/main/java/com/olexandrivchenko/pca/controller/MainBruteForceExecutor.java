package com.olexandrivchenko.pca.controller;

import com.olexandrivchenko.pca.addressgenerator.AddressGenerator;
import com.olexandrivchenko.pca.checker.AddressChecker;
import com.olexandrivchenko.pca.controller.dto.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Service
public class MainBruteForceExecutor {

    private final static Logger log = LoggerFactory.getLogger(MainBruteForceExecutor.class);

    private static final BigInteger JOB_SIZE = BigInteger.valueOf(100000L);

    @Autowired
    private ResultProcessor resultProcessor;

    private BigInteger startPoint;
    private BigInteger currentPoint;
    private boolean finished;

    private long executorStartTime;
    private long lastJobRequestTime;
    private Map<String, Double> threadPerformanceMap = new TreeMap<>();

    public void startThreads(BigInteger startPoint, int threadCount, List<AddressGenerator> addressGenerators, List<AddressChecker> addressCheckers) {
        this.startPoint = startPoint;
        currentPoint = startPoint;
        ExecutorService service = Executors.newFixedThreadPool(threadCount);
        ThreadExecutor thread = new ThreadExecutor(addressGenerators, addressCheckers, getJobSupplier(), getResultConsumer(), getPerformanceReportConsumer());
        executorStartTime = System.currentTimeMillis();
        for (int i = 0; i < threadCount; i++) {
            service.execute(thread);
        }
        while (!finished) {
            try {
                Thread.sleep(120000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("Total hashrate is: {}kH/s over {} threads", String.format("%.2f", currentPoint.subtract(startPoint).subtract(JOB_SIZE.multiply(BigInteger.valueOf(threadCount/2))).doubleValue()/(lastJobRequestTime-executorStartTime)), threadCount);
            String header = "";
            String values = "";
            for(Map.Entry<String, Double> entry : threadPerformanceMap.entrySet()) {
                header += "|" + String.format("%1$9s", entry.getKey().substring(7));
                values += "|" + String.format("%1$9s", String.format("%.2fkH/s", entry.getValue()/1000));
//                log.info("{} : \t{}kH/s", entry.getKey(), String.format("%.2f", entry.getValue()));
            }
            log.info(header);
            log.info(values);
        }
    }

    private Consumer<Double> getPerformanceReportConsumer() {
        return speed -> {
            threadPerformanceMap.put(Thread.currentThread().getName(), speed);
        };
    }

    private Supplier<Job> getJobSupplier() {
        return new Supplier<Job>() {
            @Override
            public Job get() {
                if (finished) {
                    return null;
                }
                lastJobRequestTime = System.currentTimeMillis();
                synchronized (this) {
                    Job job = new Job(currentPoint, JOB_SIZE.longValue());
                    currentPoint = currentPoint.add(JOB_SIZE);
                    return job;
                }
            }
        };
    }

    private BiConsumer<BigInteger, AddressGenerator> getResultConsumer() {
        return new BiConsumer<BigInteger, AddressGenerator>() {
            @Override
            public void accept(BigInteger result, AddressGenerator generator) {
                finished = true;
                resultProcessor.processResult(result, generator);
            }
        };
    }


}
