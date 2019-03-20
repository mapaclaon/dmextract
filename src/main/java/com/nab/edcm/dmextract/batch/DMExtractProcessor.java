package com.nab.edcm.dmextract.batch;

import com.nab.edcm.dmextract.persistence.models.DMExtract;
import com.nab.edcm.dmextract.persistence.repo.DMExtractRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class DMExtractProcessor implements ItemProcessor<DMExtract, DMExtract> {

    @Autowired
    private DMExtractRepository fileRepo;

    @Override
    public DMExtract process(DMExtract user) throws Exception {
        Optional<DMExtract> userFromDb = fileRepo.findById(user.getId());
        if(userFromDb.isPresent()) {
            log.info("dm extracted with data");
            user.setHouseName(userFromDb.get().getHouseName().toUpperCase());
        }
        return user;
    }

}
