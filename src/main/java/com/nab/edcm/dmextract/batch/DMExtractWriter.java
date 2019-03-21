package com.nab.edcm.dmextract.batch;

import com.nab.edcm.dmextract.persistence.models.DMExtract;
import com.nab.edcm.dmextract.persistence.repo.DMExtractRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class DMExtractWriter implements ItemWriter<DMExtract>{

    @Autowired
    private DMExtractRepository repo;

    @Override
    @Transactional
    public void write(List<? extends DMExtract> extractFiles) throws Exception {

        log.info("Extract files size [{}]", extractFiles.size());
        extractFiles.stream().forEach(file -> {
            Optional<DMExtract> dm = repo.findById(((DMExtract) file).getId());
            if (dm.isPresent()) {
                DMExtract process = dm.get();
                process.setStatus("done");
                repo.save(process);
                log.info("Saving to database [{} - {}]", process.getId(), process.getFullname());
              }
            else {
                repo.save(file);
            }

        });
    }

}
