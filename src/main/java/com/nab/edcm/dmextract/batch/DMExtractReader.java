package com.nab.edcm.dmextract.batch;

import com.nab.edcm.dmextract.persistence.models.DMExtract;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.Resource;

public class DMExtractReader extends FlatFileItemReader<DMExtract> {

    public DMExtractReader(Resource resource) {

        super();

        setResource(resource);

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames(new String[] { "id", "fullname", "houseName", "tagLine", "status" });
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);

        BeanWrapperFieldSetMapper<DMExtract> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(DMExtract.class);

        DefaultLineMapper<DMExtract> defaultLineMapper = new DefaultLineMapper<>();
        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);
        setLineMapper(defaultLineMapper);
    }
}