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

    private AddressGenerator addressGenerator;
    private List<AddressChecker> addressCheckers;
    private AddressChecker mainAddressChecker;
    private Supplier<Job> jobSupplier;
    private BiConsumer<BigInteger, AddressGenerator> resultConsumer;
    private Consumer<Double> performanceReportConsumer;

    public ThreadExecutor(AddressGenerator addressGenerator,
                          List<AddressChecker> addressCheckers,
                          Supplier<Job> jobSupplier,
                          BiConsumer<BigInteger, AddressGenerator> resultConsumer,
                          Consumer<Double> performanceReportConsumer) {
        this.addressGenerator = addressGenerator;
        this.addressCheckers = addressCheckers;
        this.jobSupplier = jobSupplier;
        this.resultConsumer = resultConsumer;
        this.performanceReportConsumer = performanceReportConsumer;
        this.mainAddressChecker = getFastestChecker(addressCheckers);
    }

    private AddressChecker getFastestChecker(List<AddressChecker> addressCheckers) {
        //TODO implement me
        return addressCheckers.get(0);
    }

    @Override
    public void run() {
        Job job = null;
        while ((job = jobSupplier.get()) != null) {
            long jobStart = System.currentTimeMillis();
            for (long i = 0; i < job.getSize(); i++) {
                BigInteger privateKey = job.getStartPoint().add(BigInteger.valueOf(i));
                generateAddressesAndCheckMatch(privateKey);
            }
            double speed = job.getSize() * 1000d / (System.currentTimeMillis() - jobStart);
            performanceReportConsumer.accept(speed);
        }
    }

    private void generateAddressesAndCheckMatch(BigInteger privateKey) {
        String calcAddress = addressGenerator.getCalculationAddress(privateKey);
        if (hasMatch(calcAddress)) {
            resultConsumer.accept(privateKey, addressGenerator);
        }
    }

    private boolean hasMatch(String calcAddress) {
        //just a small optimization - in most cases we need to execute only one checker
        if (!mainAddressChecker.checkAddress(calcAddress)) {
            return false;
        }
        for (AddressChecker checker : addressCheckers) {
            if (!checker.checkAddress(calcAddress)) {
                return false;
            }
        }
        return true;
    }
}
