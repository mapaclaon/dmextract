package com.nab.edcm.dmextract.batch;

import com.nab.edcm.dmextract.persistence.models.DMExtract;
import com.nab.edcm.dmextract.persistence.models.TransformedDMExtract;
import com.nab.edcm.dmextract.persistence.repo.DMExtractRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
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
public class DMExtractWriter implements ItemWriter<TransformedDMExtract> {

    @Autowired
    private DMExtractRepository repo;

    @Override
    @Transactional
    public void write(List<? extends TransformedDMExtract> extractFiles) throws Exception {

        log.info("Extract files size [{}]", extractFiles.size());
        extractFiles.stream().forEach(file -> {
            Optional<DMExtract> dm = repo.findById(((TransformedDMExtract) file).getId());
            if (dm.isPresent()) {
                DMExtract process = dm.get();
                process.setStatus("done");
                repo.save(process);
                log.info("Saving to database [{} - {}]", process.getId(), process.getFullname());
                saveToFiles(file);
              }
            else {
                repo.save(dm.get());
            }
        });
    }

    private void saveToFiles(TransformedDMExtract file) {
        Path filename1 = Paths.get("d://sample.csv");
        Path filename2 = Paths.get("d://sample.json");
        try (InputStream inputStream = file.getResource().getInputStream()){
            Files.copy(inputStream, filename1, StandardCopyOption.REPLACE_EXISTING);
            Files.write(filename2, ((TransformedDMExtract) file).getMetadataJson().getBytes());
            //do the checksum, encrypt
            //zip them
            zipFiles(file);
        }catch (IOException ioex) {
            log.error("something went wrong... [{}]", ioex.getMessage());
        }
    }

    private void zipFiles(TransformedDMExtract file) throws IOException {
        List<String> srcFiles = Arrays.asList("d://sample.json", "d://sample.csv");
        FileOutputStream fos = new FileOutputStream("d://" + file.getId() + ".zip");
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        for (String srcFile : srcFiles) {
            File fileToZip = new File(srcFile);
            FileInputStream fis = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            fis.close();
        }
        zipOut.close();
        fos.close();
    }

}
