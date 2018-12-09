package com.zetcode;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;

public class JavaXmlTransformHtmlEx {

    // Program arguments: stylesheet.xsl input.xml output.html
    
    public static void main(String[] args) throws TransformerException {

        var xslt = new StreamSource(new File(args[0]));
        var data = new StreamSource(new File(args[1]));

        var factory = TransformerFactory.newInstance();
        var transformer = factory.newTransformer(xslt);

        transformer.transform(data, new StreamResult(new File(args[2])));
    }
}
