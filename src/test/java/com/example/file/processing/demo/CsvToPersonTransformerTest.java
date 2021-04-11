package com.example.file.processing.demo;

import com.example.file.processing.demo.transformation.Invoice;
import com.example.file.processing.demo.transformation.Person;
import com.example.file.processing.demo.transformation.csv.CsvToPersonTransformer;
import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import static com.example.file.processing.demo.CsvToXmlTransformerTest.compressByteArrayEncodeToString;
import static junit.framework.Assert.assertEquals;

public class CsvToPersonTransformerTest {
    private CsvToPersonTransformer transformer;

    @Before
    public void setup(){
        transformer = new CsvToPersonTransformer();
    }

    @Test
    public void convert(){
        List<String> csvRow = Arrays.asList("john", "smith");
        List<Person> result = transformer.convert(Arrays.asList(csvRow));

        assertEquals(result.size(), 1);

        Person person = result.get(0);

        assertEquals(person.getLastName(), "smith");
        assertEquals(person.getFirstName(), "john");
    }
    @Test
    public void assertOneLineAndImageFromFile() throws IOException, ParseException {
        byte[] bytes = new ClassPathResource("files/img.txt").getInputStream().readAllBytes();
        String invoiceImg = compressByteArrayEncodeToString(bytes);
        List<String> csvRow = Arrays.asList("South African Gold Mines Corp", "scanned_invoice_1.png", invoiceImg,"2020-09-01",
                "AA16789-1","22000.89", "USD","NEW","Goldie & Sons\nSouth African Gold Mines Corp");
        List<Invoice> result = transformer.convertInvoice(Arrays.asList(csvRow));

        assertEquals(result.size(), 1);

        Invoice invoice = result.get(0);

        assertEquals(invoice.getBuyerName(), "South African Gold Mines Corp");
        assertEquals(invoice.getImageName(), "scanned_invoice_1.png");
        assertEquals(invoice.getInvoiceImage(), invoiceImg);
        assertEquals(invoice.getInvoiceDueDate(), "2020-09-01");
        assertEquals(invoice.getInvoiceNumber(), "AA16789-1");
        assertEquals(invoice.getInvoiceAmount(),"22000.89");
        assertEquals(invoice.getInvoiceCurrency(), "USD");
        assertEquals(invoice.getInvoiceStatus(), "NEW");
        assertEquals(invoice.getSupplier(), "Goldie & Sons\nSouth African Gold Mines Corp");
    }
    @Test
    public void readCsvAssertFirstLine() throws IOException, ParseException {
        List<List<String>> records = new ArrayList<List<String>>();
        try (CSVReader csvReader = new CSVReader(new FileReader(new ClassPathResource("files/input.csv").getFile()));) {
            String[] values = null;
            while ((values = csvReader.readNext()) != null) {
                records.add(Arrays.asList(values));
            }
        }
        List<Invoice> result = transformer.convertInvoice(records);

        assertEquals(result.size(), 11);

        Invoice invoice = result.get(1);

        assertEquals(invoice.getBuyerName(), "South African Gold Mines Corp");
        assertEquals(invoice.getImageName(), "scanned_invoice_1.png");
        assertEquals(invoice.getInvoiceDueDate(), "2020-09-01");
        assertEquals(invoice.getInvoiceNumber(), "AA16789-1");
        assertEquals(invoice.getInvoiceAmount(),"22000.89");
        assertEquals(invoice.getInvoiceCurrency(), "USD");
        assertEquals(invoice.getInvoiceStatus(), "NEW");
    }


}
