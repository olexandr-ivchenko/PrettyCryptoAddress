package com.olexandrivchenko.pca.checker;

import java.util.List;

public interface AddressChecker {

    /**
     *
     * @return a new instance of this checker with given parameters
     * @param args
     */
    AddressChecker getInstance(List<String> args);

    /**
     * Does actual check of given address against configured rules
     * @param address
     * @return
     */
    boolean checkAddress(String address);

    /**
     * This will be used to show available command line options
     * @return a command line key to enable this checker
     */
    String getCommandKey();

    /**
     * This will be used in help screen to let user know, what exactly this class checks
     * Put a meaningful description here
     * @return
     */
    String getDescription();

}
