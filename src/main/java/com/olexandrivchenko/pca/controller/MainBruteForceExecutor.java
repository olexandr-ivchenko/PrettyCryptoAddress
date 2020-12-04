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

    private static final BigInteger JOB_SIZE = BigInteger.valueOf(100000L);

    @Autowired
    private ResultProcessor resultProcessor;

    private BigInteger startPoint;
    private BigInteger currentPoint;
    private boolean finished;

    public void startThreads(BigInteger startPoint, int threadCount, List<AddressGenerator> addressGenerators, List<AddressChecker> addressCheckers){
        this.startPoint = startPoint;
        currentPoint = startPoint;
        ExecutorService service = Executors.newFixedThreadPool(threadCount);
        ThreadExecutor thread = new ThreadExecutor(addressGenerators, addressCheckers, getJobSupplier(), getResultConsumer());
        for (int i = 0; i < threadCount; i++) {
            service.execute(thread);
        }
    }

    private Supplier<Job> getJobSupplier(){
        return new Supplier<Job>() {
            @Override
            public Job get() {
                if(finished){
                    return null;
                }
                synchronized (this){
                    Job job = new Job(currentPoint, JOB_SIZE.longValue());
                    currentPoint = currentPoint.add(JOB_SIZE);
                    return job;
                }
            }
        };
    }

    private BiConsumer<BigInteger, AddressGenerator> getResultConsumer(){
        return new BiConsumer<BigInteger, AddressGenerator>() {
            @Override
            public void accept(BigInteger result, AddressGenerator generator) {
                finished = true;
                resultProcessor.processResult(result, generator);
            }
        };
    }


}
