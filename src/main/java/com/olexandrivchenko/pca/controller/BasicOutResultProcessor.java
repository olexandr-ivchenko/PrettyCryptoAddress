package com.olexandrivchenko.pca.controller;

import com.olexandrivchenko.pca.addressgenerator.AddressGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class BasicOutResultProcessor implements ResultProcessor {

    private final static Logger log = LoggerFactory.getLogger(BasicOutResultProcessor.class);

    @Override
    public void processResult(BigInteger result, AddressGenerator generator) {
        log.info("************************Result found************************");
        log.info("Integer value: {}", result.toString());
        log.info("PrivateKey: {}", generator.getPrivateKey(result));
        log.info("PublicKey: TODO");
        log.info("Address: {}", generator.getAddress(result));
        log.info("************************************************************");
    }
}
