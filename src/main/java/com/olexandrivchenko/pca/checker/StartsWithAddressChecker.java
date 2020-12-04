package com.olexandrivchenko.pca.checker;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StartsWithAddressChecker implements AddressChecker{

    private String startString;

    public AddressChecker getInstance(List<String> args) {
        if(args.size() != 1){
            throw new RuntimeException("StartsWithAddressChecker accepts one and only one parameter");
        } else if(args.get(0).length() < 1 || args.get(0).length() > 22){
            throw  new RuntimeException("StartsWithAddressChecker parameter should be 1-22 chars long");
        }
        StartsWithAddressChecker checker = new StartsWithAddressChecker();
        checker.startString = args.get(0);
        return checker;
    }

    @Override
    public boolean checkAddress(String address) {
        return address.startsWith(startString);
    }

    @Override
    public String getCommandKey() {
        return "startsWith";
    }

    @Override
    public String getDescription() {
        return "Check if address starts from specified characters";
    }
}
