package com.olexandrivchenko.pca.controller;

import com.olexandrivchenko.pca.addressgenerator.AddressGenerator;
import com.olexandrivchenko.pca.checker.AddressChecker;
import com.olexandrivchenko.pca.controller.dto.Job;

import java.math.BigInteger;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Represents worker thread code
 * Written in the night - spagetti code - please rewrite
 */
public class ThreadExecutor implements Runnable {

    private List<AddressGenerator> addressGenerators;
    private List<AddressChecker> addressCheckers;
    private Supplier<Job> jobSupplier;
    private BiConsumer<BigInteger, AddressGenerator> resultConsumer;

    public ThreadExecutor(List<AddressGenerator> addressGenerators,
                          List<AddressChecker> addressCheckers,
                          Supplier<Job> jobSupplier,
                          BiConsumer<BigInteger, AddressGenerator> resultConsumer) {
        this.addressGenerators = addressGenerators;
        this.addressCheckers = addressCheckers;
        this.jobSupplier = jobSupplier;
        this.resultConsumer = resultConsumer;
    }

    @Override
    public void run() {
        Job job = null;
        while((job = jobSupplier.get()) != null){
            for(long i=0; i<job.getSize(); i++) {
                BigInteger privateKey = job.getStartPoint().add(BigInteger.valueOf(i));
                generateAddressesAndCheckMatch(privateKey);
            }
        }
    }

    private void generateAddressesAndCheckMatch(BigInteger privateKey) {
        for (AddressGenerator generator : addressGenerators) {
            String calcAddress = generator.getCalculationAddress(privateKey);
            if(hasMatch(calcAddress)){
                resultConsumer.accept(privateKey, generator);
            }
        }
    }

    private boolean hasMatch(String calcAddress) {
        for(AddressChecker checker : addressCheckers){
            if(!checker.checkAddress(calcAddress)){
                return false;
            }
        }
        return true;
    }
}
