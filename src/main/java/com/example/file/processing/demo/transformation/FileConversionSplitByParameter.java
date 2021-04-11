package com.example.file.processing.demo.transformation;

import com.example.file.processing.demo.InvoicesList;
import com.example.file.processing.demo.transformation.csv.CsvToPojoTransformer;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import java.io.File;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.springframework.stereotype.Component;
import static java.util.stream.Collectors.groupingBy;

/**
 * @author Petya Marinova
 */
@Component
public class FileConversionSplitByParameter {
    private CsvToPojoTransformer transformer;

    public FileConversionSplitByParameter(CsvToPojoTransformer transformer) {
        this.transformer = transformer;
    }

    public void generateCSVAndSplitBuyerName(List<Invoice> result) {
        Map<String, List<Invoice>> groupedInvoices = result.stream()
                .filter(Objects::nonNull)
                .filter(it -> it.getBuyerName() != null)
                .collect(groupingBy(Invoice::getBuyerName));

        groupedInvoices.keySet().stream().filter(key -> !key.equalsIgnoreCase("\uFEFFbuyer")).forEach(key -> {

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

                Path pathToFile = Paths.get("C:/workspace/splittedInvoiceBuyer/" + key + "-XMLRecord-" + new Date().getTime() + ".xml");
                Files.createDirectories(pathToFile.getParent());
                Files.createFile(pathToFile);

                File file = new File(pathToFile.toFile().getPath());
                jaxbMarshaller.marshal(invoicesList, file);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }
    public void generateXMLAndSplitBuyerName(List<Invoice> result) {
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
                Path pathToFile = Paths.get("C:/workspace/splittedInvoiceBuyer/" + key+ "-CSVRecord- " +new Date().getTime() + ".csv");
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
