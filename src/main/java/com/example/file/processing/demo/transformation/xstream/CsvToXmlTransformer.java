package com.example.file.processing.demo.transformation.xstream;

import com.example.file.processing.demo.transformation.Invoice;
import com.example.file.processing.demo.transformation.Person;
import com.thoughtworks.xstream.XStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CsvToXmlTransformer {
    @Autowired
    private XStream xStream;

    public List<String> convert(List<List<String>> csvRows){
        List<String> resultingXml = new ArrayList<>();
        for (List<String> csvRow : csvRows) {
            Person person = new Person();
            person.setFirstName(csvRow.get(0));
            person.setLastName(csvRow.get(1));
            resultingXml.add(xStream.toXML(person));
        }
        return resultingXml;
    }
    public List<String> convertInvoice(List<List<String>> csvRows) throws ParseException {
        List<String> resultingXml = new ArrayList<>();
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
            resultingXml.add(xStream.toXML(invoice));
        }
        return resultingXml;
    }

    public void setXstream(XStream xstream) {
        this.xStream = xstream;
    }

    public static byte[] compressString(String srcTxt)  throws IOException {
        if (srcTxt != null) {
//for reference https://lifelongprogrammer.blogspot.com/2013/11/java-use-zip-stream-and-base64-to-compress-big-string.html
            ByteArrayOutputStream rstBao = new ByteArrayOutputStream();
            GZIPOutputStream zos = new GZIPOutputStream(rstBao);
            try {
                zos.write(srcTxt.getBytes());
                byte[] bytes = rstBao.toByteArray();
//                return Base64.encodeBase64String(bytes);
                return bytes;
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                zos.close();
                rstBao.close();
            }
        }
        return null;
    }
}
