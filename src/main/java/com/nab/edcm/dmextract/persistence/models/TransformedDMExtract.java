package com.nab.edcm.dmextract.persistence.models;

import lombok.Data;
import org.springframework.core.io.Resource;

@Data
public class TransformedDMExtract {

    private Long id;
    private String metadataJson;
    private Resource resource;
}
