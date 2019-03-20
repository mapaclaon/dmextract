package com.nab.edcm.dmextract.batch;

import com.nab.edcm.dmextract.persistence.models.DMExtract;
import com.nab.edcm.dmextract.persistence.repo.DMExtractRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Slf4j
public class DMExtractWriter implements ItemWriter<DMExtract>{

    @Autowired
    private DMExtractRepository repo;

    @Override
    @Transactional
    public void write(List<? extends DMExtract> extractFiles) throws Exception {
        log.info("Saving to database [{}]", extractFiles.get(0).toString());
        repo.saveAll(extractFiles);
    }

}
