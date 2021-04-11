package com.example.file.processing.demo;

import com.example.file.processing.demo.transformation.FileConversionSplitByParameter;
import com.example.file.processing.demo.transformation.Invoice;
import com.example.file.processing.demo.transformation.csv.CsvToPojoTransformer;
import java.io.File;
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
import org.junit.Before;
import org.junit.Test;
import static java.util.stream.Collectors.groupingBy;
import static junit.framework.Assert.assertEquals;

/**
 * @author Petya Marinova
 */
public class CSVSplitterToXMLTest {
    private CsvToPojoTransformer transformer;
    private FileConversionSplitByParameter fileConversionSplitByParameter;


    @Before
    public void setup() {
        transformer = new CsvToPojoTransformer();
        fileConversionSplitByParameter = new FileConversionSplitByParameter(transformer);
    }

    @Test
    public void iterateCsvAndSplitToXmlFiles() throws Exception {
        List<Invoice> result = transformer.convertCsvToPojo();
        assertEquals(result.size(), 11);
        fileConversionSplitByParameter.generateCSVAndSplitBuyerName(result);
    }
}


