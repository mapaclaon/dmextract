package com.nab.edcm.dmextract;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class DmextractApplication {

	public static void main(String[] args) {
		SpringApplication.run(DmextractApplication.class, args);
	}

}
