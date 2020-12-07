package com.olexandrivchenko.pca.addressgenerator;

import java.math.BigInteger;

public class Bitj_P2SH_AddressGenerator implements AddressGenerator{
    @Override
    public String getCommandKey() {
        return "BTC_P2SH";
    }

    @Override
    public String getDescription() {
        return "This is basic implementation for \"Pay To Script Hash\" address. Those addresses are starting from 3";
    }

    @Override
    public String getAddress(BigInteger privateKey) {
        return null;
    }

    @Override
    public String getCalculationAddress(BigInteger privateKey) {
        return null;
    }

    @Override
    public String getPrivateKey(BigInteger privateKey) {
        return null;
    }
}
