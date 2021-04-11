package com.example.file.processing.demo.transformation.csv;

import com.example.file.processing.demo.transformation.Invoice;
import com.example.file.processing.demo.transformation.Person;
import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class CsvToPojoTransformer {
    public List<Person> convert(List<List<String>> csvRows){
        List<Person> results = new ArrayList<Person>();
        for (List<String> csvRow : csvRows) {
            Person person = new Person();
            person.setFirstName(csvRow.get(0));
            person.setLastName(csvRow.get(1));
            results.add(person);
        }
        return results;
    }
    public List<Invoice> convertInvoice(List<List<String>> csvRows) throws ParseException {
        List<Invoice> results = new ArrayList<Invoice>();
        for (List<String> csvRow : csvRows) {
            Invoice invoice = new Invoice();
            invoice.setBuyerName(csvRow.get(0));
            invoice.setImageName(csvRow.get(1));
            String invoiceImage = csvRow.get(2);
            invoice.setInvoiceImage(invoiceImage);
            invoice.setInvoiceDueDate(csvRow.get(3));

            invoice.setInvoiceNumber(csvRow.get(4));
            invoice.setInvoiceAmount(csvRow.get(5));
            invoice.setInvoiceCurrency(csvRow.get(6));
            invoice.setInvoiceStatus(csvRow.get(7));
            invoice.setSupplier(csvRow.get(8));
            results.add(invoice);
        }
        return results;
    }
    public List<Invoice> convertCsvToPojo() throws ParseException {
        List<List<String>> records = new ArrayList<List<String>>();
        try (CSVReader csvReader = new CSVReader(new FileReader(new ClassPathResource("files/input.csv").getFile()));) {
            String[] values = null;
            while ((values = csvReader.readNext()) != null) {
                records.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  convertInvoice(records);
    }
}
