package com.example.file.processing.demo;

import com.example.file.processing.demo.transformation.Invoice;
import com.example.file.processing.demo.transformation.csv.CsvToPersonTransformer;
import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import static java.util.stream.Collectors.groupingBy;
import static junit.framework.Assert.assertEquals;

/**
 * @author Petya Marinova
 */
public class CSVSplitterToXML {
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
        //input lines in foreach for separate files?
        List<Invoice> result = transformer.convertInvoice(records);
        Map<String, List<Invoice>> groupedInvoices = result.stream()
                .filter(Objects::nonNull)
                .filter(it -> it.getBuyerName() != null)
                .collect(groupingBy(Invoice::getBuyerName));


        assertEquals(result.size(), 11);

        groupedInvoices.keySet().stream().filter(key->!key.equalsIgnoreCase("\uFEFFbuyer")).forEach(key -> {

                InvoicesList invoicesList = new InvoicesList();
                List<Invoice> mappedInvoices = new ArrayList<>();
                for (List<Invoice> invoices : groupedInvoices.values()) {
                    for (Invoice invoice : invoices) {
                        if (invoice.getBuyerName().equalsIgnoreCase(key)) {
                            invoice.setInvoiceImage(null);
                            mappedInvoices.add(invoice);
                        }
                    }
                }
                invoicesList.setInvoices(mappedInvoices);
                try {
                    JAXBContext jaxbContext = JAXBContext.newInstance(InvoicesList.class);
                    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

                    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

                    Path pathToFile = Paths.get("C:/workspace/splittedCSV/" + key + "XMLsplitRecord " + new Date().getTime() + ".xml");
                    Files.createDirectories(pathToFile.getParent());
                    Files.createFile(pathToFile);

                    File file = new File(pathToFile.toFile().getPath());
                    jaxbMarshaller.marshal(invoicesList, file);
                } catch (Exception e) {
                    e.printStackTrace();
                }

        });




    }
}


