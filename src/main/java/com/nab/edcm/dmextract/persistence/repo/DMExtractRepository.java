package com.nab.edcm.dmextract.persistence.repo;

import com.nab.edcm.dmextract.persistence.models.DMExtract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DMExtractRepository extends JpaRepository<DMExtract, Long> {
}
