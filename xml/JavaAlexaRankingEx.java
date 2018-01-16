package com.zetcode;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class JavaAlexaRankingEx {

    public static void main(String[] args) throws MalformedURLException, 
            IOException, ParserConfigurationException, SAXException {

        String webSite = "something.com";
        
        int ranking = 0;

        String url = String.format("http://data.alexa.com/data?cli=10&url=%s", webSite);

        URLConnection conn = new URL(url).openConnection();
        
        try (InputStream is = conn.getInputStream()) {
            
            DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            
            Document doc = builder.parse(is);
            
            Element element = doc.getDocumentElement();
            
            NodeList nodeList = element.getElementsByTagName("POPULARITY");
            
            if (nodeList.getLength() > 0) {
                
                Element elementAttribute = (Element) nodeList.item(0);
                
                ranking = Integer.valueOf(elementAttribute.getAttribute("TEXT"));
            }
        }

        System.out.printf("Ranking of %s: %d%n", webSite, ranking);;
    }
}
