package com.nab.edcm.dmextract.batch;

import com.nab.edcm.dmextract.persistence.models.DMExtract;
import com.nab.edcm.dmextract.persistence.models.TransformedDMExtract;
import com.nab.edcm.dmextract.persistence.repo.DMExtractRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.springframework.core.io.ResourceLoader.CLASSPATH_URL_PREFIX;

@Component
@Slf4j
public class DMExtractProcessor implements ItemProcessor<DMExtract, TransformedDMExtract> {

    @Autowired
    private DMExtractRepository fileRepo;

    @Autowired
    private ResourceLoader resourceLoader;

    @Override
    public TransformedDMExtract process(DMExtract user) throws Exception {
        Optional<DMExtract> userFromDb = fileRepo.findById(user.getId());
        TransformedDMExtract filed = null;
        if(userFromDb.isPresent()) {
            log.info("dm extracted with data");
            filed = new TransformedDMExtract();
            filed.setId(userFromDb.get().getId());
            filed.setMetadataJson("{" + userFromDb.get().getHouseName() + "}");
            //get a resource from here
            filed.setResource(resourceLoader.getResource(CLASSPATH_URL_PREFIX
                    + "user-data.csv"));
        }
        return filed;
    }

}
