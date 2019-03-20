package com.nab.edcm.dmextract.persistence.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "file_inventory")
public class DMExtract {

    @Id
    private Long id;
    private String fullname;
    private String houseName;
    private String tagLine;
}
