package com.zetcode;


import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;

public class JavaXmlTransformCsv {

    // Program arguments stylesheet.csl users.xml users.csv
    // TODO add nationality element

    public static void main(String[] args) throws TransformerException {

        var xslt = new StreamSource(new File(args[0]));
        var data = new StreamSource(new File(args[1]));

        var factory = TransformerFactory.newInstance();
        var transformer = factory.newTransformer(xslt);

        transformer.transform(data, new StreamResult(new File(args[2])));

        System.out.println("Transformation succeeded");
    }
}
