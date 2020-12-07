package com.olexandrivchenko.pca;

import com.olexandrivchenko.pca.addressgenerator.AddressGenerator;
import com.olexandrivchenko.pca.checker.AddressChecker;
import com.olexandrivchenko.pca.comandline.ArgumentsParser;
import com.olexandrivchenko.pca.comandline.Help;
import com.olexandrivchenko.pca.controller.RuntimeController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigInteger;
import java.util.List;

@SpringBootApplication
public class PrettyCryptoAddressApplication implements CommandLineRunner {

	@Autowired
	Help help;

	@Autowired
	RuntimeController runtimeController;

	@Autowired
	ArgumentsParser argParser;

	public static void main(String[] args) {
		SpringApplication.run(PrettyCryptoAddressApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//parse arguments
		argParser.parseArguments(args);
		//create beans, per parameters

		//launch calculation
		List<AddressChecker> addressCheckers = argParser.getEffectiveCheckers();
		AddressGenerator addressGenerators = argParser.getEffectiveGenerator();
		int threadCount = 16;
		BigInteger startPoint = argParser.getStartPoint();
		runtimeController.startThreads(startPoint, threadCount, addressGenerators, addressCheckers);
	}
}
