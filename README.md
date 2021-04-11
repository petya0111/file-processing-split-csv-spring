## File Processr from csv to xml

**Introduction**

Project for converting data from one file format to another & splitting it to field parameter.

**Main Unit tests**

The base running logic is in unit tests.

**CSVSplitterToSeparateCsvFilesTest**

Iteration for the csv file and setting it into pojo;
afterwards iterating the list of invoice pojo & grouping it by buyer & saving into separated csv files by buyer
The files csv buyers are saved in filesystem it checks for directory is existing.

**CSVSplitterToXMLTest**

Iteration for the csv file and setting it into pojo;
afterwards iterating the list of invoice pojo  & grouping it by buyer & saving into separated xml files by buyer; 
invoice image field is omitted
The files xml buyers are saved in filesystem it checks for directory is existing.

**Bonus tests with Xstream (not required, but it took me time to overthink the requirement)**

**CsvToXmlTransformerTest**

Testing first row of data custom CsvToXmlTransformer with xStream convert
The invoice img is separated in file & compressed byte[]; all fields are asserted

**CsvToPersonTransformerTest**

Conversion of the first row of csv data and converting into pojo, the fields are asserted

<details>
  <summary>View task</summary>

## Task
At T. we often deal with ingesting data and converting it into target formats for consumption
into different systems - source and destination systems vary from accounting systems, dataextracts to REST APIs or SOAP calls. An example file is attached and contains invoice data
(amounts, identifiers etc) as well as a base64 encoded invoice image. The real files can be 2GB+.
Problem description:
We need to write a system that parses and ingests the given (large) file and has the ability to
produce the two different output formats specified below.
As a user of that system I need to be able to configure or otherwise specify which of the two
output formats should be produced.
The new output formats will then later on be ingested by other systems - the integrity of data and
files has to stay. The later ingestion of the newly produced files is not part of this exercise.
The two destination formats should be:
1. CSV file of the original data but split up by 'buyer'. So if there are 10 different buyers overall
   there should be 10 different output files. The rest of the data in the CSV should be arranged in
   the same way as in the input file.
2. XML file of the original data split up by 'buyer'. The invoice image should not be part of the
   XML data but the single invoice files should be extracted from the CSV and be placed into the
   file-system. The format of the XML should loosely follow the input CSV in regards to nodenames etc.You can decide any changes to folder-structure etc. of the output format.
   It is up to you what language you develop the solution in as long as we can see the solution
   running and walk through the code and output files you produced together with.
   Unit tests would be appreciated.
</details>