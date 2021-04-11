package com.example.file.processing.demo;

import com.example.file.processing.demo.transformation.Invoice;
import com.example.file.processing.demo.transformation.csv.CsvToPersonTransformer;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import static java.util.stream.Collectors.groupingBy;
import static junit.framework.Assert.assertEquals;

/**
 * @author Petya Marinova
 */
public class CSVSplitterToSeparateCsvFiles {
    private CsvToPersonTransformer transformer;

    @Before
    public void setup() {
        transformer = new CsvToPersonTransformer();
    }

    @Test
    public void iterateCsvAndSplitToXmlFiles() throws Exception {
        List<List<String>> records = new ArrayList<List<String>>();
        try (CSVReader csvReader = new CSVReader(new FileReader(new ClassPathResource("files/input.csv").getFile()));) {
            String[] values = null;
            while ((values = csvReader.readNext()) != null) {
                records.add(Arrays.asList(values));
            }
        }
        List<Invoice> result = transformer.convertInvoice(records);
        assertEquals(result.size(), 11);

        Map<String, List<Invoice>> groupedInvoices = result.stream()
                .filter(Objects::nonNull)
                .filter(it -> it.getBuyerName() != null)
                .collect(groupingBy(Invoice::getBuyerName));

        groupedInvoices.keySet().stream().filter(key->!key.equalsIgnoreCase("\uFEFFbuyer")).forEach(key -> {

            InvoicesList invoicesList = new InvoicesList();
            List<Invoice> mappedInvoices = new ArrayList<>();
            for (List<Invoice> invoices : groupedInvoices.values()) {
                for (Invoice invoice : invoices) {
                    if (invoice.getBuyerName().equalsIgnoreCase(key)) {
                        mappedInvoices.add(invoice);
                    }
                }
            }
            invoicesList.setInvoices(mappedInvoices);
            try {
                Path pathToFile = Paths.get("C:/workspace/splittedCSV/" + key+ "CSVsplitRecord " +new Date().getTime() + ".csv");
                Files.createDirectories(pathToFile.getParent());
                Files.createFile(pathToFile);

                try (Writer writer = Files.newBufferedWriter(pathToFile)) {
                    StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer)
                            .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                            .build();
                    beanToCsv.write(invoicesList.getInvoices());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });



    }
}
