package com.olexandrivchenko.pca.addressgenerator;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Utils;
import org.bitcoinj.params.MainNetParams;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

/**
 * Please note - due to nature of address generation, 1st char is not linearly distributed
 * [2-9][A-P] - 4.4% each
 * [Q] - 1.5%
 * [1] - 0.4%
 * [R-Za-z] - 0.07% each
 * There is also a small difference in 2nd char probability, but that is inside of 0.1%
 * [2-9A-K] - 1.8%
 * others - 1.7%
 */
@Component
public class Bitj_P2PKH_Compressed_AddressGenerator implements AddressGenerator {

    @Override
    public String getAddress(BigInteger privateKey) {
        ECKey key = ECKey.fromPrivate(privateKey, true);
        Address address = new Address(MainNetParams.get(), Utils.sha256hash160(key.getPubKey()));
        return address.toBase58();

    }

    @Override
    public String getCommandKey() {
        return "BTC_P2PKH_compressed";
    }

    @Override
    public String getDescription() {
        return "This is basic implementation for Pay To Public Key Hash address. Public key is compressed";
    }

    @Override
    public String getCalculationAddress(BigInteger privateKey) {
        //TODO needs check
        return getAddress(privateKey).substring(1);
    }

    @Override
    public String getPrivateKey(BigInteger privateKey) {
        ECKey key = ECKey.fromPrivate(privateKey, true);
        return key.getPrivateKeyAsWiF(MainNetParams.get());
    }
}
