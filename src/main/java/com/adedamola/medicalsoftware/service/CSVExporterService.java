package com.adedamola.medicalsoftware.service;

import com.adedamola.medicalsoftware.model.Patient;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Service
public class CSVExporterService {

    public InputStreamResource getFileInputStream(Patient patient) {
        String[] csvHeader = {
                "id", "name", "age", "last_visit_date"
        };

        List<List<String>> csvBody = new ArrayList<>();
        csvBody.add(Arrays.asList(String.valueOf(patient.getId()), String.valueOf(patient.getName()),
                String.valueOf(patient.getAge()), String.valueOf(patient.getLastVisitDate())));

        ByteArrayInputStream byteArrayOutputStream;

        try (
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                // defining the CSV printer
                CSVPrinter csvPrinter = new CSVPrinter(
                        new PrintWriter(out),
                        // withHeader is optional
                        CSVFormat.DEFAULT.withHeader(csvHeader)
                );
        ) {
            // populating the CSV content
            for (List<String> record : csvBody)
                csvPrinter.printRecord(record);

            // writing the underlying stream
            csvPrinter.flush();

            byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        InputStreamResource fileInputStream = new InputStreamResource(byteArrayOutputStream);
        return fileInputStream;

    }
}
