package com.olexandrivchenko.pca.comandline;

import com.olexandrivchenko.pca.addressgenerator.AddressGenerator;
import com.olexandrivchenko.pca.checker.AddressChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Help {

    private final static Logger log = LoggerFactory.getLogger(Help.class);

    @Autowired
    private List<? extends AddressGenerator> generators;

    @Autowired
    private List<? extends AddressChecker> checkers;

    public void showHelp(){
        log.info("PrettyCryptoAddress help");
        log.info("Available generators: ");
        for(AddressGenerator generator : generators) {
            log.info("{}\t\t{}", generator.getCommandKey(), generator.getDescription());
        }
        log.info("Available checkers: ");
        for(AddressChecker checker : checkers){
            log.info("{}\t\t{}", checker.getCommandKey(), checker.getDescription());
        }
    }
}
