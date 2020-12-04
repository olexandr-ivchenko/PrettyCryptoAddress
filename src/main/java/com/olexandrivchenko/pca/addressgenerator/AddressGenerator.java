package com.olexandrivchenko.pca.addressgenerator;

import java.math.BigInteger;

public interface AddressGenerator {
    /**
     * This will be used to show available command line options
     * @return a command line key to enable this generator
     */
    String getCommandKey();

    /**
     * This will be used in help screen to let user know, what exactly this class generates
     * Put a meaningful description here
     * @return
     */
    String getDescription();

    /**
     * Generates full user-readable address, including address prefix
     * @param privateKey
     * @return
     */
    String getAddress(BigInteger privateKey);

    /**
     * Generates address for comparison. E.g. no prefixes and other "fixed" part of address
     * @param privateKey
     * @return
     */
    String getCalculationAddress(BigInteger privateKey);

    /**
     * Return WIF private key that will be shown to user
     * @param privateKey
     * @return
     */
    String getPrivateKey(BigInteger privateKey);
}
