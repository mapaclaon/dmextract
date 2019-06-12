package com.nab.edcm.dmextract.batch;

import com.nab.edcm.dmextract.persistence.models.DMExtract;
import com.nab.edcm.dmextract.persistence.models.TransformedDMExtract;
import com.nab.edcm.dmextract.persistence.repo.DMExtractRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.springframework.core.io.ResourceLoader.CLASSPATH_URL_PREFIX;

@Component
@Slf4j
public class InitProcessor implements ItemProcessor<DMExtract, DMExtract> {

    @Autowired
    private DMExtractRepository fileRepo;

    @Override
    public DMExtract process(DMExtract user) throws Exception {
        Optional<DMExtract> userFromDb = fileRepo.findById(user.getId());
        if(userFromDb.isPresent()) {
            log.info("Data is in");
        }
        return user;
    }

}
