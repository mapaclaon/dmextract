package com.nab.edcm.dmextract;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@EnableBatchProcessing
@Slf4j
public class DmextractApplication {

	public static void main(String[] args) {
		SpringApplication.run(DmextractApplication.class, args);

		List myList = new ArrayList();
		Map<String, Object> map = new HashMap();
		map.put("id", 1);
		map.put("name", "mary");
		myList.add(map);
		map = new HashMap<>();
		map.put("id", 2);
		map.put("name", "ann");
		myList.add(map);

		myList.stream().forEach(item -> log.info("each stream -> id = [{}], name is [{}]",
				((Map<String, Object>) item).get("id"), ((Map<String, Object>) item).get("name")));

	}

}
