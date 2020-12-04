package com.olexandrivchenko.pca.controller;

import com.olexandrivchenko.pca.addressgenerator.AddressGenerator;

import java.math.BigInteger;

public interface ResultProcessor {
    void processResult(BigInteger result, AddressGenerator generator);
}
