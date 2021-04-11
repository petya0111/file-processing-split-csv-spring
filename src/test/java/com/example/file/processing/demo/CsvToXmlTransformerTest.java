package com.example.file.processing.demo;

import com.example.file.processing.demo.transformation.Invoice;
import com.example.file.processing.demo.transformation.Person;
import com.example.file.processing.demo.transformation.xstream.CsvToXmlTransformer;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPOutputStream;
import junit.framework.Assert;
import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import static org.junit.Assert.assertEquals;

public class CsvToXmlTransformerTest {
    private CsvToXmlTransformer transformer;
    XStream xstream;
    @Value("files/input.csv")
    private Resource inputCsv;
    @Value("files/img.txt")
    private Resource testImg;

    @Before
    public void setup(){
        transformer = new CsvToXmlTransformer();
        xstream = new XStream();
        // clear out existing permissions and set own ones
        xstream.addPermission(NoTypePermission.NONE);
// allow some basics
        xstream.addPermission(NullPermission.NULL);
        xstream.addPermission(PrimitiveTypePermission.PRIMITIVES);
        xstream.allowTypeHierarchy(Collection.class);
// allow any type from the same package
        xstream.allowTypesByWildcard(new String[] {
                "com.example.file.**"
        });
//    resolve vulnerable    https://stackoverflow.com/questions/44698296/security-framework-of-xstream-not-initialized-xstream-is-probably-vulnerable
        transformer.setXstream(xstream);
    }

    @Test
    public void convert(){
        List<String> csvRow = Arrays.asList("john", "smith");
        List<String> result = transformer.convert(Arrays.asList(csvRow));

        Assert.assertEquals(result.size(), 1);

        Person person = (Person) xstream.fromXML(result.get(0));

        assertEquals(person.getLastName(), "smith");
        assertEquals(person.getFirstName(), "john");
    }
    @Test
    public void convertInvoice() throws IOException, ParseException {
        byte[] bytes = new ClassPathResource("files/img.txt").getInputStream().readAllBytes();
        String invoiceImg = compressByteArrayEncodeToString(bytes);
        List<String> csvRow = Arrays.asList("South African Gold Mines Corp", "scanned_invoice_1.png", invoiceImg,"2020-09-01",
                "AA16789-1","22000.89", "USD","NEW","Goldie & Sons\nSouth African Gold Mines Corp");
        List<String> result = transformer.convertInvoice(Collections.singletonList(csvRow));
        assertEquals(result.size(), 1);
        Invoice invoice = (Invoice) xstream.fromXML(result.get(0));
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
    public static String compressByteArrayEncodeToString(byte[] srcBytes)  throws IOException {
        if (srcBytes != null) {
//for reference https://lifelongprogrammer.blogspot.com/2013/11/java-use-zip-stream-and-base64-to-compress-big-string.html
            ByteArrayOutputStream rstBao = new ByteArrayOutputStream();
            GZIPOutputStream zos = new GZIPOutputStream(rstBao);
            try {
                zos.write(srcBytes);
                byte[] bytes = rstBao.toByteArray();
                return Base64.encodeBase64String(bytes);
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
