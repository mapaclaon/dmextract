package com.nab.edcm.dmextract.batch;

import com.nab.edcm.dmextract.persistence.models.DMExtract;
import com.nab.edcm.dmextract.persistence.models.TransformedDMExtract;
import com.nab.edcm.dmextract.persistence.repo.DMExtractRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
@Slf4j
public class InitWriter implements ItemWriter<DMExtract> {

    @Autowired
    private DMExtractRepository repo;

    @Override
    @Transactional
    public void write(List<? extends DMExtract> extractFiles) throws Exception {

        log.info("Extract files size [{}]", extractFiles.size());
        extractFiles.stream().forEach(file -> {
            Optional<DMExtract> dm = repo.findById(file.getId());
            if (dm.isPresent()) {
                DMExtract process = dm.get();
                process.setStatus("done");
                repo.save(process);
              }
            else {
                repo.saveAll(extractFiles);
            }
        });
    }

}
