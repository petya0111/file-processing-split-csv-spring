package com.example.file.processing.demo;

import com.example.file.processing.demo.transformation.Invoice;
import java.util.List;
 
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "invoicesList")
@XmlAccessorType (XmlAccessType.FIELD)
public class InvoicesList
{
    @XmlElement(name = "invoice",type = Invoice.class)
    private List<Invoice> invoices = null;
 
    public List<Invoice> getInvoices() {
        return invoices;
    }
 
    public void setInvoices(List<Invoice> employees) {
        this.invoices = employees;
    }

    public InvoicesList(List<Invoice> invoices) {
        this.invoices = invoices;
    }
    public InvoicesList(){

    }
}