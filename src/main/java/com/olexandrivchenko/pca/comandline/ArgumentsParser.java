package com.olexandrivchenko.pca.comandline;

import com.olexandrivchenko.pca.addressgenerator.AddressGenerator;
import com.olexandrivchenko.pca.checker.AddressChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArgumentsParser {

    @Autowired
    private Help help;

    @Autowired
    private List<? extends AddressGenerator> fullGeneratorList;

    @Autowired
    private List<? extends AddressChecker> fullCheckerList;

    private List<AddressGenerator> effectiveGenerators = new ArrayList<>();
    private List<AddressChecker> effectiveCheckers = new ArrayList<>();

    private String startPoint;

    /**
     * @param args
     * @return true if parsing is successful and have enough arguments to start calculation
     */
    public boolean parseArguments(String[] args) {
        if (needsHelp(args)) {
            help.showHelp();
        }
        List<List<String>> groupedParams = groupParams(args);
        for (List<String> arg : groupedParams) {
            if(checkStartPoint(arg)){
                continue;
            }
            AddressGenerator generatorForParameter = getGeneratorForParameter(arg);
            if (generatorForParameter != null) {
                effectiveGenerators.add(generatorForParameter);
                continue;
            }
            AddressChecker checkerForParameter = getCheckerForParameter(arg);
            if (checkerForParameter != null) {
                effectiveCheckers.add(checkerForParameter);
                continue;
            }
            //throw something
        }
        return true;
    }

    /**
     *
     * @param arg
     * @return true, if start point arguments found and valid
     */
    private boolean checkStartPoint(List<String> arg) {
        if (arg.get(0).equalsIgnoreCase("--startPoint")) {
            if (arg.size() != 2) {
                //throw something
            } else if (startPoint != null) {
                //start point is set - throw something
            } else {
                startPoint = arg.get(1);
                return true;
            }
        }
        return false;
    }

    private List<List<String>> groupParams(String[] args) {
        List<List<String>> result = new ArrayList<>();
        List<String> paramsSet = new ArrayList<>();
        for (String arg : args) {
            if (arg.startsWith("--")) {
                result.add(paramsSet);
                paramsSet = new ArrayList<>();
            }
            paramsSet.add(arg);
        }
        result.remove(0); //first one is always empty
        result.add(paramsSet);
        return result;
    }

    private AddressChecker getCheckerForParameter(List<String> arg) {
        String key = arg.get(0).substring(2);
        for (AddressChecker checker : fullCheckerList) {
            if (checker.getCommandKey().equalsIgnoreCase(key)) {
                AddressChecker newChecker = checker.getInstance(arg.subList(1, arg.size()));
                return newChecker;
            }
        }
        return null;
    }

    private AddressGenerator getGeneratorForParameter(List<String> arg) {
        String key = arg.get(0).substring(2);
        for (AddressGenerator generator : fullGeneratorList) {
            if (generator.getCommandKey().equalsIgnoreCase(key)) {
                if(arg.size() != 1){
                    //throw something
                    return null;
                }
                return generator;
            }
        }
        return null;
    }

    private boolean needsHelp(String[] args) {
        if (args.length == 0) {
            return true;
        }
        for (String arg : args) {
            if ("--help".equalsIgnoreCase(arg)
                    || "-h".equalsIgnoreCase(arg)) {
                return true;
            }
        }
        return false;
    }

    public List<AddressGenerator> getEffectiveGenerators() {
        return effectiveGenerators;
    }

    public List<AddressChecker> getEffectiveCheckers() {
        return effectiveCheckers;
    }

    public String getStartPoint() {
        return startPoint;
    }
}
