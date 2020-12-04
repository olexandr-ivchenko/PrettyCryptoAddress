package com.olexandrivchenko.pca.addressgenerator;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Utils;
import org.bitcoinj.params.MainNetParams;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

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
        //TODO: just a stub - rewrite
        return privateKey.toString();
    }
}
