package com.zetcode;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FopFactory;
import org.apache.xmlgraphics.util.MimeConstants;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class JavaXLsfoEx {

    public static void main(String[] args) throws FOPException, TransformerException, IOException {

        // XSL FO file
        var xsltFile = new File("src/main/resources/stylesheet.xsl");

        // input XML file
        var xmlSource = new StreamSource(new File("src/main/resources/contacts.xml"));

        // create an instance of FOP factory
        var fopFactory = FopFactory.newInstance(new File(".").toURI());

        // a user agent is needed for transformation
        var foUserAgent = fopFactory.newFOUserAgent();

        try(var os = new FileOutputStream("src/main/resources/output.pdf")) {

            // Construct FOP with desired output format
            var fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, os);

            // Setup XSLT
            var factory = TransformerFactory.newInstance();
            var transformer = factory.newTransformer(new StreamSource(xsltFile));

            // Resulting SAX events (the generated FO) must be piped through to  FOP
            var result = new SAXResult(fop.getDefaultHandler());

            // Start XSLT transformation and FOP processing
            // That's where the XML is first transformed to XSL-FO and then
            // PDF is created
            transformer.transform(xmlSource, result);

        }
    }
}
