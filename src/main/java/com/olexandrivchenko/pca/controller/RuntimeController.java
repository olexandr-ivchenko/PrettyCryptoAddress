package com.olexandrivchenko.pca.controller;

import com.olexandrivchenko.pca.addressgenerator.AddressGenerator;
import com.olexandrivchenko.pca.checker.AddressChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

import static com.olexandrivchenko.pca.controller.MainBruteForceExecutor.JOB_SIZE;

@Component
public class RuntimeController {

    private final static Logger log = LoggerFactory.getLogger(RuntimeController.class);

    @Autowired
    private MainBruteForceExecutor executor;

    private Map<String, Double> threadPerformanceMap;

    private long executorStartTime;


    public void startThreads(BigInteger startPoint,
                             int threadCount,
                             AddressGenerator addressGenerators,
                             List<AddressChecker> addressCheckers) {
        executor.startThreads(startPoint, threadCount, addressGenerators, addressCheckers, getPerformanceReportConsumer());
        threadPerformanceMap = new TreeMap<>();
        doRuntimeControl();
    }

    public void doRuntimeControl() {
        executorStartTime = System.currentTimeMillis();

        while (!executor.isFinished()) {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            double hashRate = executor.getCurrentPoint()
                    .subtract(executor.getStartPoint())
                    .subtract(JOB_SIZE.multiply(BigInteger.valueOf(executor.getThreadCount() / 2))).doubleValue()
                    / (executor.getLastJobRequestTime() - executorStartTime);
            log.info("Total hashrate is: {}kH/s over {} threads",
                    String.format("%.2f", hashRate),
                    executor.getThreadCount());
            String header = "";
            String values = "";
            for (Map.Entry<String, Double> entry : threadPerformanceMap.entrySet()) {
                header += "|" + String.format("%1$9s", entry.getKey().substring(7));
                values += "|" + String.format("%1$9s", String.format("%.2fkH/s", entry.getValue() / 1000));
            }
            threadPerformanceMap = new TreeMap<>();
            log.info(header);
            log.info(values);
        }
    }

    public Consumer<Double> getPerformanceReportConsumer() {
        return speed -> {
            threadPerformanceMap.put(Thread.currentThread().getName(), speed);
        };
    }

}
